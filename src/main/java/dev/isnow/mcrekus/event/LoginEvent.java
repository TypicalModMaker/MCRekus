package dev.isnow.mcrekus.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.data.PlayerDataManager;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.data.EssentialsPlayerData;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class LoginEvent implements Listener {

    @EventHandler
    public void onLogin(final AsyncPlayerPreLoginEvent event) {
        final PlayerDataManager playerDataManager = MCRekus.getInstance().getPlayerDataManager();

        MCRekus.getInstance().getThreadPool().execute(() -> {
            playerDataManager.preloadPlayerData(event);
        });
    }
}
