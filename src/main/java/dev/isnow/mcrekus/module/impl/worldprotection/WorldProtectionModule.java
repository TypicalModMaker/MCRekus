package dev.isnow.mcrekus.module.impl.worldprotection;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.worldprotection.config.WorldProtectionConfig;
import lombok.Getter;

@Getter
public class WorldProtectionModule extends Module<WorldProtectionConfig> {

    public WorldProtectionModule() {
        super("WorldProtection");
    }

    @Override
    public void onEnable(MCRekus plugin) {
        registerListeners("event");
    }

    @Override
    public void onDisable(MCRekus plugin) {

    }
}
