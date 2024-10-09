package dev.isnow.mcrekus.util.migration;

import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OldHome {
    private String name;
    private RekusLocation location;
}
