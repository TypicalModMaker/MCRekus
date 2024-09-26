package dev.isnow.mcrekus.module.impl.essentials.command.teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.TeleportUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("tp|teleport")
@Description("Command to teleport to someone or teleport 2 different players")
@CommandPermission("mcrekus.teleport")
@SuppressWarnings("unused")
public class TeleportCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    @CommandCompletion("@players @players")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        switch (args.length) {
            case 0:
                player.sendMessage(ComponentUtil.deserialize(config.getTeleportNoArgsMessage()));
                return;
            case 1:
                Player target = player.getServer().getPlayer(args[0]);
                TeleportUtil.teleportPlayers(player, player, target, config);
                return;
            case 2:
                target = player.getServer().getPlayer(args[0]);
                final Player destination = player.getServer().getPlayer(args[1]);

                TeleportUtil.teleportPlayers(player, target, destination, config);
                return;
            case 3:
                final Location location = new Location(player.getWorld(), Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]), player.getYaw(), player.getPitch());

                player.teleport(location);
                player.sendMessage(ComponentUtil.deserialize(config.getTeleportToLocationMessage(), null, "%coordinates%", ComponentUtil.formatLocation(location, true)));
                return;
            default:
                player.sendMessage(ComponentUtil.deserialize(config.getTeleportNoArgsMessage()));
        }

    }
}
