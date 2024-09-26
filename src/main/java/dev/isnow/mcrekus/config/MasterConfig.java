package dev.isnow.mcrekus.config;

import dev.isnow.mcrekus.MCRekus;
import java.io.File;
import java.nio.file.Paths;

public abstract class MasterConfig extends RekusConfig {
    public MasterConfig(final String name) {
        super(name, Paths.get(MCRekus.getInstance().getDataFolder() + File.separator + name + ".yml"));
    }
}
