package dev.isnow.mcrekus.module.impl.customevents.event.impl.travel;

import dev.isnow.mcrekus.module.impl.customevents.event.CustomEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.impl.travel.event.MoveBukkitEvent;

public class TravelledDistanceEvent extends CustomEvent {

    public TravelledDistanceEvent() {
        super("Travel", "Przebyty dystans", "Przebądź jak najdłuższy dystans!", MoveBukkitEvent.class);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
