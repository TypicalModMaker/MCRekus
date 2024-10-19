package dev.isnow.mcrekus.module.impl.ranking.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class RankingConfig extends ModuleConfig {
    public RankingConfig() {
        super("config", "Ranking");
    }

    @Comment({"", "Default elo for new players"})
    private int defaultElo = 500;

    private int titleFadeIn = 500;

    private int titleFadeOut = 500;

    private int titleStay = 3000;

    private String titleKiller = "<gradient:#FF0000:#FF3737><bold>ZABÓJSTWO!</gradient>";

    private String subtitleKiller = "&fZabiłeś gracza &4%player% &7| &e%old_elo% &7➡ &e%new_elo%";

    private String titleKilled = "<gradient:#FF0000:#FF3737><bold>ŚMIERĆ!</gradient>";

    private String subtitleKilled = "&fZostałeś zabity przez gracza &4%player% &7| &e%old_elo% &7➡ &e%new_elo%";

    private int combatLogTime = 30 * 1000;
}