package dev.isnow.mcrekus.module.impl.spawn.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawn.SpawnModule;
import dev.isnow.mcrekus.module.impl.spawn.config.SpawnConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinEvent extends ModuleAccessor<SpawnModule> implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (event.getPlayer().hasPlayedBefore()) return;

        final SpawnConfig config = getModule().getConfig();
        if (!config.isTeleportOnFirstJoin()) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                event.getPlayer().teleport(config.getSpawnLocation().toBukkitLocation());
            }
        }.runTaskLater(MCRekus.getInstance(), 2);
    }

}
