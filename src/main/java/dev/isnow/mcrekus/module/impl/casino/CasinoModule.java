package dev.isnow.mcrekus.module.impl.casino;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.casino.config.CasinoConfig;
import dev.isnow.mcrekus.module.impl.casino.machine.CasinoMachine;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import java.util.HashMap;
import lombok.Getter;

@Getter
public class CasinoModule extends Module<CasinoConfig> {
    private final HashMap<RekusLocation, CasinoMachine> casinoMachines = new HashMap<>();

    public CasinoModule() {
        super("Casino");
    }

    @Override
    public void onEnable(MCRekus plugin) {
        registerCommands("command");
        registerListeners("event");

        for(RekusLocation location : getConfig().getLocations()) {
            casinoMachines.put(location, CasinoMachine.setupMachine(this, location.toBukkitLocation()));
        }
    }

    @Override
    public void onDisable(MCRekus plugin) {
        unRegisterListeners();
        unRegisterCommands();
    }
}
