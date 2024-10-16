package dev.isnow.mcrekus.module.impl.timeshop.menu;

import dev.isnow.mcrekus.data.PlayerData;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.timeshop.TimeShopModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.DateUtil;
import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.Menu;
import io.github.mqzen.menus.base.iterator.Direction;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import io.github.mqzen.menus.misc.itembuilder.ComponentItemBuilder;
import io.github.mqzen.menus.misc.itembuilder.LegacyItemBuilder;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class TimeShopMenu extends ModuleAccessor<TimeShopModule> implements Menu {
    private final PlayerData data;

    @Override
    public String getName() {
        return "TimeShop";
    }

    @Override
    public @NotNull MenuTitle getTitle(DataRegistry dataRegistry, Player player) {
        return MenuTitles.createModern(ComponentUtil.deserialize(getModule().getConfig().getTimeShopMenuTitle()));
    }

    @Override
    public @NotNull Capacity getCapacity(DataRegistry dataRegistry, Player player) {
        return Capacity.ofRows(5);
    }

    @Override
    public @NotNull Content getContent(DataRegistry dataRegistry, Player player,
            Capacity capacity) {

        Content.Builder builder = Content.builder(capacity);

        builder = builder.iterate(Slot.of(0, 0), Direction.RIGHT, ((content, slot) -> content.setButton(slot, Button.clickable(LegacyItemBuilder.legacy(Material.GRAY_STAINED_GLASS_PANE, 1)
                .setDisplay("&7")
                .build(), ButtonClickAction.plain((menuView, inventoryClickEvent) -> inventoryClickEvent.setCancelled(true))))));

        builder = builder.iterate(Slot.of(4, 0), Direction.RIGHT, ((content, slot) -> content.setButton(slot, Button.clickable(LegacyItemBuilder.legacy(Material.GRAY_STAINED_GLASS_PANE, 1)
                .setDisplay("&7")
                .build(), ButtonClickAction.plain((menuView, inventoryClickEvent) -> inventoryClickEvent.setCancelled(true))))));

        builder = builder.iterate(Slot.of(1, 0), Direction.RIGHT, ((content, slot) -> content.setButton(slot, Button.clickable(LegacyItemBuilder.legacy(Material.RED_STAINED_GLASS_PANE, 1)
                .setDisplay("&7")
                .build(), ButtonClickAction.plain((menuView, inventoryClickEvent) -> inventoryClickEvent.setCancelled(true))))));

        builder = builder.iterate(Slot.of(2, 0), Direction.RIGHT, ((content, slot) -> content.setButton(slot, Button.clickable(LegacyItemBuilder.legacy(Material.YELLOW_STAINED_GLASS_PANE, 1)
                .setDisplay("&7")
                .build(), ButtonClickAction.plain((menuView, inventoryClickEvent) -> inventoryClickEvent.setCancelled(true))))));

        builder = builder.iterate(Slot.of(3, 0), Direction.RIGHT, ((content, slot) -> content.setButton(slot, Button.clickable(LegacyItemBuilder.legacy(Material.LIME_STAINED_GLASS_PANE, 1)
                .setDisplay("&7")
                .build(), ButtonClickAction.plain((menuView, inventoryClickEvent) -> inventoryClickEvent.setCancelled(true))))));

        builder = builder.setButton(Slot.of(4, 4), Button.clickable(LegacyItemBuilder.modern(Material.BARRIER, 1)
                .setDisplay(ComponentUtil.deserialize("<gradient:#93291e:#ed213a><bold><tinify>Zamknij Sklep</tinify></gradient>"))
                .setLore(Component.empty(), ComponentUtil.deserialize("<gradient:#8D8D8D:#666666>Kliknij tutaj, aby zamknąć menu!</gradient>"))
                .build(), ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
                    inventoryClickEvent.setCancelled(true);
                    player.closeInventory();
        })));

        data.setTime(data.getTime() + (System.currentTimeMillis() - getModule().getJoinTime().get(player)));

        final long time = data.getTime();

        builder = builder.setButton(Slot.of(0, 4), Button.clickable(LegacyItemBuilder.modern(Material.CLOCK, 1)
                .setDisplay(ComponentUtil.deserialize("<gradient:#FFE259:#FFA751><bold><tinify>Twój Czas</tinify></gradient>"))
                .setLore(Component.empty(),
                        ComponentUtil.deserialize("&8︱ <gradient:#FFE259:#FFA751><bold><tinify>" + DateUtil.formatNewTime(time) + "</tinify></gradient>"),
                        Component.empty(),
                        ComponentUtil.deserialize("<gradient:#8D8D8D:#666666>Tyle czasu spędziłeś na serwerze.</gradient>"),
                        ComponentUtil.deserialize("&#8D8D8DM&#8B8B8Bo&#8A8A8Aż&#888888e&#868686s&#858585z &#818181g&#7F7F7Fo &#7C7C7Cw&#7A7A7Ay&#797979d&#777777a&#757575ć &#727272n&#707070a <gradient:#FF0000:#FF7F00:#FFFF00:#00FF00:#0000FF:#4B0082:#9400D3><bold><tinify>przeróżne klucze</tinify></gradient> &#6D6D6Do&#6B6B6Br&#696969a&#686868z <gradient:#fbf95b:#fdf261><bold><tinify>VIPA</tinify></gradient>"),
                        Component.empty(),
                        ComponentUtil.deserialize("<gradient:#FF0000:#CA0000><bold>UWAGA!</gradient>"),
                        ComponentUtil.deserialize("<gradient:#8D8D8D:#666666>Wydany czas nie wpływa na czas w topkach graczy.</gradient>")
                )
                .build(), ButtonClickAction.plain((menuView, inventoryClickEvent) -> inventoryClickEvent.setCancelled(true))));

        final List<Component> lore = new ArrayList<>();

        addShopInfo(lore, false);
        addPriceInfo(lore, "10 DNI");
        addBuyInfo(lore, time, 864000000, false);

        builder = builder.setButton(Slot.of(1, 4), buildButton(
                LegacyItemBuilder.modern(Material.ENCHANTED_BOOK, 5),
                lore,
                ComponentUtil.deserialize("<gradient:#fbf95b:#fdf261><bold><tinify>VIP NA 5 DNI</tinify></gradient>"),
                ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
                    inventoryClickEvent.setCancelled(true);
                    buyItem(player, time, 864000000, "VIP");
                })
        ));

        addShopInfo(lore, true);
        addPriceInfo(lore, "3 DNI");
        addBuyInfo(lore, time, 259200000, true);

        builder = builder.setButton(Slot.of(2, 3), buildButton(
                LegacyItemBuilder.modern(Material.TRIPWIRE_HOOK, 1),
                lore,
                ComponentUtil.deserialize("<gradient:#BEBEBE:#858585><bold><tinify>Klucz do skrzyni</tinify></gradient> <gradient:#414D0B:#727A17><bold><tinify>MILITARNEJ</tinify></gradient>"),
                ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
                    inventoryClickEvent.setCancelled(true);
                    buyItem(player, time, 259200000, "militarnaskrzynia");
                })
        ));


        addShopInfo(lore, true);
        addPriceInfo(lore, "2 DNI");
        addBuyInfo(lore, time, 172800000, true);

        builder = builder.setButton(Slot.of(2, 5), buildButton(
                LegacyItemBuilder.modern(Material.TRIPWIRE_HOOK, 1),
                lore,
                ComponentUtil.deserialize("<gradient:#BEBEBE:#858585><bold><tinify>Klucz do skrzyni</tinify></gradient> <gradient:#411256:#2d0c3c><bold><tinify>SPAWNEROWEJ</tinify></gradient>"),
                ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
                    inventoryClickEvent.setCancelled(true);
                    buyItem(player, time, 172800000, "spawnerowa");
                })
        ));

        addShopInfo(lore, true);
        addPriceInfo(lore, "1 DZIEŃ");
        addBuyInfo(lore, time, 86400000, true);

        builder = builder.setButton(Slot.of(3, 4), buildButton(
                LegacyItemBuilder.modern(Material.TRIPWIRE_HOOK, 1),
                lore,
                ComponentUtil.deserialize("<gradient:#BEBEBE:#858585><bold><tinify>Klucz do skrzyni</tinify></gradient> <gradient:#53bcbd:#9ed9d9><bold><tinify>DIAMENTOWEJ</tinify></gradient>"),
                ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
                    inventoryClickEvent.setCancelled(true);
                    buyItem(player, time, 86400000, "diamentowaskrzynia");
                })
        ));

        addShopInfo(lore, true);
        addPriceInfo(lore, "6 GODZIN");
        addBuyInfo(lore, time, 21600000, true);

        builder = builder.setButton(Slot.of(3, 2), buildButton(
                LegacyItemBuilder.modern(Material.TRIPWIRE_HOOK, 1),
                lore,
                ComponentUtil.deserialize("<gradient:#BEBEBE:#858585><bold><tinify>Klucz do skrzyni</tinify></gradient> <gradient:#FCFF00:#BFC100><bold><tinify>ZLOTEJ</tinify></gradient>"),
                ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
                    inventoryClickEvent.setCancelled(true);
                    buyItem(player, time, 21600000, "zlotaskrzynia");
                })
        ));

        addShopInfo(lore, true);
        addPriceInfo(lore, "2 GODZINY");
        addBuyInfo(lore, time, 7200000, true);

        builder = builder.setButton(Slot.of(3, 6), buildButton(
                LegacyItemBuilder.modern(Material.TRIPWIRE_HOOK, 1),
                lore,
                ComponentUtil.deserialize("<gradient:#BEBEBE:#858585><bold><tinify>Klucz do skrzyni</tinify></gradient> <gradient:#C1C1C1:#606060><bold><tinify>SREBRNEJ</tinify></gradient>"),
                ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
                    inventoryClickEvent.setCancelled(true);
                    buyItem(player, time, 7200000, "srebrnaskrzynia");
                })
        ));

        return builder.build();

    }

    private Button buildButton(final ComponentItemBuilder item, final List<Component> lore, final Component display, ButtonClickAction action) {
        final Button button = Button.clickable(item
                .setDisplay(display)
                .setLore(lore)
                .build(), action);

        lore.clear();

        return button;
    }

    private void addShopInfo(final List<Component> lore, final boolean key) {
        lore.add(Component.empty());
        lore.add(ComponentUtil.deserialize("<gradient:#8D8D8D:#666666>Informacje o " + (key ? "kluczu" : "randze") + " znajdują się</gradient>"));
        lore.add(ComponentUtil.deserialize("<gradient:#8D8D8D:#666666>na stronie sklepu (</gradient><gradient:#15FB08:#1BCB00><tinify>sklep.mcrekus.eu</tinify></gradient>&#666666)"));
        lore.add(Component.empty());
    }

    private void addPriceInfo(final List<Component> lore, final String price) {
        lore.add(ComponentUtil.deserialize("<gradient:#8D8D8D:#666666>Koszt:</gradient> <gradient:#FFE259:#FFA751><bold><tinify>" + price + "</tinify></gradient> <gradient:#8D8D8D:#666666>spędzonego czasu</gradient>"));
        lore.add(Component.empty());
    }

    private void addBuyInfo(final List<Component> lore, final long time, final long requiredTime, final boolean key) {
        if(time >= requiredTime) {
            lore.add(ComponentUtil.deserialize("&a⊳ <tinify>Kliknij aby zakupić " + (key ? "ten klucz" : "tą range") + "!</tinify>"));
        } else {
            lore.add(ComponentUtil.deserialize("&c⊳ <gradient:#FF0000:#CA0000><tinify>Nie posiadasz wystarczająco czasu aby zakupić " + (key ? "ten klucz" : "tą range") + "</tinify>"));
        }
    }

    private void buyItem(final Player player, final long time, final long requiredTime, final String item) {
        if(time < requiredTime) {
            return;
        }

        if (item.equals("VIP")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "lp user " + player.getName() + " parent addtemp vip 5d");
            player.sendMessage(ComponentUtil.deserialize(
                    "[P] &aZakupiłeś rangę <gradient:#fbf95b:#fdf261><bold>VIP</gradient> &ana 5 dni!"));
        } else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "excellentcrates key give " + player.getName() + " " + item + " 1");
//            player.sendMessage(ComponentUtil.deserialize("[P] &aZakupiłeś klucz do skrzyni!"));
        }

        data.setTime(time - requiredTime);
        data.save();
        player.closeInventory();
    }
}
