package dev.isnow.mcrekus.module.impl.customevents.event.impl.killplayers.event;

import dev.isnow.mcrekus.module.impl.customevents.event.BukkitEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.CustomEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class PlayerBukkitEvent extends BukkitEvent implements Listener {

    public PlayerBukkitEvent(final CustomEvent customEvent) {
        super(customEvent);
    }

    @EventHandler
    public void onMobKill(final EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;

        getCustomEvent().addScore(event.getEntity().getKiller());
    }
}
