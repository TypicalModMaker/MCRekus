package dev.isnow.mcrekus.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.data.PlayerData;
import dev.isnow.mcrekus.database.DatabaseManager;
import dev.isnow.mcrekus.util.RekusLogger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class LoginEvent implements Listener {

    @EventHandler
    public void onLogin(final PlayerLoginEvent event) {
        final DatabaseManager databaseManager = MCRekus.getInstance().getDatabaseManager();

        final Player player = event.getPlayer();

        // Preload to L2 Cache
        databaseManager.getUserAsync(player, (session, playerData) -> {
            if (playerData == null) {
                RekusLogger.debug("Player data not found for " + player.getName() + ", creating new entry.");
                databaseManager.saveUser(new PlayerData(player), databaseManager.openSession());
            } else {
                RekusLogger.debug("Player data found for " + player.getName() + ", loading from cache.");
                playerData.setName(player.getName());

                databaseManager.saveUser(playerData, session);
            }
        });
    }
}
