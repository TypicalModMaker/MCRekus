package dev.isnow.mcrekus.database;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.config.impl.GeneralConfig;
import dev.isnow.mcrekus.config.impl.database.DatabaseType;
import dev.isnow.mcrekus.data.PlayerData;
import dev.isnow.mcrekus.data.query.QPlayerData;
import dev.isnow.mcrekus.util.FileUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import io.ebean.DB;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.Transaction;
import io.ebean.annotation.Platform;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.ebean.dbmigration.DbMigration;
import io.ebean.migration.MigrationConfig;
import io.ebean.migration.MigrationRunner;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class DatabaseManager {

    static int SCHEMA_VERSION = 1;

    Database db;

    public DatabaseManager(final ClassLoader pluginLoader) {
        Database db;

        final dev.isnow.mcrekus.config.impl.database.DatabaseConfig authConfig = MCRekus.getInstance().getConfigManager().getDatabaseConfig();

        final DataSourceConfig dataSourceConfig = getDataSourceConfig(authConfig);

        final GeneralConfig masterConfig = MCRekus.getInstance().getConfigManager().getGeneralConfig();
        final DatabaseConfig config = getDatabaseConfig(dataSourceConfig, masterConfig);

        final String dataPath = MCRekus.getInstance().getDataFolder().getAbsolutePath() + File.separator + "do_not_delete_databaseMigrations";

        final boolean firstRun = masterConfig.isFirstRun();

        if (firstRun) {
            generateMigration(authConfig.getDatabaseType());
        } else if (SCHEMA_VERSION > masterConfig.getSchemaVersion()) {
            RekusLogger.info("Schema version changed! MCRekus will migrate the database automatically.");
            generateMigration(authConfig.getDatabaseType());

            final MigrationConfig migrationConfig = new MigrationConfig();
            migrationConfig.setDbUsername(authConfig.getUsername());
            migrationConfig.setAllowErrorInRepeatable(true);
            migrationConfig.setDbPassword(authConfig.getPassword());
            migrationConfig.setDbUrl(getUrl(authConfig));
            migrationConfig.setMigrationPath("filesystem:" + dataPath);

            final Optional<File> initMigration = dev.isnow.mcrekus.util.FileUtil.getOldestFile(new File(dataPath));

            AtomicReference<Path> outsidePath = new AtomicReference<>();

            initMigration.ifPresent(file -> {
                outsidePath.set(Path.of(file.getParent() + File.separator +
                        ".." + File.separator + file.getName()));
                try {
                    Files.move(file.toPath(), outsidePath.get(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    RekusLogger.error("Failed to move initial migration file outside migration folder. Error: " + e);
                }
            });

            RekusLogger.info("Running Migrator...");
            MigrationRunner runner = new MigrationRunner(migrationConfig);
            runner.run();

            initMigration.ifPresent(file -> {
                try {
                    Files.move(outsidePath.get(), Path.of(dataPath + File.separator + file.getName()), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    RekusLogger.error("Failed to move initial migration file inside migration folder. Error: " + e);
                }
            });
        }
        try {
            db = DatabaseFactory.createWithContextClassLoader(config, pluginLoader);

            // Successfully migrated
            if (!firstRun) {
                masterConfig.setSchemaVersion(SCHEMA_VERSION);
                MCRekus.getInstance().getConfigManager().saveConfigs();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            db = null;
        }

        this.db = db;
    }

    private void generateMigration(final dev.isnow.mcrekus.config.impl.database.DatabaseType type) {
        final Platform platform = Platform.valueOf(type.name());

        DbMigration dbMigration = DbMigration.create();
        dbMigration.setPlatform(platform);
        dbMigration.setMigrationPath("do_not_delete_databaseMigrations");
        dbMigration.setPathToResources(MCRekus.getInstance().getDataFolder().getAbsolutePath());
        dbMigration.setStrictMode(false);
        dbMigration.setApplyPrefix("V");
        try {
            dbMigration.generateMigration();
        } catch (IOException e) {
            RekusLogger.error("Failed to generate migration script! Contact 5170");
            e.printStackTrace();
        }
    }

    private DatabaseConfig getDatabaseConfig(final DataSourceConfig dataSourceConfig, final GeneralConfig masterConfig) {
        final DatabaseConfig config = new DatabaseConfig();
        config.setDataSourceConfig(dataSourceConfig);
        config.setName("db");

        // Generate tables
        if (masterConfig.isFirstRun()) {
            config.ddlGenerate(true);
            config.ddlRun(true);
        }

        return config;
    }

    private DataSourceConfig getDataSourceConfig(final dev.isnow.mcrekus.config.impl.database.DatabaseConfig authConfig) {
        final DataSourceConfig dataSourceConfig = new DataSourceConfig();

        dataSourceConfig.setUsername(authConfig.getUsername());
        dataSourceConfig.setPassword(authConfig.getPassword());
        dataSourceConfig.setUrl(getUrl(authConfig));

        return dataSourceConfig;
    }

    private String getUrl(final dev.isnow.mcrekus.config.impl.database.DatabaseConfig authConfig) {
        switch (authConfig.getDatabaseType()) {
            case MYSQL -> {
                return authConfig.getDatabaseType().getPrefix() + authConfig.getHost() + "/" + authConfig.getDatabase();
            }
            case MARIADB -> {
                return authConfig.getDatabaseType().getPrefix() + authConfig.getHost() + "/" + authConfig.getDatabase() + authConfig.getDatabaseType().getSuffix();
            }
            case H2 -> {
                return authConfig.getDatabaseType().getPrefix() + MCRekus.getInstance().getDataFolder().getAbsolutePath() + "/" + authConfig.getDatabase();
            }
            default -> throw new IllegalArgumentException();
        }
    }

    public void shutdown() {
        RekusLogger.info("Shutting down the database connection.");

        db.shutdown();
    }

    public void saveUser(final PlayerData user) {
        if(Bukkit.isPrimaryThread()) {
            RekusLogger.warn("Saving user data on the main thread!!!");
        }

        RekusLogger.debug("Saving user " + user.getName());

        db.save(user);
    }

    public PlayerData loadUserByUUID(final UUID uuid) {
        if(Bukkit.isPrimaryThread()) {
            RekusLogger.warn("Getting user data on the main thread!!!");
        }

        return new QPlayerData().where().uuid.eq(uuid).findOne();
    }


    public void saveAllUsers() {
        RekusLogger.info("Saving all data to the database...");

        final Transaction transaction = db.beginTransaction();

        try {
            for (final PlayerData data : MCRekus.getInstance().getPlayerDataManager().getAllPlayerData()) {
                saveUser(data);
            }

            transaction.commit();
        } catch (final Exception e) {
            RekusLogger.error("Failed to save user data to the database! Error: " + e);
            transaction.rollback();
        } finally {
            transaction.end();
        }
    }

}
