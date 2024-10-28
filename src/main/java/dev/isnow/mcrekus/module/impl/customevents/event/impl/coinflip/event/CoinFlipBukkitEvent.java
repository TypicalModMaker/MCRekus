package dev.isnow.mcrekus.module.impl.customevents.event.impl.coinflip.event;

import dev.isnow.mcrekus.module.impl.customevents.event.BukkitEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.CustomEvent;
import net.zithium.deluxecoinflip.api.events.CoinflipCompletedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CoinFlipBukkitEvent extends BukkitEvent implements Listener{

    public CoinFlipBukkitEvent(final CustomEvent customEvent) {
        super(customEvent);
    }

    @EventHandler
    public void onCoinFlip(final CoinflipCompletedEvent event) {
        if(!event.getWinner().isOnline()) return;

        getCustomEvent().addScore(event.getWinner().getPlayer());
    }
}
