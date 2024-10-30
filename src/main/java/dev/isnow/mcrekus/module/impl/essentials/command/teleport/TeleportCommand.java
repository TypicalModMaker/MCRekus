package dev.isnow.mcrekus.module.impl.essentials.command.teleport;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.TeleportUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Command({"tp", "teleport"})
@Description("Command to teleport to someone or teleport 2 different players")
@Permission("mcrekus.teleport")
@SuppressWarnings("unused")
public class TeleportCommand extends ModuleAccessor<EssentialsModule> {

    @Async
    @Usage
    public void executeDefault(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getTeleportNoArgsMessage()));
    }

    @Async
    @Usage
    public void executeOne(final BukkitSource source, @Named("player") final Player target) {
        final EssentialsConfig config = getModule().getConfig();
        final Player player = source.asPlayer();

//
//        if (target == null) {
//            player.sendMessage(ComponentUtil.deserialize(config.getTeleportPlayerNotFoundMessage(), null, "%player%", args[0]));
//            return;
//        }

        TeleportUtil.teleportPlayers(player, player, target, config);
    }

    @Async
    @Usage
    public void executeTwo(final BukkitSource source, @Named("player") final Player target, @Named("destination") final Player destination) {
        final EssentialsConfig config = getModule().getConfig();
        final Player player = source.asPlayer();

        TeleportUtil.teleportPlayers(player, target, destination, config);
    }

    @Async
    @Usage
    public void executeThree(final BukkitSource source, @Named("x") final double x, @Named("y") final double y, @Named("z") final double z) {
        final EssentialsConfig config = getModule().getConfig();
        final Player player = source.asPlayer();

        final Location location = new Location(player.getWorld(), x, y, z, player.getYaw(), player.getPitch());

        player.teleport(location);
        source.reply(ComponentUtil.deserialize(config.getTeleportToLocationMessage(), null, "%coordinates%", ComponentUtil.formatLocation(location, true)));
    }
}
