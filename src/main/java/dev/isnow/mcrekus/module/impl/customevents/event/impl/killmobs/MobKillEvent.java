package dev.isnow.mcrekus.module.impl.customevents.event.impl.killmobs;

import dev.isnow.mcrekus.module.impl.customevents.event.CustomEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.impl.killmobs.event.MobBukkitEvent;

public class MobKillEvent extends CustomEvent {

    public MobKillEvent() {
        super("MobKill", "Zabijanie Mobów", "Zabij jak najwięcej mobów!", MobBukkitEvent.class);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
