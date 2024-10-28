package dev.isnow.mcrekus.module.impl.customevents.event.impl.pearls;

import dev.isnow.mcrekus.module.impl.customevents.event.CustomEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.impl.pearls.event.ThrowBukkitEvent;

public class EnderPearlEvent extends CustomEvent {

    public EnderPearlEvent() {
        super("EnderPearl", "Ender Perły", "Rzuć jak najwięcej ender pereł!", ThrowBukkitEvent.class);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
