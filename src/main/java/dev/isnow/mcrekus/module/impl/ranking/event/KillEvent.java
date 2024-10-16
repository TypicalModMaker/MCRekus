package dev.isnow.mcrekus.module.impl.ranking.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.database.DatabaseManager;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.ranking.RankingModule;
import dev.isnow.mcrekus.module.impl.ranking.config.RankingConfig;
import dev.isnow.mcrekus.module.impl.ranking.hit.PlayerHit;
import dev.isnow.mcrekus.util.ComponentUtil;
import java.time.Duration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class KillEvent extends ModuleAccessor<RankingModule> implements Listener {

    @EventHandler
    public void onKill(final PlayerDeathEvent event) {
        final Player player = event.getPlayer();

        if (!getModule().getHitCache().containsKey(player)) return;

        final PlayerHit playerHit = getModule().getHitCache().get(player);

        if (playerHit.isAfterCombat(getModule().getConfig().getCombatLogTime())) return;

        getModule().handleKill(player, playerHit.getPlayer());
    }

}
