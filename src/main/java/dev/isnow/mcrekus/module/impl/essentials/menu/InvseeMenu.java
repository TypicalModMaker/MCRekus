package dev.isnow.mcrekus.module.impl.essentials.menu;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.Menu;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import io.github.mqzen.menus.misc.itembuilder.LegacyItemBuilder;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InvseeMenu extends ModuleAccessor<EssentialsModule> implements Menu {

    private Player player;
    private Player target;

    @Override
    public String getName() {
        return "invsee";
    }

    @Override
    public @NotNull MenuTitle getTitle(DataRegistry dataRegistry, Player player) {
        return MenuTitles.createModern(ComponentUtil.deserialize(getModule().getConfig().getBinGuiName()));
    }

    @Override
    public @NotNull Capacity getCapacity(DataRegistry dataRegistry, Player player) {
        return Capacity.ofRows(6);
    }

    @Override
    public @NotNull Content getContent(DataRegistry dataRegistry, Player player,
            Capacity capacity) {

        Content.Builder builder = Content.builder(capacity);

        builder = builder.repeatButton(Slot.of(0, 0), Slot.of(0, 8), Button.clickable(
                        LegacyItemBuilder.legacy(Material.GRAY_STAINED_GLASS_PANE, 1)
                                .setDisplay("&7")
                                .build(), ButtonClickAction.plain((menuView, inventoryClickEvent) -> inventoryClickEvent.setCancelled(true))));

        return builder.build();
    }
}
