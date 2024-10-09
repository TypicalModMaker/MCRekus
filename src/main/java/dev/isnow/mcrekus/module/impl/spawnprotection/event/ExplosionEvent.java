package dev.isnow.mcrekus.module.impl.spawnprotection.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawnprotection.SpawnProtectionModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplosionEvent extends ModuleAccessor<SpawnProtectionModule> implements Listener {

    @EventHandler
    public void onEntityExplosion(final EntityExplodeEvent event) {
        if (!getModule().getSpawnCuboid().isIn(event.getEntity())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockExplosion(final BlockExplodeEvent event) {
        if (!getModule().getSpawnCuboid().isIn(event.getBlock())) return;

        event.setCancelled(true);
    }
}
