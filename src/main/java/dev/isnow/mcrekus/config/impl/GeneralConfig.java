package dev.isnow.mcrekus.config.impl;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.config.MasterConfig;
import dev.isnow.mcrekus.util.RekusLogger;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class GeneralConfig extends MasterConfig {
    @Comment({RekusLogger.bigPrefix, " ", "Rekus Configuration, ask 5170 for more info.", "", "List of enabled modules"})
    private ArrayList<String> enabledModules = new ArrayList<>() {{
        add("Casino");
        add("Crates");
        add("Crystal");
        add("CustomEvents");
        add("DeathChest");
        add("Essentials");
        add("Kingdoms");
        add("Pumpkins");
        add("Ranking");
        add("Spawn");
        add("Spawners");
        add("SpawnProtection");
        add("TimeShop");
        add("WorldProtection");
        add("Xray");
        add("Christmas");
        add("Models");
        add("Music");
    }};

    @Comment({"", "Debug mode (more information in console)"})
    private boolean debugMode = true;

    @Comment({"", "Messages Prefix"})
    private String prefix = "&#a0fb60&lM&#91fb66&lc&#82fc6b&lR&#73fc71&le&#64fd76&lk&#5efd77&lu&#60fe74&ls&#63fe70&l.&#65ff6d&lE&#67ff69&lU &8•";

    @Comment({"", "Increase this if your server is lagging with higher player count"})
    int threadAmount = 20;

    public GeneralConfig() {
        super("config");
    }
}