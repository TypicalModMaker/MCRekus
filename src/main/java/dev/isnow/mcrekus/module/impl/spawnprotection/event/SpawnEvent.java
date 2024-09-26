package dev.isnow.mcrekus.module.impl.spawnprotection.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawnprotection.SpawnProtectionModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SpawnEvent extends ModuleAccessor<SpawnProtectionModule> implements Listener {

    @EventHandler
    public void onSpawn(final CreatureSpawnEvent event) {
        if (switch (event.getSpawnReason()) {
            case CUSTOM, COMMAND -> true;
            default -> false;
        }) return;

        // TODO: ADD MORE EDGE CASES
        if (event.getEntityType() == EntityType.DROPPED_ITEM) return;

        if (!getModule().getSpawnCuboid().isIn(event.getEntity())) return;

        event.setCancelled(true);
    }
}
