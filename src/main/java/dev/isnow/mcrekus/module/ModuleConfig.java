package dev.isnow.mcrekus.module;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.config.RekusConfig;
import java.io.File;
import java.nio.file.Paths;

public abstract class ModuleConfig extends RekusConfig {
    public ModuleConfig(final String name, final String moduleName) {
        super(name, Paths.get(MCRekus.getInstance().getDataFolder() + File.separator + "modules" + File.separator + moduleName + File.separator + name + ".yml"));
    }
}
