package dev.isnow.mcrekus.module.impl.spawnprotection.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawnprotection.SpawnProtectionModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

public class SpreadEvent extends ModuleAccessor<SpawnProtectionModule> implements Listener {

    @EventHandler
    public void onSpread(final BlockSpreadEvent event) {
        if (!getModule().getSpawnCuboid().isIn(event.getBlock())) return;

        event.setCancelled(true);
    }
}
