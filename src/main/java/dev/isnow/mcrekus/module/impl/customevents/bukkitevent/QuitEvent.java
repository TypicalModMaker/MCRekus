package dev.isnow.mcrekus.module.impl.customevents.bukkitevent;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.customevents.CustomEventsModule;
import dev.isnow.mcrekus.module.impl.customevents.config.CustomEventsConfig;
import dev.isnow.mcrekus.module.impl.customevents.event.CustomEventManager;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent extends ModuleAccessor<CustomEventsModule> implements Listener {

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final CustomEventManager customEventManager = getModule().getCustomEventManager();

        if (customEventManager.getCurrentEvent() == null) return;

        final CustomEventsConfig config = getModule().getConfig();

        if (!MCRekus.getInstance().getConfigManager().getGeneralConfig().isDebugMode() && Bukkit.getOnlinePlayers().size() >= config.getMinPlayers()) return;

        customEventManager.stopEvent();
        Bukkit.broadcast(ComponentUtil.deserialize(config.getEventStoppedNotEnoughPlayersMessage()));

    }
}
