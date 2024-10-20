package dev.isnow.mcrekus.module.impl.kingdoms.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class KingdomsConfig extends ModuleConfig {
    public KingdomsConfig() {
        super("config", "Kingdoms");
    }

    @Comment("Champion teleport delay in seconds")
    private int championTeleportDelay = 30;

    @Comment("Champion teleport y offset")
    private double championTeleportYOffset = 2;

    @Comment("Invasion player list message")
    private String invasionPlayerListMessage = "&#66FB5FKrólestwa &8» &#961E1E%player% &frozpoczął inwazję na twoim terenie wraz ze swoimi sojusznikami: &#961E1E%allies%";

    @Comment("Invasion player list message for defenders")
    private String invasionPlayerListMessageDefender = "&#66FB5FKrólestwa &8» &fRozpocząłeś inwazję na królestwo &#961E1E%kingdom_name% &ftwoimi pomocnikami są: &#66FB5F%allies%";

    @Comment("Invasion player list message for allies")
    private String invasionPlayerListMessageAlly = "&#66FB5FKrólestwa &8» &fJesteś pomocnikiem w inwazji na królestwo &#961E1E%kingdom_name% &ftwoim dowódcą jest: &#66FB5F%player%";

    @Comment("Cant explode bed in nether message")
    private String kingdomsBedMessage = "[P] &cNie możesz eksplodować łóżek w Netherze!";
}
