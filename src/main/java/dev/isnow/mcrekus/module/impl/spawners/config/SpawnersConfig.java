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

    private int cooldownTime = 30;

    private int minTimeTillBreak = 30 * 60;
    private int maxTimeTillBreak = 45 * 60;

    private String cooldownMessage = "[P] &cMusisz odczekać %time% sekund zanim znowu zaczniesz naprawiać ten spawner!";

    private String beingRepairedByAnotherPlayerMessage = "[P] &cTen spawner jest aktualnie naprawiany przez innego gracza!";

    private String failedToRepairMessage = "[P] &cNie udało ci się naprawić spawnera! Spróbuj ponownie za 30 sekund.";

    private String fixingSpawnerSubtitle = "<gradient:#D7753C:#DD7248>Naprawianie spawnera...</gradient>";

    private String cantBreakBrokenSpawnerMessage = "[P] &cNie możesz zniszczyć spawnera, który jest zepsuty!";

    private String repairingDifferentSpawnerMessage = "[P] &cAktualnie naprawiasz inny spawner!";

    private String cantBreakSpawnerMessage = "[P] &cNie możesz zniszczyć tego spawnera!";
}
