package dev.isnow.mcrekus.module.impl.ranking.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.ranking.RankingModule;
import dev.isnow.mcrekus.module.impl.ranking.hit.PlayerHit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent extends ModuleAccessor<RankingModule> implements Listener {

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        final PlayerHit playerHit = getModule().getHitCache().get(player);

        if (playerHit == null) return;

        getModule().getHitCache().remove(player);

        if (playerHit.isAfterCombat(getModule().getConfig().getCombatLogTime())) return;

        getModule().handleKill(player, playerHit.getPlayer());
    }
}
