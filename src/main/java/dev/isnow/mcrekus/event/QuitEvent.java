package dev.isnow.mcrekus.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.hibernate.Transaction;

public class QuitEvent implements Listener {

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        MCRekus.getInstance().getDatabaseManager().getUserAsync(event.getPlayer(), (session, data) -> {
            data.setLastLocation(RekusLocation.fromBukkitLocation(event.getPlayer().getLocation()));

            MCRekus.getInstance().getDatabaseManager().saveUser(data, session);
        });
    }

}
