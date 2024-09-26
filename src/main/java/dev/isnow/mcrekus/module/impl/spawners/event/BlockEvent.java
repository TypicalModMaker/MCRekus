package dev.isnow.mcrekus.module.impl.spawners.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawners.SpawnersModule;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class BlockEvent extends ModuleAccessor<SpawnersModule> implements Listener {
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        final ItemStack playerItem = event.getPlayer().getInventory().getItemInMainHand();

        if (!playerItem.getType().name().contains("PICKAXE")) return;

        if (playerItem.getEnchantmentLevel(Enchantment.SILK_TOUCH) == 0) return;

        ItemStack spawner = new ItemStack(Material.SPAWNER);
        BlockStateMeta bsm = (BlockStateMeta) spawner.getItemMeta();
        bsm.setBlockState(event.getBlock().getState());
        spawner.setItemMeta(bsm);

        event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), spawner);
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (event.getBlock().getType() != Material.SPAWNER) return;

        BlockStateMeta bsm = (BlockStateMeta) event.getItemInHand().getItemMeta();
        CreatureSpawner creatureSpawner = (CreatureSpawner) bsm.getBlockState();

        CreatureSpawner blockCreatureSpawner = (CreatureSpawner) event.getBlock().getState();
        blockCreatureSpawner.setSpawnedType(creatureSpawner.getSpawnedType());
        blockCreatureSpawner.update();
    }
}
