package dev.isnow.mcrekus.module.impl.essentials.event;

import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import dev.isnow.mcrekus.event.custom.PlayerDropItemSlotEvent;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.menu.InvseeMenu;
import dev.isnow.mcrekus.util.RekusLogger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryEvent extends ModuleAccessor<EssentialsModule> implements Listener {

//    @EventHandler
//    public void onInventoryClick(final InventoryClickEvent event) {
//        if(!(event.getWhoClicked() instanceof Player player)) return;
//
//        if (event.getClickedInventory() == null || !event.getClickedInventory().equals(player.getInventory())) return;
//
//        final InvseeMenu menu = getModule().getInvseeMenus().get(player);
//
//        if(menu == null) return;
//
//        menu.update(event.getSlot(), event.getCursor());
//    }
//
//    @EventHandler
//    public void onInventoryDrag(final InventoryDragEvent event) {
//        if(!(event.getWhoClicked() instanceof Player player)) return;
//
//        final InvseeMenu menu = getModule().getInvseeMenus().get(player);
//
//        if(menu == null) return;
//        for(final int slot : event.getRawSlots()) {
//            menu.update(slot, event.getNewItems().get(slot));
//        }
//    }
//
//    @EventHandler
//    public void onItemPickup(final EntityPickupItemEvent event) {
//        if(!(event.getEntity() instanceof Player player)) return;
//
//        final InvseeMenu menu = getModule().getInvseeMenus().get(player);
//
//        if(menu == null) return;
//
//        final int first = player.getInventory().first(event.getItem().getItemStack().getType());
//
//        if (first == -1) {
//            menu.update(player.getInventory().firstEmpty(), event.getItem().getItemStack());
//        } else {
//            menu.update(first, player.getInventory().getItem(first).clone().add(event.getItem().getItemStack().getAmount()));
//        }
//
//    }
//
//    @EventHandler
//    public void onItemDrop(final PlayerDropItemSlotEvent event) {
//        final Player player = event.getPlayer();
//
//        if (event.getInventory() == null || !event.getInventory().equals(player.getInventory())) return;
//
//        final InvseeMenu menu = getModule().getInvseeMenus().get(player);
//
//        if(menu == null) return;
//
//        if(event.getAction() == DiggingAction.DROP_ITEM_STACK) {
//            menu.update(event.getSlot(), null);
//        } else if (event.getAction() == DiggingAction.DROP_ITEM) {
//            menu.update(event.getSlot(), event.getItemStack().clone().subtract(1));
//        }
//    }
}
