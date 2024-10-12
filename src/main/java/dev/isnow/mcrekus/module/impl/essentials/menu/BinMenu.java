package dev.isnow.mcrekus.module.impl.essentials.menu;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.Menu;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BinMenu extends ModuleAccessor<EssentialsModule> implements Menu {

    @Override
    public String getName() {
        return "bin";
    }

    @Override
    public @NotNull MenuTitle getTitle(DataRegistry dataRegistry, Player player) {
        return MenuTitles.createModern(ComponentUtil.deserialize(getModule().getConfig().getBinGuiName()));
    }

    @Override
    public @NotNull Capacity getCapacity(DataRegistry dataRegistry, Player player) {
        return Capacity.ofRows(5);
    }

    @Override
    public @NotNull Content getContent(DataRegistry dataRegistry, Player player,
            Capacity capacity) {
        return Content.builder(capacity).build();
    }
}
