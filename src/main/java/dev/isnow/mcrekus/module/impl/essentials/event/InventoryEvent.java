package dev.isnow.mcrekus.module.impl.essentials.event;

import dev.isnow.mcrekus.event.custom.PlayerDropItemSlotEvent;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.menu.InvseeMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryEvent extends ModuleAccessor<EssentialsModule> implements Listener {

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) return;

        if (event.getClickedInventory() == null || !event.getClickedInventory().equals(player.getInventory())) return;

        final InvseeMenu menu = getModule().getInvseeMenus().get(player);

        if(menu == null) return;

        menu.update(event.getSlot(), event.getCursor());
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) return;

        final InvseeMenu menu = getModule().getInvseeMenus().get(player);

        if(menu == null) return;
        for(final int slot : event.getRawSlots()) {
            menu.update(slot, event.getNewItems().get(slot));
        }
    }

    @EventHandler
    public void onItemPickup(final EntityPickupItemEvent event) {
        if(!(event.getEntity() instanceof Player player)) return;

        final InvseeMenu menu = getModule().getInvseeMenus().get(player);

        if(menu == null) return;

        menu.update(player.getInventory().firstEmpty(), event.getItem().getItemStack());
    }

    @EventHandler
    public void onItemDrop(final PlayerDropItemSlotEvent event) {
        final Player player = event.getPlayer();

        final InvseeMenu menu = getModule().getInvseeMenus().get(player);

        if(menu == null) return;

        menu.update(event.getSlot(), event.getItemStack());
    }
}
