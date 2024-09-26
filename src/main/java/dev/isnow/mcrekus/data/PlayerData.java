package dev.isnow.mcrekus.data;

import dev.isnow.mcrekus.module.Module;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public abstract class PlayerData {
    private final transient String module;
    private final transient Class<? extends PlayerData> type;

    public PlayerData(String module, Class<? extends PlayerData> type) {
        this.module = module;
        this.type = type;
    }
}
