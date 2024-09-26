package dev.isnow.mcrekus.module.impl.spawnprotection.hookedevent;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawnprotection.SpawnProtectionModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import es.pollitoyeye.vehicles.events.VehicleEnterEvent;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kingdoms.events.lands.ClaimLandEvent;

public class VehiclesEvent extends ModuleAccessor<SpawnProtectionModule> implements Listener {

    @EventHandler
    public void onEnter(VehicleEnterEvent e) {
        if (!getModule().getSpawnCuboid().isIn(e.getMainArmorStand())) return;

        if (e.getPlayer().hasPermission("mcrekus.spawnprotection")) return;

        e.setCancelled(true);

        e.getPlayer().getPlayer().sendMessage(ComponentUtil.deserialize(getModule().getConfig().getSpawnClaimMessage()));
    }
}
