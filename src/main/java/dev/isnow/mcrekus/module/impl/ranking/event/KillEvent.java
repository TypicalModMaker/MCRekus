package dev.isnow.mcrekus.module.impl.ranking.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.ranking.RankingModule;
import dev.isnow.mcrekus.module.impl.ranking.hit.PlayerHit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KillEvent extends ModuleAccessor<RankingModule> implements Listener {

    @EventHandler
    public void onKill(final PlayerDeathEvent event) {
        final Player player = event.getPlayer();

        if (!getModule().getHitCache().containsKey(player)) return;

        final PlayerHit playerHit = getModule().getHitCache().get(player);

        getModule().getHitCache().remove(player);

        if (playerHit.isAfterCombat(getModule().getConfig().getCombatLogTime())) return;

        getModule().handleKill(player, playerHit.getPlayer());
    }

}
