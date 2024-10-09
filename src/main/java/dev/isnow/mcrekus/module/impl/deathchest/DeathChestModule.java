package dev.isnow.mcrekus.module.impl.deathchest;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.deathchest.config.DeathChestConfig;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import eu.decentsoftware.holograms.api.DHAPI;
import java.util.HashMap;
import java.util.UUID;
import lombok.Getter;

@Getter
public class DeathChestModule extends Module<DeathChestConfig> {

    public final HashMap<RekusLocation, DeathChest> deathChests = new HashMap<>();

    public DeathChestModule() {
        super("DeathChest");
    }

    @Override
    public void onEnable(MCRekus plugin) {
        registerListeners("event");
    }

    @Override
    public void onDisable(MCRekus plugin) {
        unRegisterListeners();

        for(final DeathChest deathChest : deathChests.values()) {
            deathChest.remove();
        }
    }
}
