package dev.isnow.mcrekus.module.impl.ranking.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.ranking.RankingModule;
import dev.isnow.mcrekus.module.impl.ranking.hit.PlayerHit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HitEvent extends ModuleAccessor<RankingModule> implements Listener {

    @EventHandler
    public void onHit(final EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager) || !(event.getEntity() instanceof Player player)) return;

        getModule().getHitCache().put(player, new PlayerHit(damager, System.currentTimeMillis()));
    }

}
