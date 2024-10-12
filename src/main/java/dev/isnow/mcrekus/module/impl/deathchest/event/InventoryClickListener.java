package dev.isnow.mcrekus.module.impl.deathchest.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.deathchest.DeathChest;
import dev.isnow.mcrekus.module.impl.deathchest.DeathChestModule;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener extends ModuleAccessor<DeathChestModule> implements Listener {

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }

        if (!(event.getClickedInventory().getHolder() instanceof Chest chest)) {
            return;
        }

        final DeathChest deathChest = getModule().getDeathChests().get(RekusLocation.fromBukkitLocation(chest.getLocation()));

        if (deathChest == null) {
            RekusLogger.info("DeathChest is null");
            return;
        }

        final long count = Arrays.stream(event.getClickedInventory().getContents()).filter(itemStack -> itemStack != null && itemStack.getType() != Material.AIR).count();

        RekusLogger.info(event.getAction().toString());

        if ((event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || event.getAction() == InventoryAction.PICKUP_ALL) && count == 1) {
            deathChest.remove();
        }

    }

}
