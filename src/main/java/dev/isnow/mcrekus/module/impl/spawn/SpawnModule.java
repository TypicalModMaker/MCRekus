package dev.isnow.mcrekus.module.impl.spawn;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.spawn.config.SpawnConfig;
import dev.isnow.mcrekus.module.impl.spawn.teleport.SpawnTeleportManager;
import lombok.Getter;

@Getter
public class SpawnModule extends Module<SpawnConfig> {

    private SpawnTeleportManager spawnTeleportManager;

    public SpawnModule() {
        super("Spawn", null);
    }

    @Override
    public void onEnable(final MCRekus plugin) {
        spawnTeleportManager = new SpawnTeleportManager();

        registerCommands("command");
        registerListeners("event");
    }

    @Override
    public void onDisable(final MCRekus plugin) {
        unRegisterCommands();
    }
}
