package dev.isnow.mcrekus.module.impl.ranking.kill;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
public class PlayerKill {
    private final UUID killer;
    private long time;
}
