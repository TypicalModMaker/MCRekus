package dev.isnow.mcrekus.module.impl.deathchest;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.deathchest.config.DeathChestConfig;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;

@Getter
public class DeathChestModule extends Module<DeathChestConfig> {

    public final ConcurrentHashMap<RekusLocation, DeathChest> deathChests = new ConcurrentHashMap<>();

    public DeathChestModule() {
        super("DeathChest");
    }

    @Override
    public void onEnable(final MCRekus plugin) {
        registerListeners("event");
    }

    @Override
    public void onDisable(final MCRekus plugin) {
        unRegisterListeners();

        for(final DeathChest deathChest : deathChests.values()) {
            deathChest.remove();
        }
    }
}
