package dev.isnow.mcrekus.module.impl.kingdoms.event;

import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.kingdoms.constants.land.Land;

public class FireplaceEvent implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByBlockEvent event) {
        if(!(event.getEntity() instanceof Player player)) return;

        if (event.getDamager() == null) return;

        if (!Tag.CAMPFIRES.isTagged(event.getDamager().getType())) return;

        final Land land = Land.getLand(event.getDamager());

        if(land == null || land.getKingdom() == null) return;

        if(!land.getKingdom().isMember(player.getUniqueId())) return;

        event.setCancelled(true);
    }
}
