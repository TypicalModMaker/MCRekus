package dev.isnow.mcrekus.module.impl.spawnprotection.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawnprotection.SpawnProtectionModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import me.matsubara.vehicles.event.VehicleSpawnEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;

public class VehicleEvent extends ModuleAccessor<SpawnProtectionModule> implements Listener {
    @EventHandler
    public void onDestroy(VehicleDestroyEvent event) {
        if(!(event.getAttacker() instanceof Player player)) return;

        if (!getModule().getSpawnCuboid().isIn(event.getVehicle())) return;

        if(player.hasPermission("mcrekus.spawnprotection")) return;

        event.setCancelled(true);
        player.sendMessage(ComponentUtil.deserialize(getModule().getConfig().getSpawnDestroyVehicleMessage()));
    }

    @EventHandler
    public void onMount(final VehicleSpawnEvent event) {
        final Player player = event.getPlayer();

        if (player == null) return;

        if (!getModule().getSpawnCuboid().isIn(event.getLocation())) return;

        if(player.hasPermission("mcrekus.spawnprotection")) return;

        event.setCancelled(true);
        player.sendMessage(ComponentUtil.deserialize(getModule().getConfig().getSpawnBlockPlaceMessage()));
    }
}
