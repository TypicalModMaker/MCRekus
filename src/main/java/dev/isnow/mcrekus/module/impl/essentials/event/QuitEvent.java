package dev.isnow.mcrekus.module.impl.essentials.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        event.quitMessage(null);
    }

}
