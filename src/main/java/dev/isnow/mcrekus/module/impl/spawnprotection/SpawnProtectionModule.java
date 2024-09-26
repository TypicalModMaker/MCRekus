package dev.isnow.mcrekus.module.impl.spawnprotection;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.spawnprotection.config.SpawnProtectionConfig;
import dev.isnow.mcrekus.module.impl.spawnprotection.hookedevent.KingdomsClaimEvent;
import dev.isnow.mcrekus.module.impl.spawnprotection.hookedevent.VehiclesEvent;
import dev.isnow.mcrekus.util.cuboid.Cuboid;
import lombok.Getter;

@Getter
public class SpawnProtectionModule extends Module<SpawnProtectionConfig> {

    private Cuboid spawnCuboid;

    public SpawnProtectionModule() {
        super("SpawnProtection", null);
    }

    @Override
    public void onEnable(MCRekus plugin) {
        spawnCuboid = new Cuboid(getConfig().getPos1().toBukkitLocation(), getConfig().getPos2().toBukkitLocation());

        if(plugin.getHookManager().isKingdomsHook()) {
            registerListener(KingdomsClaimEvent.class);
        }

        if(plugin.getHookManager().isVehiclesHook()) {
            registerListener(VehiclesEvent.class);
        }

        registerListeners("event");
        registerCommands("command");
    }

    @Override
    public void onDisable(MCRekus plugin) {

    }
}
