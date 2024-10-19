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
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
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
            final Button button = Button.clickable(target.getInventory().getItem(j), ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
                RekusLogger.debug("Clicked");
                target.getInventory().setItem(inventoryClickEvent.getSlot(), inventoryClickEvent.getCursor());
            }));

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

        final Button button = Button.clickable(item, ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
            target.getInventory().setItem(inventoryClickEvent.getSlot(), inventoryClickEvent.getCursor());
        }));

        view.replaceButton(slot, button);
    }
}
