package dev.isnow.mcrekus.module.impl.customevents.event.impl.pumpkin.event;

import dev.isnow.mcrekus.module.impl.customevents.event.BukkitEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.CustomEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PumpkinBreakBukkitEvent extends BukkitEvent implements Listener {

    public PumpkinBreakBukkitEvent(final CustomEvent customEvent) {
        super(customEvent);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(final BlockBreakEvent event) {
        if (event.isCancelled()) return;

        if (event.getBlock().getType().name().contains("PUMPKIN")) {
            getCustomEvent().addScore(event.getPlayer());
        }
    }
}
