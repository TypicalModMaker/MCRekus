package dev.isnow.mcrekus.config;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.config.impl.GeneralConfig;
import dev.isnow.mcrekus.config.impl.database.DatabaseConfig;
import java.io.File;
import lombok.Getter;

@Getter
public class ConfigManager {
    private final MCRekus plugin;

    private GeneralConfig generalConfig;
    private DatabaseConfig databaseConfig;

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
        databaseConfig = (DatabaseConfig) new DatabaseConfig().load();
    }

    public void saveConfigs() {
        generalConfig.save();
    }

    public void reloadConfigs() {
        loadAll();
    }

}