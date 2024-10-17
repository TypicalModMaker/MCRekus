package dev.isnow.mcrekus.module.impl.spawners.event;

import dev.isnow.mcrekus.data.SpawnerData;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawners.SpawnersModule;
import dev.isnow.mcrekus.module.impl.spawners.spawners.RekusSpawner;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

public class SpawnEvent extends ModuleAccessor<SpawnersModule> implements Listener {

    @EventHandler
    public void onSpawn(final SpawnerSpawnEvent event) {
        if (event.getSpawner() == null) return;

        final CreatureSpawner spawner = event.getSpawner();

        RekusSpawner rekusSpawner = getModule().getSpawners().get(RekusLocation.fromBukkitLocation(spawner.getLocation()));

        if (rekusSpawner == null) {
            final RekusLocation location = RekusLocation.fromBukkitLocation(spawner.getLocation());

            rekusSpawner = new RekusSpawner(location);

            getModule().getSpawners().put(RekusLocation.fromBukkitLocation(spawner.getLocation()), rekusSpawner);

            final SpawnerData spawnerData = new SpawnerData(location);
            spawnerData.save();
        }

        if (!rekusSpawner.isBroken()) return;

        event.setCancelled(true);
    }
}
