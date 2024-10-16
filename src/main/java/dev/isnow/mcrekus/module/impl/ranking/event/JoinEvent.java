package dev.isnow.mcrekus.module.impl.ranking.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.ranking.RankingModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent extends ModuleAccessor<RankingModule> implements Listener{

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        MCRekus.getInstance().getDatabaseManager().getUserAsync(event.getPlayer(), (session, data) -> {
            getModule().getRankingCache().put(event.getPlayer(), data.getElo());
        });
    }

}
