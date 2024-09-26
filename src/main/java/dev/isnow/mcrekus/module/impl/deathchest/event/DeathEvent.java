package dev.isnow.mcrekus.module.impl.deathchest.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.deathchest.DeathChestModule;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class DeathEvent extends ModuleAccessor<DeathChestModule> implements Listener {

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        final List<ItemStack> drops = event.getDrops();

        if (drops.isEmpty()) {
            return;
        }

        final Block playerBlock = event.getPlayer().getLocation().getBlock();

        playerBlock.setType(Material.CHEST);
        final Chest chest = (Chest) playerBlock.getState();

        for (final ItemStack drop : drops) {
            if (drop == null) {
                continue;
            }

            chest.getInventory().addItem(drop);
        }

        event.getDrops().clear();
    }

}
