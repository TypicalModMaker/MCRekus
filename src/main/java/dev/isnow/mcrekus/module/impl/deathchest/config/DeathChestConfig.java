package dev.isnow.mcrekus.module.impl.deathchest.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class DeathChestConfig extends ModuleConfig {
    public DeathChestConfig() {
        super("config", "DeathChest");
    }

    @Comment("The hologram that will be displayed above the death chest.")
    private String[] hologram = {"&cSkrzynka śmierci gracza %player%", "&7Czas śmierci: &c%time%", "Zniknie za: &c%time-remaining%"};

    @Comment("Time format for the death time.")
    private String timeFormat = "HH:mm:ss";

    @Comment("The time in seconds after which the death chest will disappear.")
    private int time = 300;

    @Comment("The particle that will be displayed above the death chest.")
    private String particle = "FIREWORKS_SPARK";

    @Comment("The hologram offset from the death chest.")
    private double hologramOffset = 2.5;
}
