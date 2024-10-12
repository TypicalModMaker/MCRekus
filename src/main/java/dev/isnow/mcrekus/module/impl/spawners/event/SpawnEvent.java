package dev.isnow.mcrekus.module.impl.spawners.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawners.SpawnersModule;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

public class SpawnEvent extends ModuleAccessor<SpawnersModule> implements Listener {

    @EventHandler
    public void onSpawn(final SpawnerSpawnEvent event) {
        if (event.getSpawner() == null) return;

        final CreatureSpawner spawner = event.getSpawner();

        if (!getModule().isSpawnerBroken(spawner.getLocation())) return;

        event.setCancelled(true);
    }
}
