package dev.isnow.mcrekus.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.isnow.mcrekus.data.PlayerData;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GsonUtil {

    @SafeVarargs
    public Gson buildGson(final Class<? extends PlayerData>... classes) {
        RuntimeTypeAdapterFactory<PlayerData> playerDataAdapterFactory = RuntimeTypeAdapterFactory
                .of(PlayerData.class, "type");

        for (Class<? extends PlayerData> clazz : classes) {
            playerDataAdapterFactory.registerSubtype(clazz);
        }


        return new GsonBuilder()
                .registerTypeAdapterFactory(playerDataAdapterFactory)
                .create();
    }

}
