package dev.isnow.mcrekus.module.impl.spawnprotection.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawnprotection.SpawnProtectionModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageEvent extends ModuleAccessor<SpawnProtectionModule> implements Listener {
    @EventHandler
    public void onDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (player.hasPermission("mcrekus.spawnprotection")) return;

        if (!getModule().getSpawnCuboid().isIn(player)) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player damager && damager.hasPermission("mcrekus.spawnprotection")) return;

        if (!getModule().getSpawnCuboid().isIn(event.getEntity())) return;

        event.setCancelled(true);
    }
}
