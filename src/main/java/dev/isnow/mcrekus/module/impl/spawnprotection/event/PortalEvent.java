package dev.isnow.mcrekus.module.impl.spawnprotection.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawnprotection.SpawnProtectionModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.PortalCreateEvent.CreateReason;

public class PortalEvent extends ModuleAccessor<SpawnProtectionModule> implements Listener {

    @EventHandler
    public void onPortal(PortalCreateEvent event) {
        if (event.getBlocks().stream().anyMatch(blockState -> getModule().getSpawnCuboid().isIn(blockState.getLocation()))) {
            event.setCancelled(true);
        }
    }
}
