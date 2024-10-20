package dev.isnow.mcrekus.module.impl.essentials.menu;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.Menu;
import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import io.github.mqzen.menus.misc.itembuilder.LegacyItemBuilder;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class InvseeMenu extends ModuleAccessor<EssentialsModule> implements Menu {

    private final Player player;
    private final Player target;
    private BukkitTask updateTask;

    @Override
    public String getName() {
        return "invsee";
    }

    @Override
    public @NotNull MenuTitle getTitle(final DataRegistry dataRegistry, final Player player) {
        return MenuTitles.createModern(ComponentUtil.deserialize(getModule().getConfig().getInvseeGuiName(), null, "%player%", target.getName()));
    }

    @Override
    public @NotNull Capacity getCapacity(final DataRegistry dataRegistry, final Player player) {
        return Capacity.ofRows(5);
    }

    @Override
    public @NotNull Content getContent(final DataRegistry dataRegistry, final Player player,
            final Capacity capacity) {

        final Content.Builder builder = Content.builder(capacity);

        for(int j = 0; j < 5 * 9; j++) {
            final Button button = Button.empty(target.getInventory().getItem(j));

            builder.setButton(j, button);
        }

        final ItemStack healthPot = LegacyItemBuilder.modern(Material.POTION).addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ITEM_SPECIFICS).setDisplay(ComponentUtil.deserialize("&c<tinify>Health</tinify>")).setLore(ComponentUtil.deserialize(
                "&c" + target.getHealth())).build();

        final PotionType potionType = PotionType.INSTANT_HEAL;
        final PotionMeta potionMeta = (PotionMeta) healthPot.getItemMeta();
        potionMeta.setBasePotionType(potionType);
        healthPot.setItemMeta(potionMeta);

        final Button healthButton = Button.clickable(healthPot, ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
            inventoryClickEvent.setCancelled(true);
        }));

        builder.setButton(Slot.of(43), healthButton);

        final ItemStack foodSteak = LegacyItemBuilder.modern(Material.COOKED_BEEF).setDisplay(ComponentUtil.deserialize("&c<tinify>Food Level</tinify>")).setLore(ComponentUtil.deserialize(
                "&c" + target.getFoodLevel())).build();

        final Button foodButton = Button.clickable(foodSteak, ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
            inventoryClickEvent.setCancelled(true);
        }));

        builder.setButton(Slot.of(44), foodButton);

        final ItemStack xp = LegacyItemBuilder.modern(Material.EXPERIENCE_BOTTLE).setDisplay(ComponentUtil.deserialize("&c<tinify>Experience</tinify>")).setLore(ComponentUtil.deserialize(
                "&c" + target.getLevel())).build();

        final Button xpButton = Button.clickable(xp, ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
            inventoryClickEvent.setCancelled(true);
        }));

        builder.setButton(Slot.of(42), xpButton);

        return builder.build();
    }

    @Override
    public void onDrag(MenuView<?> playerMenuView, InventoryDragEvent event) {
        if(!event.getInventory().equals(playerMenuView.getInventory())) return;

        for(final int slot : event.getRawSlots()) {
            if(slot >= 42) {
                continue;
            }

            final ItemStack item = event.getNewItems().get(slot);
            target.getInventory().setItem(slot, item);
        }
    }

    @Override
    public void onPostClick(MenuView<?> playerMenuView, InventoryClickEvent event) {
        if(event.getClickedInventory() == null) return;

        final boolean isPlayerInventory = event.getClickedInventory().equals(playerMenuView.getInventory());

        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            if (!isPlayerInventory) {
                target.getInventory().addItem(event.getCurrentItem());
            } else {
                if(event.getSlot() >= 35 || event.getSlot() <= 40) {
                    target.getInventory().setItem(event.getSlot(), null);
                    return;
                }
                target.getInventory().removeItem(event.getCurrentItem());
            }
            return;
        }

        if(!isPlayerInventory) return;

        if (event.getAction() == InventoryAction.CLONE_STACK) {
            return;
        }

        if (event.getAction() == InventoryAction.PICKUP_HALF) {
            final ItemStack item = target.getInventory().getItem(event.getSlot());
            if (item == null) {
                return;
            }
            item.setAmount(item.getAmount() / 2);

            return;
        }

        if (event.getAction() == InventoryAction.PLACE_ONE) {
            final ItemStack item = target.getInventory().getItem(event.getSlot());
            if (event.getCurrentItem() == null || item == null) {
                final ItemStack stack = event.getCursor().clone();
                stack.setAmount(1);
                target.getInventory().setItem(event.getSlot(), stack);
                return;
            }
            item.setAmount(item.getAmount() + 1);

            return;
        }

        if (event.getAction() == InventoryAction.PLACE_ALL && event.getCurrentItem() != null) {
            final ItemStack item = target.getInventory().getItem(event.getSlot());
            if (item == null) {
                return;
            }
            item.setAmount(item.getAmount() + event.getCursor().getAmount());

            return;
        }

        if (event.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
            final ItemStack item = event.getCursor();
            int stack = item.getAmount();
            final ArrayList<Integer> affectedSlots = new ArrayList<>();
            for (int i = 0; i < target.getInventory().getSize(); i++) {
                final ItemStack slotItem = target.getInventory().getItem(i);
                if (slotItem == null || !slotItem.isSimilar(item) || stack >= item.getMaxStackSize()) {
                    continue;
                }
                affectedSlots.add(i);
                stack += slotItem.getAmount();
            }

            for(final int slot : affectedSlots) {
                target.getInventory().setItem(slot, null);
            }

            return;
        }

        RekusLogger.debug(event.getAction().name());

        target.getInventory().setItem(event.getSlot(), event.getCursor());
    }


    @Override
    public void onClose(final MenuView<?> playerMenuView, final InventoryCloseEvent event) {
        getModule().getInvseeMenus().remove(target);

        updateTask.cancel();
    }



    public void doRunnable() {
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                final MenuView<?> view = MCRekus.getInstance().getMenuAPI().getMenuView(player.getUniqueId()).orElse(null);

                if (view == null) {
                    return;
                }
                for(int j = 0; j < 42; j++) {
                    update(view, j, target.getInventory().getItem(j));
                }

                final ItemStack healthPot = LegacyItemBuilder.modern(Material.POTION).addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ITEM_SPECIFICS).setDisplay(ComponentUtil.deserialize("&c<tinify>Health</tinify>")).setLore(ComponentUtil.deserialize(
                        "&c" + target.getHealth())).build();

                final PotionType potionType = PotionType.INSTANT_HEAL;
                final PotionMeta potionMeta = (PotionMeta) healthPot.getItemMeta();
                potionMeta.setBasePotionType(potionType);
                healthPot.setItemMeta(potionMeta);

                final Button healthButton = Button.clickable(healthPot, ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
                    inventoryClickEvent.setCancelled(true);
                }));

                view.replaceButton(Slot.of(43), healthButton);

                final ItemStack foodSteak = LegacyItemBuilder.modern(Material.COOKED_BEEF).setDisplay(ComponentUtil.deserialize("&c<tinify>Food Level</tinify>")).setLore(ComponentUtil.deserialize(
                        "&c" + target.getFoodLevel())).build();

                final Button foodButton = Button.clickable(foodSteak, ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
                    inventoryClickEvent.setCancelled(true);
                }));

                view.replaceButton(Slot.of(44), foodButton);

                final ItemStack xp = LegacyItemBuilder.modern(Material.EXPERIENCE_BOTTLE).setDisplay(ComponentUtil.deserialize("&c<tinify>Experience</tinify>")).setLore(ComponentUtil.deserialize(
                        "&c" + target.getLevel())).build();

                final Button xpButton = Button.clickable(xp, ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
                    inventoryClickEvent.setCancelled(true);
                }));

                view.replaceButton(Slot.of(42), xpButton);

            }
        }.runTaskTimerAsynchronously(MCRekus.getInstance(), 0, 1);
    }

    public void update(final MenuView<?> view, final int slotNumber, final ItemStack item) {
        final Slot slot = Slot.of(slotNumber);

        final Button button = Button.empty(item);

        view.replaceButton(slot, button);
    }
}
