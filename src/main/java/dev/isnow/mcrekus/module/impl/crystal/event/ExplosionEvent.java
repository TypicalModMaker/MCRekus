package dev.isnow.mcrekus.module.impl.crystal.event;

import org.bukkit.entity.EnderCrystal;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class ExplosionEvent implements Listener {

    @EventHandler
    public void onExplosion(final ExplosionPrimeEvent event) {
        if(!(event.getEntity() instanceof EnderCrystal)) return;

        event.setFire(false);
        event.setRadius(0.0f);
    }

}
