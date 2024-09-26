package dev.isnow.mcrekus.module.impl.essentials.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent extends ModuleAccessor<EssentialsModule> implements Listener {

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        event.deathMessage(null);
    }
}
