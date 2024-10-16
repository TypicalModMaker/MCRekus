package dev.isnow.mcrekus.module.impl.timeshop.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.timeshop.TimeShopModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent extends ModuleAccessor<TimeShopModule> implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        getModule().getJoinTime().put(event.getPlayer(), System.currentTimeMillis());
    }
}
