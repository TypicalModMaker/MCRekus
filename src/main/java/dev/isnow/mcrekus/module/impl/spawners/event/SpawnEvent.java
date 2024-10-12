package dev.isnow.mcrekus.module.impl.spawners.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawners.SpawnersModule;
import dev.isnow.mcrekus.module.impl.spawners.spawners.RekusSpawner;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

public class SpawnEvent extends ModuleAccessor<SpawnersModule> implements Listener {

    @EventHandler
    public void onSpawn(final SpawnerSpawnEvent event) {
        if (event.getSpawner() == null) return;

        final CreatureSpawner spawner = event.getSpawner();

        final RekusSpawner rekusSpawner = getModule().getSpawners().get(RekusLocation.fromBukkitLocation(spawner.getLocation()));

        if (!rekusSpawner.isBroken()) return;

        event.setCancelled(true);
    }
}
