package dev.isnow.mcrekus.module.impl.deathchest.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.deathchest.DeathChest;
import dev.isnow.mcrekus.module.impl.deathchest.DeathChestModule;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BreakEvent extends ModuleAccessor<DeathChestModule> implements Listener {

    @EventHandler
    public void onBreak(final BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.CHEST) return;

        final DeathChest deathChest = getModule().getDeathChests().get(RekusLocation.fromBukkitLocation(event.getBlock().getLocation()));

        if (deathChest == null) return;

        for(final ItemStack item : deathChest.getInventory().getContents()) {
            if(item == null) continue;

            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
            deathChest.getInventory().remove(item);
        }

        deathChest.remove();
    }
}
