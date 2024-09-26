package dev.isnow.mcrekus.config;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.config.impl.GeneralConfig;
import java.io.File;
import lombok.Getter;

@Getter
public class ConfigManager {
    private final MCRekus plugin;

    private GeneralConfig generalConfig;

    public ConfigManager() {
        plugin = MCRekus.getInstance();

        final File modulesPath = new File(plugin.getDataFolder() + File.separator + "modules");

        if(!modulesPath.exists()) {
            modulesPath.mkdir();
        }

        loadAll();
    }

    private void loadAll() {
        generalConfig = (GeneralConfig) new GeneralConfig().load();
    }

    public void saveConfigs() {
        generalConfig.save();
    }

    public void reloadConfigs() {
        loadAll();
    }

}