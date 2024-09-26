package dev.isnow.mcrekus.data;

import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.cache.impl.PlayerDataCache;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDataManager {
    private PlayerDataCache playerDataCache = new PlayerDataCache();

    public PlayerData getPlayerData(final UUID uuid) {
        return playerDataCache.get(uuid);
    }

    public void preloadPlayerData(final UUID uuid) {
        RekusLogger.debug("Preloading player data for " + uuid);
        playerDataCache.preload(uuid);
    }

    public List<PlayerData> getAllPlayerData() {
        return playerDataCache.getAll().stream().toList();
    }

    public boolean userExists(final UUID uuid) {
        return playerDataCache.contains(uuid);
    }

    public void addUser(final PlayerData playerData) {
        playerDataCache.put(playerData.getUuid(), playerData);
    }
}
