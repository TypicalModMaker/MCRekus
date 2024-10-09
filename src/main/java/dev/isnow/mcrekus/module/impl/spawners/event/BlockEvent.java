package dev.isnow.mcrekus.module.impl.spawners.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawners.SpawnersModule;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.land.location.SimpleChunkLocation;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.main.Kingdoms;
import org.kingdoms.managers.entity.types.KingdomEntity;

public class BlockEvent extends ModuleAccessor<SpawnersModule> implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(final BlockBreakEvent event) {
        final ItemStack playerItem = event.getPlayer().getInventory().getItemInMainHand();

        if (!playerItem.getType().name().contains("PICKAXE")) return;

        if (playerItem.getEnchantmentLevel(Enchantment.SILK_TOUCH) == 0) return;

        if (!(event.getBlock().getState() instanceof CreatureSpawner)) return;

        SimpleChunkLocation chunk = SimpleChunkLocation.of(event.getBlock().getLocation().getChunk());
        if (chunk.getLand() != null && chunk.getLand().isClaimed() && chunk.getLand().getKingdom() != null) {
            KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(event.getPlayer());

            if (kp.getKingdom() == null) {
                return;
            }

            if (!kp.getKingdom().isClaimed(chunk)) {
                return;
            }
        }

        ItemStack spawner = new ItemStack(Material.SPAWNER);
        BlockStateMeta bsm = (BlockStateMeta) spawner.getItemMeta();
        bsm.setBlockState(event.getBlock().getState());
        spawner.setItemMeta(bsm);

        event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), spawner);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (event.getBlock().getType() != Material.SPAWNER) return;

        BlockStateMeta bsm = (BlockStateMeta) event.getItemInHand().getItemMeta();
        CreatureSpawner creatureSpawner = (CreatureSpawner) bsm.getBlockState();

        CreatureSpawner blockCreatureSpawner = (CreatureSpawner) event.getBlock().getState();
        blockCreatureSpawner.setSpawnedType(creatureSpawner.getSpawnedType());
        blockCreatureSpawner.update();
    }
}
