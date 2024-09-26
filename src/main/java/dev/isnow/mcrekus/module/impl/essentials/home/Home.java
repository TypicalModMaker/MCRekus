package dev.isnow.mcrekus.module.impl.essentials.home;

import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Home {
    private String name;
    private RekusLocation location;
}
