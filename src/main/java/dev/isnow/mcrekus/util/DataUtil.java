package dev.isnow.mcrekus.util;

import dev.isnow.mcrekus.MCRekus;
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

            final TimeShopModule timeShopModule = MCRekus.getInstance().getModuleManager().getModuleByName("TimeShop");
            if(timeShopModule != null) {
                final long time = System.currentTimeMillis() - timeShopModule.getJoinTime().get(player);
                data.setTime(data.getTime() + time);

                timeShopModule.getJoinTime().remove(player);
            }

            final RankingModule rankingModule = MCRekus.getInstance().getModuleManager().getModuleByName("Ranking");
            if(rankingModule != null) {
                final int elo = rankingModule.getRankingCache().get(player);
                RekusLogger.debug("Saving elo: " + elo);

                data.setElo(elo);
                rankingModule.getRankingCache().remove(player);
            }

            data.save(session);
        });
    }
}
