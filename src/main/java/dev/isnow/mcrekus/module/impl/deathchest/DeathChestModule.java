package dev.isnow.mcrekus.module.impl.deathchest;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.deathchest.config.DeathChestConfig;
import dev.isnow.mcrekus.module.impl.spawnprotection.config.SpawnProtectionConfig;
import dev.isnow.mcrekus.module.impl.spawnprotection.hookedevent.KingdomsClaimEvent;
import dev.isnow.mcrekus.module.impl.spawnprotection.hookedevent.VehiclesEvent;
import dev.isnow.mcrekus.util.cuboid.Cuboid;
import lombok.Getter;

@Getter
public class DeathChestModule extends Module<DeathChestConfig> {

    public DeathChestModule() {
        super("DeathChest", null);
    }

    @Override
    public void onEnable(MCRekus plugin) {
        registerListeners("event");
    }

    @Override
    public void onDisable(MCRekus plugin) {

    }
}
