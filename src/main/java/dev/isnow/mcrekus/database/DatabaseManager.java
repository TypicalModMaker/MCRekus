package dev.isnow.mcrekus.database;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.config.ConfigManager;
import dev.isnow.mcrekus.config.impl.GeneralConfig;
import dev.isnow.mcrekus.config.impl.database.DatabaseConfig;
import dev.isnow.mcrekus.data.PlayerData;
import dev.isnow.mcrekus.data.PumpkinData;
import dev.isnow.mcrekus.util.ExpiringSession;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import java.util.function.BiConsumer;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Getter
public final class DatabaseManager {
    private final Database database;

    public DatabaseManager() {
        final ConfigManager configManager = MCRekus.getInstance().getConfigManager();
        final GeneralConfig mainConfig = configManager.getGeneralConfig();
        final DatabaseConfig authConfig = configManager.getDatabaseConfig();

        this.database = new Database(mainConfig, authConfig);
    }

    public void getUserAsync(final OfflinePlayer player, final BiConsumer<ExpiringSession, PlayerData> callback) {
        database.fetchEntityAsync("FROM PlayerData WHERE uuid = :uuid", "uuid", player.getUniqueId(), PlayerData.class, callback);
    }

    public void getUserAsync(final String playerName, final BiConsumer<ExpiringSession, PlayerData> callback) {
        database.fetchEntityAsync("FROM PlayerData WHERE name = :player_name", "player_name", playerName, PlayerData.class, callback);
    }

    public void getPumpkinAsync(final RekusLocation pumpkinLocation, final BiConsumer<ExpiringSession, PumpkinData> callback) {
        database.fetchEntityAsync("FROM PumpkinData WHERE location = :location", "location", pumpkinLocation, PumpkinData.class, callback);
    }

    public void preloadPlayer(final Player player) {
        getUserAsync(player, (session, foundPlayerData) -> {
            if (foundPlayerData == null) {
                RekusLogger.debug("Player data not found for " + player.getName() + ", creating new entry.");

                final PlayerData playerData = new PlayerData(player);
                playerData.save(session);
            } else {
                RekusLogger.debug("Player data found for " + player.getName() + ", loading from cache.");

                foundPlayerData.setName(player.getName());
                foundPlayerData.save(session);
            }
        });
    }

}
