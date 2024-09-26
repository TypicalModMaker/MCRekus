package dev.isnow.mcrekus.module.impl.deathchest.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter
@Setter
@Configuration
public class DeathChestConfig extends ModuleConfig {
    public DeathChestConfig() {
        super("config", "DeathChest");
    }

    @Comment("The hologram that will be displayed above the death chest.")
    private String[] hologram = {"&cSkrzynka śmierci gracza %player%", "&7Zniknie za: &c%time%"};

    @Comment("The time in seconds after which the death chest will disappear.")
    private int time = 300;
}
