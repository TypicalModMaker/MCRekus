package dev.isnow.mcrekus.util;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.ranking.RankingModule;
import dev.isnow.mcrekus.module.impl.timeshop.TimeShopModule;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public class DataUtil {

    public void saveData(final Player player) {
        MCRekus.getInstance().getDatabaseManager().getUserAsync(player, (session, data) -> {
            data.setLastLocation(RekusLocation.fromBukkitLocation(player.getLocation()));

            final dev.isnow.mcrekus.module.Module<?> timeShopModule = MCRekus.getInstance().getModuleManager().getModuleByName("TimeShop");
            if(timeShopModule != null) {
                final TimeShopModule timeShop = (TimeShopModule) timeShopModule;
                final long time = System.currentTimeMillis() - timeShop.getJoinTime().get(player);
                data.setTime(data.getTime() + time);

                timeShop.getJoinTime().remove(player);
            }

            final Module<?> rankingModule = MCRekus.getInstance().getModuleManager().getModuleByName("Ranking");
            if(rankingModule != null) {
                final RankingModule ranking = (RankingModule) rankingModule;

                final int elo = ranking.getRankingCache().get(player);
                RekusLogger.debug("Saving elo: " + elo);

                data.setElo(elo);
                ranking.getRankingCache().remove(player);
            }

            data.save(session);
        });
    }
}
