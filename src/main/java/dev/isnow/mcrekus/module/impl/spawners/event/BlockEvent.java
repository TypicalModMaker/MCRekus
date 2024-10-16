package dev.isnow.mcrekus.module.impl.spawners.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawners.SpawnersModule;
import dev.isnow.mcrekus.module.impl.spawners.config.SpawnersConfig;
import dev.isnow.mcrekus.module.impl.spawners.spawners.RekusSpawner;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import dev.rosewood.rosestacker.event.SpawnerStackEvent;
import dev.rosewood.rosestacker.event.SpawnerUnstackEvent;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.kingdoms.constants.land.location.SimpleChunkLocation;
import org.kingdoms.constants.player.KingdomPlayer;

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

        final RekusSpawner spawner = getModule().getSpawners().get(location);

        if (spawner.isBroken()) {
            player.sendMessage(ComponentUtil.deserialize(config.getCantBreakBrokenSpawnerMessage()));
            event.setCancelled(true);
        } else if(event.getStack().getStackSize() == 1) {
            spawner.getTask().cancel();
            getModule().getSpawners().remove(location);

            RekusLogger.debug("Spawner broken at " + location);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(final SpawnerStackEvent event) {
        RekusLogger.info(String.valueOf(event.getStack().getStackSize()));
        if(event.getStack().getStackSize() != 0) {
            return;
        }

        final RekusLocation location = RekusLocation.fromBukkitLocation(event.getStack().getLocation());

        final RekusSpawner spawner = new RekusSpawner(location);
        getModule().getSpawners().put(location, spawner);

        RekusLogger.debug("Spawner placed at " + location);
    }

}
