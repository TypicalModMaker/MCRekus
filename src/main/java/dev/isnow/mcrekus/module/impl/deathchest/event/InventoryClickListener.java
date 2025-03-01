package dev.isnow.mcrekus.module.impl.deathchest.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.deathchest.DeathChest;
import dev.isnow.mcrekus.module.impl.deathchest.DeathChestModule;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InventoryClickListener extends ModuleAccessor<DeathChestModule> implements Listener {

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }

        final DeathChest deathChest = getModule().getDeathChests().values().stream().filter(deathChest1 -> deathChest1.getInventory().equals(event.getClickedInventory())).findFirst().orElse(null);

        if (deathChest == null) return;

        final long count = Arrays.stream(event.getClickedInventory().getContents()).filter(itemStack -> itemStack != null && itemStack.getType() != Material.AIR).count();

        if ((event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || event.getAction() == InventoryAction.PICKUP_ALL) && count == 1) {
            deathChest.remove();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(final PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        if (event.getClickedBlock().getType() != Material.CHEST) return;

        final DeathChest deathChest = getModule().getDeathChests().get(RekusLocation.fromBukkitLocation(event.getClickedBlock().getLocation()));

        if (deathChest == null) return;

        event.setCancelled(false);
    }

    @EventHandler
    public void onChestOpen(final InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof Chest chest) {

            final DeathChest deathChest = getModule().getDeathChests().get(RekusLocation.fromBukkitLocation(chest.getLocation()));

            if (deathChest == null) return;
            event.setCancelled(true);
            event.getPlayer().openInventory(deathChest.getInventory());
        }
    }

}
