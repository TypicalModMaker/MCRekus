package dev.isnow.mcrekus.module.impl.spawners.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawners.SpawnersModule;
import dev.isnow.mcrekus.module.impl.spawners.config.SpawnersConfig;
import dev.isnow.mcrekus.module.impl.spawners.spawners.RekusSpawner;
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
        final RekusLocation location = RekusLocation.fromBukkitLocation(event.getBlock().getLocation());

        final RekusSpawner spawner = getModule().getSpawners().get(location);

        if (spawner.isBroken()) {
            player.sendMessage(ComponentUtil.deserialize(config.getCantBreakBrokenSpawnerMessage()));
            event.setCancelled(true);
        } else {
            spawner.getTask().cancel();
            getModule().getSpawners().remove(location);

            RekusLogger.debug("Spawner broken at " + location);
        }

        final ItemStack playerItem = event.getPlayer().getInventory().getItemInMainHand();

        if (!playerItem.getType().name().contains("PICKAXE")) return;

        if (playerItem.getEnchantmentLevel(Enchantment.SILK_TOUCH) == 0) return;


        final ItemStack spawnerItem = new ItemStack(Material.SPAWNER);
        final BlockStateMeta bsm = (BlockStateMeta) spawnerItem.getItemMeta();
        bsm.setBlockState(event.getBlock().getState());
        spawnerItem.setItemMeta(bsm);

        event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), spawnerItem);
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

        final RekusSpawner spawner = new RekusSpawner(location);
        getModule().getSpawners().put(location, spawner);

        RekusLogger.debug("Spawner placed at " + location);
    }

}
