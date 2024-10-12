package dev.isnow.mcrekus.module.impl.spawners.config;

import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class SpawnersConfig extends ModuleConfig {
    public SpawnersConfig() {
        super("config", "Spawners");
    }

    private int cooldownTime = 300;

    private int minTimeTillBreak = 15 * 60;
    private int maxTimeTillBreak = 25 * 60;

    private String cooldownMessage = "&cMusisz odczekać %time% sekund zanim znowu zaczniesz naprawiać ten spawner!";

    private String beingRepairedByAnotherPlayerMessage = "&cTen spawner jest aktualnie naprawiany przez innego gracza!";

    private String failedToRepairMessage = "&cNie udało ci się naprawić spawnera! Spróbuj ponownie za 5 minut.";

    private String fixingSpawnerSubtitle = "<gradient:#D7753C:#DD7248>Naprawianie spawnera...</gradient>";

    private String cantBreakBrokenSpawnerMessage = "&cNie możesz zniszczyć spawnera, który jest zepsuty!";

    private String repairingDifferentSpawnerMessage = "&cAktualnie naprawiasz inny spawner!";
}
