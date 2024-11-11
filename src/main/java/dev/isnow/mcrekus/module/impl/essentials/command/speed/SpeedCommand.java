package dev.isnow.mcrekus.module.impl.essentials.command.speed;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
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
import org.bukkit.entity.Player;

@Command({"speed", "predkosc"})
@Description("Command to set your flight/walk speed")
@Permission("mcrekus.speed")
@SuppressWarnings("unused")
public class SpeedCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void executeDefault(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getSpeedUsageMessage()));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("type") @SuggestionProvider("speedtype") final String type, @Named("action") @Suggest("speed/reset") final String action, @Named("speed") final float speed, @Named("player") @Optional final Player target) {
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
