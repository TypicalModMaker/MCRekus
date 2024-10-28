package dev.isnow.mcrekus.module.impl.customevents.event.impl.pearls.event;

import dev.isnow.mcrekus.module.impl.customevents.event.BukkitEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.CustomEvent;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ThrowBukkitEvent extends BukkitEvent implements Listener {

    public ThrowBukkitEvent(final CustomEvent customEvent) {
        super(customEvent);
    }

    @EventHandler
    public void onEnderPearl(final ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() == null) return;

        if (!(event.getEntity() instanceof EnderPearl)) return;

        if (!(event.getEntity().getShooter() instanceof Player player)) return;

        getCustomEvent().addScore(player);
    }
}
