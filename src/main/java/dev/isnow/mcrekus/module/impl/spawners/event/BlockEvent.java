package dev.isnow.mcrekus.module.impl.spawners.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawners.SpawnersModule;
import dev.isnow.mcrekus.module.impl.spawners.config.SpawnersConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.scheduler.BukkitTask;
import org.kingdoms.constants.land.location.SimpleChunkLocation;
import org.kingdoms.constants.player.KingdomPlayer;

public class BlockEvent extends ModuleAccessor<SpawnersModule> implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(final BlockBreakEvent event) {
        if (!(event.getBlock().getState() instanceof CreatureSpawner)) return;

        final Player player = event.getPlayer();

        final SimpleChunkLocation chunk = SimpleChunkLocation.of(event.getBlock().getLocation().getChunk());
        if (chunk.getLand() != null && chunk.getLand().isClaimed() && chunk.getLand().getKingdom() != null) {
            final KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(event.getPlayer());

            if (kp.getKingdom() == null) {
                return;
            }

            if (!kp.getKingdom().isClaimed(chunk)) {
                return;
            }
        }

        final SpawnersConfig config = getModule().getConfig();

        if (getModule().isSpawnerBroken(event.getBlock().getLocation())) {
            player.sendMessage(ComponentUtil.deserialize(config.getCantBreakBrokenSpawnerMessage()));
            event.setCancelled(true);
        } else {
            final BukkitTask task = getModule().getSpawnerTask(RekusLocation.fromBukkitLocation(event.getBlock().getLocation()));
            if (task != null) {
                task.cancel();
            }

            final RekusLocation location = RekusLocation.fromBukkitLocation(event.getBlock().getLocation());

            getModule().removeSpawnerTask(location);
            getModule().removeSpawnerToRepair(location);

            RekusLogger.debug("Spawner broken at " + location);
        }

        final ItemStack playerItem = event.getPlayer().getInventory().getItemInMainHand();

        if (!playerItem.getType().name().contains("PICKAXE")) return;

        if (playerItem.getEnchantmentLevel(Enchantment.SILK_TOUCH) == 0) return;


        final ItemStack spawner = new ItemStack(Material.SPAWNER);
        final BlockStateMeta bsm = (BlockStateMeta) spawner.getItemMeta();
        bsm.setBlockState(event.getBlock().getState());
        spawner.setItemMeta(bsm);

        event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), spawner);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (event.getBlock().getType() != Material.SPAWNER) return;

        final BlockStateMeta bsm = (BlockStateMeta) event.getItemInHand().getItemMeta();
        final CreatureSpawner creatureSpawner = (CreatureSpawner) bsm.getBlockState();

        final CreatureSpawner blockCreatureSpawner = (CreatureSpawner) event.getBlock().getState();
        blockCreatureSpawner.setSpawnedType(creatureSpawner.getSpawnedType());
        blockCreatureSpawner.update();

        final RekusLocation location = RekusLocation.fromBukkitLocation(event.getBlock().getLocation());

        getModule().scheduleBreakingSpawner(location);
        RekusLogger.debug("Spawner placed at " + location);
    }

}
