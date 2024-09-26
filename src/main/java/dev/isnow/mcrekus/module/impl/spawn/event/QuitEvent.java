package dev.isnow.mcrekus.module.impl.spawn.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawn.SpawnModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent extends ModuleAccessor<SpawnModule> implements Listener {

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        getModule().getSpawnTeleportManager().getCooldown().removeCooldown(event.getPlayer().getUniqueId());
    }

}
