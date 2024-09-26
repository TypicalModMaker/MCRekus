package dev.isnow.mcrekus.module.impl.essentials.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        event.joinMessage(null);
    }

}
