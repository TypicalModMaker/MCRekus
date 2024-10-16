package dev.isnow.mcrekus.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.ranking.RankingModule;
import dev.isnow.mcrekus.module.impl.timeshop.TimeShopModule;
import dev.isnow.mcrekus.util.DataUtil;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        DataUtil.saveData(event.getPlayer());
    }

}
