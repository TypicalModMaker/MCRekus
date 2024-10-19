package dev.isnow.mcrekus.event.custom;

import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerDropItemSlotEvent extends Event implements Cancellable {

    private final Player player;
    private final ItemStack item;
    private final Inventory inventory;
    private final DiggingAction action;
    private final int slot;

    private boolean cancelled = false;

    private static final HandlerList handlers = new HandlerList();

    /**
     * Similar to PlayerDropItemEvent, but includes the slot of the item dropped.
     * If the item dropped was from the player's cursor with their inventory opened,
     * getSlot() will return -1 and isCursor() will return true;
     *
     * @param player The player dropping the item
     * @param item The ItemStack being dropped
     * @param inventory The inventory that the item was dropped from
     * @param action The action performed, either DROP_ITEM or DROP_ITEM_STACK
     * @param slot The slot of the item dropped, -1 if cursor
     */
    public PlayerDropItemSlotEvent(final Player player, final ItemStack item, final Inventory inventory, final DiggingAction action, final int slot) {
        this.player = player;
        this.item = item;
        this.inventory = inventory;
        this.action = action;
        this.slot = slot;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Get the player that dropped this item
     * @return Player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the ItemStack dropped by the player.
     * @return ItemStack
     */
    public ItemStack getItemStack() {
        return this.item;
    }

    /**
     * Get the inventory that this item was dropped from.
     * Is usually the player's inventory, unless the player is dropping the item directly from a chest.
     * If the player drops the item from their cursor this will be null.
     * @return Inventory
     */
    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Get the slot of the item dropped.
     * This returns -1 if the item was dropped from the cursor in the player's inventory.
     * @return Slot
     */
    public int getSlot() {
        return this.slot;
    }

    /**
     * Get the action performed by the player.
     * This will be either DROP_ITEM or DROP_ITEM_STACK.
     * @return DiggingAction
     */
    public DiggingAction getAction() {
        return this.action;
    }
    /**
     * Whether or not this item dropped is from the player's cursor.
     * @return Is cursor
     */
    public boolean isCursor() {
        return this.slot == -1;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean canceled) {
        this.cancelled = true;
    }

}