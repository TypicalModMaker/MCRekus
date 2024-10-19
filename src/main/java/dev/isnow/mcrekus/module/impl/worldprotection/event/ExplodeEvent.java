package dev.isnow.mcrekus.module.impl.worldprotection.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.worldprotection.WorldProtectionModule;
import dev.isnow.mcrekus.module.impl.worldprotection.config.WorldProtectionConfig;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplodeEvent extends ModuleAccessor<WorldProtectionModule> implements Listener {

    @EventHandler
    public void onSpawn(final EntityExplodeEvent event) {
        final Entity entity = event.getEntity();

        final WorldProtectionConfig config = getModule().getConfig();

        switch (entity) {
            case TNTPrimed tntPrimed when config.isDisableTNTExplosion() -> event.setCancelled(true);
            case ExplosiveMinecart explosiveMinecart when config.isDisableTNTMinecartExplosion() -> event.setCancelled(true);
            case Fireball fireball when config.isDisableFireballExplosion() -> event.setCancelled(true);
            case WitherSkull skull when config.isDisableWitherSkullExplosion() -> event.setCancelled(true);
            default -> {}
        }

    }
}
