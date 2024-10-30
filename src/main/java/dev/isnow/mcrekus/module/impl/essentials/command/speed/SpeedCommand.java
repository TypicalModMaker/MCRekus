package dev.isnow.mcrekus.module.impl.essentials.command.speed;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.command.broadcast.BroadcastType;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Optional;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Suggest;
import dev.velix.imperat.annotations.SuggestionProvider;
import dev.velix.imperat.annotations.Usage;
import dev.velix.imperat.command.parameters.CommandParameter;
import dev.velix.imperat.context.SuggestionContext;
import dev.velix.imperat.resolvers.SuggestionResolver;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;

@Command({"speed", "predkosc"})
@Description("Command to set your flight/walk speed")
@Permission("mcrekus.speed")
@SuppressWarnings("unused")
public class SpeedCommand extends ModuleAccessor<EssentialsModule> {

    public SpeedCommand() {
        super(EssentialsModule.class);

        RekusLogger.info("Registering speedType resolver");

        MCRekus.getInstance().getCommandManager().registerCompletion("speedType", new SpeedTypeResolver());
    }

    @Usage
    @Async
    public void executeDefault(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getSpeedUsageMessage()));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("type") @SuggestionProvider("speedType") String type, @Named("action") @Suggest("speed/reset") String action, @Named("speed") float speed, @Named("player") @Optional  Player target) {
        final EssentialsConfig config = getModule().getConfig();

        final SpeedType foundType;
        try {
            foundType = SpeedType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            source.reply(ComponentUtil.deserialize(config.getSpeedInvalidTypeMessage()));
            return;
        }

        if(action == null || action.isEmpty()) {
            source.reply(ComponentUtil.deserialize(config.getSpeedUsageMessage()));
            return;
        }

        final Player player = source.asPlayer();
        if(action.equalsIgnoreCase("reset")) {
            switch (foundType) {
                case FLIGHT:
                    player.setFlySpeed(0.1f);
                    player.sendMessage(ComponentUtil.deserialize(config.getFlightSpeedResetMessage()));
                    break;
                case WALK:
                    player.setWalkSpeed(0.2f);
                    player.sendMessage(ComponentUtil.deserialize(config.getWalkSpeedResetMessage()));
                    break;
            }
            return;
        }

        if(speed < -1 || speed > 1) {
            player.sendMessage(ComponentUtil.deserialize(config.getSpeedInvalidSpeedMessage()));
            return;
        }

        if(target == null) {
            player.sendMessage(ComponentUtil.deserialize(config.getSpeedPlayerNotFoundMessage()));
            return;
        }

        switch (foundType) {
            case FLIGHT:
                target.setFlySpeed(speed);
                source.reply(ComponentUtil.deserialize(config.getFlightSpeedChangedMessage(), null, "%speed%", speed, "%player%", target));
                break;
            case WALK:
                target.setWalkSpeed(speed);
                source.reply(ComponentUtil.deserialize(config.getWalkSpeedChangedMessage(), null, "%speed%", speed, "%player%", target));
                break;
        }
    }
}
class SpeedTypeResolver implements SuggestionResolver<BukkitSource> {

    @Override
    public Collection<String> autoComplete(SuggestionContext<BukkitSource> context,
            CommandParameter<BukkitSource> parameter) {
        return Arrays.stream(SpeedType.values())
                .map(SpeedType::name)
                .collect(Collectors.toList());
    }
}