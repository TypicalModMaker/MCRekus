package dev.isnow.mcrekus.module.impl.essentials.command.speed;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;

@CommandAlias("speed|predkosc")
@Description("Command to set your flight/walk speed")
@CommandPermission("mcrekus.speed")
@SuppressWarnings("unused")
public class SpeedCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    public SpeedCommand() {
        MCRekus.getInstance().getCommandManager().registerCompletion("speedType", context -> Arrays.stream(SpeedType.values())
                .map(SpeedType::name)
                .collect(Collectors.toList()));
    }

    @Default
    @CommandCompletion("@speedType [speed/reset] @players")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        if(args.length == 0) {
            player.sendMessage(ComponentUtil.deserialize(config.getSpeedUsageMessage()));
            return;
        }

        final SpeedType type;
        try {
            type = SpeedType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(ComponentUtil.deserialize(config.getSpeedInvalidTypeMessage()));
            return;
        }

        if(args.length == 1) {
            player.sendMessage(ComponentUtil.deserialize(config.getSpeedUsageMessage()));
            return;
        }

        final String speedString = args[1];

        if(speedString.equalsIgnoreCase("reset")) {
            switch (type) {
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

        final float speed;
        try {
            speed = Float.parseFloat(speedString);
        } catch (NumberFormatException e) {
            player.sendMessage(ComponentUtil.deserialize(config.getSpeedInvalidSpeedMessage()));
            return;
        }

        if(speed < -1 || speed > 1) {
            player.sendMessage(ComponentUtil.deserialize(config.getSpeedInvalidSpeedMessage()));
            return;
        }

        final Player target = args.length == 3 ? player.getServer().getPlayer(args[2]) : player;
        if(target == null) {
            player.sendMessage(ComponentUtil.deserialize(config.getSpeedPlayerNotFoundMessage(), null, "%player%", args[2]));
            return;
        }

        switch (type) {
            case FLIGHT:
                target.setFlySpeed(speed);
                player.sendMessage(ComponentUtil.deserialize(config.getFlightSpeedChangedMessage(), null, "%speed%", speed, "%player%", target));
                break;
            case WALK:
                target.setWalkSpeed(speed);
                player.sendMessage(ComponentUtil.deserialize(config.getWalkSpeedChangedMessage(), null, "%speed%", speed, "%player%", target));
                break;
        }
    }
}
