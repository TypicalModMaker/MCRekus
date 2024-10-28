package dev.isnow.mcrekus.module.impl.customevents.event.impl.travel.event;

import dev.isnow.mcrekus.module.impl.customevents.event.BukkitEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.CustomEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveBukkitEvent extends BukkitEvent implements Listener {

    public MoveBukkitEvent(final CustomEvent customEvent) {
        super(customEvent);
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            getCustomEvent().addScore(event.getPlayer());
        }
    }
}
