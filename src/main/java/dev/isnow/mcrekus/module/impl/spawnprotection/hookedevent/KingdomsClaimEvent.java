package dev.isnow.mcrekus.module.impl.spawnprotection.hookedevent;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawnprotection.SpawnProtectionModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kingdoms.events.lands.ClaimLandEvent;

public class KingdomsClaimEvent extends ModuleAccessor<SpawnProtectionModule> implements Listener {

    @EventHandler
    public void onClaim(ClaimLandEvent e) {
        if(e.getLandLocations().stream().noneMatch(simpleChunkLocation -> getModule().getSpawnCuboid().isIn(simpleChunkLocation.getCenterLocation()))) return;

        e.setCancelled(true);

        if(e.getPlayer() == null || e.getPlayer().getPlayer() == null) return;

        e.getPlayer().getPlayer().sendMessage(ComponentUtil.deserialize(getModule().getConfig().getSpawnClaimMessage()));
    }
}
