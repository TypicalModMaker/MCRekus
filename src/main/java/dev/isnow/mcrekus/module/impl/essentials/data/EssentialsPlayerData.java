package dev.isnow.mcrekus.module.impl.essentials.data;

import dev.isnow.mcrekus.data.PlayerData;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.home.Home;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter@Setter
public class EssentialsPlayerData extends PlayerData {
    private RekusLocation lastLocation;
    private Map<String, Home> homes = new HashMap<>();

    public EssentialsPlayerData() {
        super("Essentials", EssentialsPlayerData.class);
    }
}
