package dev.isnow.mcrekus.module.impl.casino.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

@Getter
@Setter
@Configuration
public class CasinoConfig extends ModuleConfig {
    public CasinoConfig() {
        super("config", "Casino");
    }

    @Comment({"", "Casino locations"})
    public List<RekusLocation> locations = new ArrayList<>();

    @Comment({"", "Casino block types"})
    public List<Material> blockTypes = List.of(Material.DIAMOND_BLOCK, Material.GOLD_BLOCK, Material.EMERALD_BLOCK);

    @Comment({"", "Casino win commands"})
    public List<String> winCommands = List.of("money %player% kasa give 2500");

    @Comment({"", "Casino title fade in"})
    public int titleFadeIn = 500;

    @Comment({"", "Casino title stay"})
    public int titleStay = 3000;

    @Comment({"", "Casino title fade out"})
    public int titleFadeOut = 500;

    @Comment({"", "Casino title losing text"})
    public String titleLosingText = "<gradient:#ff0000:#FF3737><bold><tinify>Przegrales!</tinify></gradient>";

    @Comment({"", "Casino title losing subtext"})
    public String titleLosingSubtext = "&#FFD900<tinify>Sprobuj ponownie!</tinify>";

    @Comment({"", "Casino title winning text"})
    public String titleWinningText = "<gradient:#22FF00:#00FF4F><bold><tinify>Wygrales!</tinify></gradient>";

    @Comment({"", "Casino title winning subtext"})
    public String titleWinningSubtext = "<color:yellow>1500 \uD83D\uDCB2";

    @Comment({"", "Casino already being used message"})
    public String alreadyBeingUsedMessage = "[P] <gradient:#ff0000:#FF3737><bold><tinify>Ta maszyna jest zajęta!</tinify></gradient>";

    @Comment({"", "Casino not enough money message"})
    public String notEnoughMoneyMessage = "[P] <gradient:#ff0000:#FF3737><bold><tinify>Nie masz wystarczająco pieniędzy!</tinify></gradient>";

    @Comment({"", "Casino required money"})
    public int requiredMoney = 500;
}
