package dev.isnow.mcrekus.util.migration;

import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Data;

@Data
public class OldPlayerData {
    private final UUID uuid;
    private RekusLocation lastLocation;
    private Map<String, OldHome> homes = new HashMap<>();
}
