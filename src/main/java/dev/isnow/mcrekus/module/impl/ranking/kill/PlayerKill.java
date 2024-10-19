package dev.isnow.mcrekus.module.impl.ranking.kill;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerKill {
    private final UUID killer;
    private long time;
}
