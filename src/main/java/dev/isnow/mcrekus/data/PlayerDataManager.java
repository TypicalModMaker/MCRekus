package dev.isnow.mcrekus.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.essentials.data.EssentialsPlayerData;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.GsonUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.RuntimeTypeAdapterFactory;
import dev.isnow.mcrekus.util.cache.impl.PlayerDataCache;
import dev.isnow.mcrekus.util.serializer.location.ReferenceSerializer;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

public class PlayerDataManager {
    private static final String FILE_PATH = File.separator + "PlayerData" + File.separator;
    private static final Gson BUILDER = GsonUtil.buildGson();

    private final HashMap<String, PlayerDataCache> playerDataCache = new HashMap<>();

    public PlayerData loadUser(final String module, final UUID uuid) {
        final File file = new File(MCRekus.getInstance().getDataFolder() + FILE_PATH + uuid + ".json");

        if (!file.exists()) {
            return null;
        }

        final GlobalPlayerData globalPlayerData;

        try (final FileReader reader = new FileReader(file)) {
            globalPlayerData = BUILDER.fromJson(reader, GlobalPlayerData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(globalPlayerData.getPlayerData().get(module) == null) {
            RekusLogger.warn("Player data for " + uuid + " is null for module " + module);
            return null;
        }

        return globalPlayerData.getPlayerData().get(module);
    }

    public void saveData(final UUID uuid, final PlayerData playerData) {
        final File file = new File(MCRekus.getInstance().getDataFolder() + FILE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }

        final GlobalPlayerData globalPlayerData = new GlobalPlayerData(uuid, playerDataCache.values().stream().collect(HashMap::new, (map, cache) -> map.put(cache.get(uuid).getModule(), cache.get(uuid)), HashMap::putAll));

        try (final Writer writer = new FileWriter(MCRekus.getInstance().getDataFolder() + FILE_PATH + uuid + ".json")) {
            BUILDER.toJson(globalPlayerData, writer);

            RekusLogger.debug("Saved user data for " + uuid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public PlayerData getPlayerData(final Module<?> module, UUID uuid) {
        return playerDataCache.get(module.getName()).get(uuid);
    }

    public void registerCache(final String module, final Class<? extends PlayerData> type) {
        playerDataCache.put(module, new PlayerDataCache(module, this, type));
    }

    public void unloadAllCaches() {
        for(final String module : playerDataCache.keySet()) {
            playerDataCache.remove(module);
        }
    }

    public void preloadPlayerData(final AsyncPlayerPreLoginEvent event) {
        RekusLogger.info("Preloading player datas for " + event.getUniqueId());

        try {
            for(final PlayerDataCache cache : playerDataCache.values()) {
                cache.preload(event.getUniqueId());

                if(cache.get(event.getUniqueId()) == null) {
                    final Class<? extends PlayerData> type = cache.getType();

                    final PlayerData playerData = type.getDeclaredConstructor().newInstance();

                    cache.put(event.getUniqueId(), playerData);
                }
            }
        } catch (Exception e) {
            RekusLogger.error("Failed to preload player datas for " + event.getUniqueId());
            event.setLoginResult(Result.KICK_OTHER);
            event.kickMessage(ComponentUtil.deserialize("&cWystąpił błąd podczas ładowania danych gracza! Skontaktuj się z administracją."));
            e.printStackTrace();
        }
    }

    public void addUser(final Module<?> module, UUID uuid, PlayerData playerData) {
        playerDataCache.get(module.getName()).put(uuid, playerData);
    }

    public void saveAll() {
        playerDataCache.values().forEach(cache -> cache.getKeys().forEach(uuid -> saveData(uuid, cache.get(uuid))));
    }


}
