package dev.isnow.mcrekus.module.impl.customevents.event.impl.killplayers;

import dev.isnow.mcrekus.module.impl.customevents.event.CustomEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.impl.killplayers.event.PlayerBukkitEvent;

public class PlayerKillEvent extends CustomEvent {

    public PlayerKillEvent() {
        super("PlayerKill" , "Zabijanie Graczy", "Zabij jak najwięcej graczy!", PlayerBukkitEvent.class);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
