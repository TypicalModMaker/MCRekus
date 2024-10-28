package dev.isnow.mcrekus.module.impl.customevents.event.impl.pumpkin;

import dev.isnow.mcrekus.module.impl.customevents.event.CustomEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.impl.pearls.event.ThrowBukkitEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.impl.pumpkin.event.PumpkinBreakBukkitEvent;

public class PumpkinEvent extends CustomEvent {

    public PumpkinEvent() {
        super("Pumpkin", "Niszczenie dyń", "Zniszcz jak najwięcej dyń!", PumpkinBreakBukkitEvent.class);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
