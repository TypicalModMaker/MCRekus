package dev.isnow.mcrekus.module.impl.spawners;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;

public class SpawnersModule extends Module {
    public SpawnersModule() {
        super("Spawners");
    }

    @Override
    public void onEnable(MCRekus plugin) {
        registerListeners("event");
    }

    @Override
    public void onDisable(MCRekus plugin) {

    }
}
