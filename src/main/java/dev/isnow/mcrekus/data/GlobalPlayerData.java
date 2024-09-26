package dev.isnow.mcrekus.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class GlobalPlayerData {
    public final UUID uuid;
    public final HashMap<String, PlayerData> playerData;
}
