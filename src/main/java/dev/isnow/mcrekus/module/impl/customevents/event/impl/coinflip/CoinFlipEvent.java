package dev.isnow.mcrekus.module.impl.customevents.event.impl.coinflip;

import dev.isnow.mcrekus.module.impl.customevents.event.CustomEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.impl.coinflip.event.CoinFlipBukkitEvent;

public class CoinFlipEvent extends CustomEvent {

    public CoinFlipEvent() {
        super("CoinFlip", "Rzuty Monetą", "Wygraj jak najwięcej rzutów monetą!", CoinFlipBukkitEvent.class);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
