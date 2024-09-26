package dev.isnow.mcrekus.module.impl.worldprotection.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.worldprotection.WorldProtectionModule;
import dev.isnow.mcrekus.module.impl.worldprotection.config.WorldProtectionConfig;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Phantom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SpawnEvent extends ModuleAccessor<WorldProtectionModule> implements Listener {

    @EventHandler
    public void onSpawn(final CreatureSpawnEvent event) {
        if (switch (event.getSpawnReason()) {
            case CUSTOM, COMMAND -> true;
            default -> false;
        }) return;

        final WorldProtectionConfig config = getModule().getConfig();

        final Entity entity = event.getEntity();

        switch (entity) {
            case CaveSpider caveSpider when config.isDisableCaveSpiderSpawn() -> event.setCancelled(true);
            case Phantom phantom when config.isDisablePhantomSpawn() -> event.setCancelled(true);
            default -> {}
        }
    }
}
