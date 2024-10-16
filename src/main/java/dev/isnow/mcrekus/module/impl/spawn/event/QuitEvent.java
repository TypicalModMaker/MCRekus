package dev.isnow.mcrekus.module.impl.spawn.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawn.SpawnModule;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent extends ModuleAccessor<SpawnModule> implements Listener {

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        getModule().getSpawnTeleportManager().getCooldown().removeCooldown(event.getPlayer().getUniqueId());


        MCRekus.getInstance().getDatabaseManager().getUserAsync(event.getPlayer(), (session, data) -> {
            data.setLastLocation(RekusLocation.fromBukkitLocation(event.getPlayer().getLocation()));

            data.save(session);
        });
    }

}
