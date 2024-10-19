package dev.isnow.mcrekus.module.impl.spawners.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.data.SpawnerData;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawners.SpawnersModule;
import dev.isnow.mcrekus.module.impl.spawners.config.SpawnersConfig;
import dev.isnow.mcrekus.module.impl.spawners.spawners.RekusSpawner;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import dev.rosewood.rosestacker.event.SpawnerStackEvent;
import dev.rosewood.rosestacker.event.SpawnerUnstackEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BlockEvent extends ModuleAccessor<SpawnersModule> implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(final SpawnerUnstackEvent event) {
        final Player player = event.getPlayer();

        if(event.getStack().getWorld().getName().equalsIgnoreCase("earthsmp_nether") && event.getStack().getSpawner().getSpawnedType() == EntityType.BLAZE) {
            player.sendMessage(ComponentUtil.deserialize(getModule().getConfig().getCantBreakSpawnerMessage()));
            event.setCancelled(true);
            return;
        };

//        final SimpleChunkLocation chunk = SimpleChunkLocation.of(event.getStack().getLocation().getChunk());
//        if (chunk.getLand() != null && chunk.getLand().isClaimed() && chunk.getLand().getKingdom() != null) {
//            final KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(event.getPlayer());
//
//            if (kp.getKingdom() == null) {
//                return;
//            }
//
//            if (!kp.getKingdom().isClaimed(chunk)) {
//                return;
//            }
//        }


        final SpawnersConfig config = getModule().getConfig();
        final RekusLocation location = RekusLocation.fromBukkitLocation(event.getStack().getLocation());

        RekusSpawner spawner = getModule().getSpawners().get(location);

        if (spawner == null) {
            spawner = new RekusSpawner(location);

            getModule().getSpawners().put(location, spawner);

            final SpawnerData spawnerData = new SpawnerData(location);
            spawnerData.save();
        }

        if (spawner.isBroken()) {
            player.sendMessage(ComponentUtil.deserialize(config.getCantBreakBrokenSpawnerMessage()));
            event.setCancelled(true);
        } else if(event.getStack().getStackSize() == 1) {
            spawner.getTask().cancel();
            getModule().getSpawners().remove(location);

            RekusLogger.debug("Spawner broken at " + location);
            MCRekus.getInstance().getDatabaseManager().getSpawnerAsync(location, (session, data) -> {
                if (data != null) {
                    data.delete(session);
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(final SpawnerStackEvent event) {
        if(event.getStack().getStackSize() != 0) {
            return;
        }

        final RekusLocation location = RekusLocation.fromBukkitLocation(event.getStack().getLocation());

        final RekusSpawner spawner = new RekusSpawner(location);
        getModule().getSpawners().put(location, spawner);

        RekusLogger.debug("Spawner placed at " + location);

        final SpawnerData data = new SpawnerData(location);
        data.save();
    }

}
