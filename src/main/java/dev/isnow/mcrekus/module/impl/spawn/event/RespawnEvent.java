package dev.isnow.mcrekus.module.impl.spawn.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawn.SpawnModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnEvent extends ModuleAccessor<SpawnModule> implements Listener {

    @EventHandler
    public void onRespawn(final PlayerRespawnEvent event) {
        if(getModule().getConfig().isTeleportOnRespawn()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().teleport(getModule().getConfig().getSpawnLocation().toBukkitLocation());
                }
            }.runTaskLater(MCRekus.getInstance(), 2L);
        }
    }

}
