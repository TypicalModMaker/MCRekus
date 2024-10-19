package dev.isnow.mcrekus.module.impl.worldprotection.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class WorldProtectionConfig extends ModuleConfig {
    public WorldProtectionConfig() {
        super("config", "WorldProtection");
    }

    @Comment({"World Protection module configuration (EssentialsProtect Alternative)", "", "Disable lava flow"})
    private boolean disableLavaFlow = true;

    @Comment({"", "Disable water flow"})
    private boolean disableWaterFlow = false;

    @Comment({"", "Disable tnt explosion"})
    private boolean disableTNTExplosion = true;

    @Comment({"", "Disable tnt minecart explosion"})
    private boolean disableTNTMinecartExplosion = true;

    @Comment({"", "Disable fireball explosion"})
    private boolean disableFireballExplosion = true;

    @Comment({"", "Disable phantom spawn"})
    private boolean disablePhantomSpawn = true;

    @Comment({"", "Disable cave spider spawn"})
    private boolean disableCaveSpiderSpawn = true;

    @Comment({"", "Disable thunder"})
    private boolean disableThunder = true;

    @Comment({"", "Disable storm"})
    private boolean disableStorm = true;

    @Comment({"", "Disable wither skull explosion"})
    private boolean disableWitherSkullExplosion = true;
}
