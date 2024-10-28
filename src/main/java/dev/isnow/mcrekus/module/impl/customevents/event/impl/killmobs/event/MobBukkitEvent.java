package dev.isnow.mcrekus.module.impl.customevents.event.impl.killmobs.event;

import dev.isnow.mcrekus.module.impl.customevents.event.BukkitEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.CustomEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobBukkitEvent extends BukkitEvent implements Listener {

    public MobBukkitEvent(final CustomEvent customEvent) { super(customEvent); }

    @EventHandler
    public void onMobKill(final EntityDeathEvent event) {
        if (event.getEntity() instanceof Player || event.getEntity().getKiller() == null) return;

        getCustomEvent().addScore(event.getEntity().getKiller());
    }
}
