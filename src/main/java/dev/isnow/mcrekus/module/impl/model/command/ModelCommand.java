package dev.isnow.mcrekus.module.impl.model.command;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.model.ModelModule;
import dev.isnow.mcrekus.module.impl.model.config.ModelConfig;
import dev.isnow.mcrekus.module.impl.model.parser.impl.Model;
import dev.isnow.mcrekus.module.impl.model.tracker.TrackedModel;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Greedy;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Optional;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Suggest;
import dev.velix.imperat.annotations.SuggestionProvider;
import dev.velix.imperat.annotations.Usage;
import me.tofaa.entitylib.wrapper.WrapperEntity;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Command("model")
@Permission("mcrekus.model")
@Description("Main command for the model module")
public class ModelCommand extends ModuleAccessor<ModelModule> {

    @Usage
    @Async
    public void defaultCommand(final BukkitSource source) {
        final ModelConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getCommandUsageMessage()));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("action") @Suggest({"list", "place", "remove", "parseall"}) final String action, @Named("model") @SuggestionProvider("model") @Optional @Greedy final String model) {
        final ModelConfig config = getModule().getConfig();

        switch (action) {
            case "list":
                source.reply("&7List of models:");
                getModule().getParsedModels().forEach((name, project) -> source.reply("&7- &e" + name));
                break;
            case "place":
                final Model foundModel = getModule().getParsedModels().get(model);
                if(foundModel == null) {
                    source.reply("&cModel not found!");
                    return;
                }

                final Player player = source.asPlayer();

                try {
                    final TrackedModel trackedModel = getModule().getModelTracker().spawnModel(player.getLocation(), foundModel);

                    for(final WrapperEntity object : trackedModel.getObjects()) {
                        object.addViewer(player.getUniqueId());
                    }

                    source.reply("&aPlaced model! ID: " + trackedModel.getBase().getEntityId());
                } catch (Exception e) {
                    RekusLogger.error("Failed to spawn model: " + foundModel.getName());
                    e.printStackTrace();
                    source.reply("&cFailed to place model! Check console for more information.");
                }
                break;
            case "remove":
                final TrackedModel trackedModel = getModule().getModelTracker().getModel(Integer.parseInt(model));

                if(trackedModel == null) {
                    source.reply("&cModel not found!");
                    return;
                }

                getModule().getModelTracker().despawnModel(Integer.parseInt(model));
                source.reply("&aRemoved model!");
                break;
            case "parseall":
                getModule().parseModels();
                source.reply("&aRe-Parsed all models!");
                break;
            default:
                source.reply(ComponentUtil.deserialize(config.getCommandUsageMessage()));
        }

        if(!source.isConsole()) {
            final Player player = source.asPlayer();
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
    }
}
