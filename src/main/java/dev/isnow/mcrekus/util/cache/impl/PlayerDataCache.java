package dev.isnow.mcrekus.util.cache.impl;

import dev.isnow.mcrekus.data.PlayerData;
import dev.isnow.mcrekus.data.PlayerDataManager;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.essentials.data.EssentialsPlayerData;
import dev.isnow.mcrekus.util.cache.RekusCache;
import java.util.UUID;
import lombok.Getter;

@Getter
public class PlayerDataCache extends RekusCache<UUID, PlayerData> {
    private final Class<? extends PlayerData> type;

    public PlayerDataCache(final String module, final PlayerDataManager playerDataManager, final Class<? extends PlayerData> type) {
        super("User",
                uuid -> playerDataManager.loadUser(module, uuid),
                playerDataManager::saveData,
                null);
        this.type = type;
    }
}
