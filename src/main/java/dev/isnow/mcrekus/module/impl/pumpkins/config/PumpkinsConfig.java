package dev.isnow.mcrekus.module.impl.pumpkins.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import dev.isnow.mcrekus.util.Range;
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
    private String setupPumpkinsON = "[P] &aWłączono tryb ustawiania &#FF6300Dyni";

    @Comment({"", "Setup Pumpkins command OFF message"})
    private String setupPumpkinsOFF = "[P] &cWyłączono tryb ustawiania &#FF6300Dyni";

    @Comment({"", "Setup Pumpkin place message"})
    private String setupPumpkinPlace = "[P] &aUstawiono &#FF6300Dynie";

    @Comment({"", "Setup Pumpkin remove message"})
    private String setupPumpkinRemove = "[P] &cUsunięto &#FF6300Dynie";

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

    private List<PumpkinReward> rewards = List.of(
        new PumpkinReward(new Range(1, 5), List.of("bc chat &a%player% zebrał 1-5 dyni!")),
        new PumpkinReward(new Range(6, 10), List.of("bc chat &a%player% zebrał 6-10 dyni!")),
        new PumpkinReward(new Range(11, 15), List.of("bc chat &a%player% zebrał 11-15 dyni!")),
        new PumpkinReward(new Range(16, 20), List.of("bc chat &a%player% zebrał 16-20 dyni!"))
    );


}