package dev.isnow.mcrekus.module.impl.crystal;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;

public class CrystalModule extends Module {
    public CrystalModule() {
        super("Crystal");
    }

    @Override
    public void onEnable(MCRekus plugin) {
        registerListeners("event");
    }

    @Override
    public void onDisable(MCRekus plugin) {
        unRegisterListeners();
    }
}
