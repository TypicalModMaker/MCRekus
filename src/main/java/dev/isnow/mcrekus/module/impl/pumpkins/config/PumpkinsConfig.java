package dev.isnow.mcrekus.module.impl.pumpkins.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class PumpkinsConfig extends ModuleConfig {
    public PumpkinsConfig() {
        super("config", "Pumpkins");
    }

    @Comment({"", "Setup Pumpkins command ON message"})
    private String setupPumpkinsON = "&aWłączono tryb ustawiania &#FF6300Dyni";

    @Comment({"", "Setup Pumpkins command OFF message"})
    private String setupPumpkinsOFF = "&cWyłączono tryb ustawiania &#FF6300Dyni";

    @Comment({"", "Setup Pumpkin place message"})
    private String setupPumpkinPlace = "&aUstawiono &#FF6300Dynie";

    @Comment({"", "Setup Pumpkin remove message"})
    private String setupPumpkinRemove = "&cUsunięto &#FF6300Dynie";

    @Comment({"", "Pumpkin interact title"})
    private String pumpkinInteractTitle = "&aZnaleziono &#FF6300Dynie&a!";

    @Comment({"", "Pumpkin interact subtitle"})
    private String pumpkinInteractSubTitle = "&7(&a%amount%&7/&a%max%&7)";

    @Comment({"", "Pumpkin interact title fade in"})
    private int pumpkinInteractTitleFadeIn = 1;

    @Comment({"", "Pumpkin interact title stay"})
    private int pumpkinInteractTitleStay = 10;

    @Comment({"", "Pumpkin interact title fade out"})
    private int pumpkinInteractTitleFadeOut = 1;

    private int pumpkinAmount = 20;

    private List<String> commandsCollectedAll = List.of("bc chat &a%player% zebrał wszystkie dynie!");


}