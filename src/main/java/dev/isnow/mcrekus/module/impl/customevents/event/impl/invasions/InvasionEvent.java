package dev.isnow.mcrekus.module.impl.customevents.event.impl.invasions;

import dev.isnow.mcrekus.module.impl.customevents.event.CustomEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.impl.coinflip.event.CoinFlipBukkitEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.impl.invasions.event.InvadeBukkitEvent;

public class InvasionEvent extends CustomEvent {

    public InvasionEvent() {
        super("Invasion", "Podbijanie Chunków", "Podbij jak najwięcej chunków!", InvadeBukkitEvent.class);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
