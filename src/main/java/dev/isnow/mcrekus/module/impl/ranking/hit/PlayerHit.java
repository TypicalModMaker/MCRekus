package dev.isnow.mcrekus.module.impl.ranking.hit;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.impl.ranking.RankingModule;
import lombok.Data;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Data
public class PlayerHit {
    private final Player player;
    private final long time;

    public boolean isAfterCombat(final int time) {
        return System.currentTimeMillis() - this.time > time;
    }
}
