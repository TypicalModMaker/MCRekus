package dev.isnow.mcrekus.util.cache.impl;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.data.PlayerData;
import dev.isnow.mcrekus.data.PlayerDataManager;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.util.cache.RekusCache;
import java.util.UUID;
import lombok.Getter;

@Getter
public class PlayerDataCache extends RekusCache<UUID, PlayerData> {
    public PlayerDataCache() {
        super("User",
                MCRekus.getInstance().getDatabaseManager()::loadUserByUUID,
                (uuid, data) -> MCRekus.getInstance().getDatabaseManager().saveUser(data),
                null);
    }
}
