package dev.isnow.mcrekus.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.data.PlayerData;
import dev.isnow.mcrekus.data.PlayerDataManager;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.util.RekusLogger;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class LoginEvent implements Listener {

    @EventHandler
    public void onLogin(final PlayerLoginEvent event) {
        final PlayerDataManager playerDataManager = MCRekus.getInstance().getPlayerDataManager();

        final Player player = event.getPlayer();

        MCRekus.getInstance().getThreadPool().execute(() -> {
            playerDataManager.preloadPlayerData(player.getUniqueId());

            if (!MCRekus.getInstance().getPlayerDataManager().userExists(player.getUniqueId())) {
                RekusLogger.debug("Adding new user: " + player.getName());

                MCRekus.getInstance().getPlayerDataManager().addUser(new PlayerData(player));
            } else {
                MCRekus.getInstance().getPlayerDataManager().getPlayerData(player.getUniqueId()).setName(player.getName());
            }
        });
    }
}
