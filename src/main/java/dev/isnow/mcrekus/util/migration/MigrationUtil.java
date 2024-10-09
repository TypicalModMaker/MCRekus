package dev.isnow.mcrekus.util.migration;

import static org.hibernate.engine.jdbc.batch.internal.BatchBuilderInitiator.BUILDER;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.data.HomeData;
import dev.isnow.mcrekus.data.PlayerData;
import dev.isnow.mcrekus.util.ExpiringSession;
import dev.isnow.mcrekus.util.RekusLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.hibernate.Transaction;

@UtilityClass
public class MigrationUtil {
    private static final String FILE_PATH = File.separator + "modules" + File.separator + "Essentials" + File.separator + "playerdata" + File.separator;
    private static final Gson BUILDER = new GsonBuilder().setPrettyPrinting().create();

    private List<OldPlayerData> loadUsers() throws IOException {
        final List<OldPlayerData> playerDataList = new ArrayList<>();

        final File dataFolder = new File(MCRekus.getInstance().getDataFolder() + FILE_PATH);

        for(File file : dataFolder.listFiles()) {
            if(file.getName().endsWith(".json")) {
                try(FileReader reader = new FileReader(file)) {
                    final OldPlayerData playerData = BUILDER.fromJson(reader, OldPlayerData.class);
                    RekusLogger.debug("Loaded user: " + playerData.getUuid());
                    playerDataList.add(playerData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return playerDataList;
    }


    public void migrate() throws IOException {
        final List<PlayerData> migratedUsers = new ArrayList<>();

        for(final OldPlayerData playerData : loadUsers()) {
            RekusLogger.debug("Migrating user: " + playerData.getUuid());

            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerData.getUuid());

            String name = offlinePlayer.getName();

            if(name == null) {
                RekusLogger.warn("Could not find player: " + playerData.getUuid());
                name = "Unknown";
            }

            final PlayerData data = new PlayerData(playerData.getUuid(), name);

            if(playerData.getLastLocation() != null) {
                data.setLastLocation(playerData.getLastLocation());
            }


            for (final OldHome home : playerData.getHomes().values()) {
                final HomeData homeData = new HomeData(home.getName(), home.getLocation(), data);

                if(data.getHomeLocations() == null) {
                    data.setHomeLocations(new HashMap<>());
                }

                data.getHomeLocations().put(home.getName(), homeData);
            }

            migratedUsers.add(data);
        }

        final ExpiringSession expiringSession = MCRekus.getInstance().getDatabaseManager().openSession(60);

        Transaction tx = null;
        try {
            tx = expiringSession.getSession().beginTransaction();

            for(final PlayerData user : migratedUsers) {
                expiringSession.getSession().merge(user);
            }

            tx.commit();

            MCRekus.getInstance().getConfigManager().getDatabaseConfig().setMigrate(false);
            MCRekus.getInstance().getConfigManager().saveConfigs();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            RekusLogger.error("Error migrating: " + e.getMessage());
            e.printStackTrace();
        } finally {
            expiringSession.closeSession();
        }

        RekusLogger.info("Migrated " + migratedUsers.size() + " users.");

    }
}
