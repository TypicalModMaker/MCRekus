package dev.isnow.mcrekus.database;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.config.impl.GeneralConfig;
import dev.isnow.mcrekus.config.impl.database.DatabaseConfig;
import dev.isnow.mcrekus.data.HomeData;
import dev.isnow.mcrekus.data.PlayerData;
import dev.isnow.mcrekus.data.PumpkinData;
import dev.isnow.mcrekus.util.ExpiringSession;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import org.bukkit.OfflinePlayer;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public final class DatabaseManager {
    private final SessionFactory sessionFactory;

    public DatabaseManager() {
        SessionFactory sessionFactory;

        final DatabaseConfig authConfig = MCRekus.getInstance().getConfigManager().getDatabaseConfig();
        final GeneralConfig masterConfig = MCRekus.getInstance().getConfigManager().getGeneralConfig();

        try {
            sessionFactory = configureHibernate(masterConfig, authConfig);
        } catch (Exception e) {
            RekusLogger.error("Failed to initialize Hibernate session factory: " + e.getMessage());
            e.printStackTrace();
            sessionFactory = null;
        }

        this.sessionFactory = sessionFactory;
    }

    private SessionFactory configureHibernate(final GeneralConfig mainConfig, final DatabaseConfig authConfig) {
        final Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.dialect", getHibernateDialect(authConfig.getDatabaseType()));
        configuration.setProperty("hibernate.connection.driver_class", getDriverClass(authConfig.getDatabaseType()));
        configuration.setProperty("hibernate.connection.url", getUrl(authConfig));
        configuration.setProperty("hibernate.connection.username", authConfig.getUsername());
        configuration.setProperty("hibernate.connection.password", authConfig.getPassword());

        configuration.setProperty("hibernate.cache.use_second_level_cache", "true");
        configuration.setProperty("hibernate.cache.use_query_cache", "true");
        configuration.setProperty("hibernate.cache.region.factory_class", "jcache");
        configuration.setProperty("hibernate.javax.cache.provider", "com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider");

        if (mainConfig.isDebugMode()) {
            RekusLogger.debug("Enabling Hibernate debug mode.");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.format_sql", "true");
            configuration.setProperty("hibernate.use_sql_comments", "true");
            configuration.setProperty("hibernate.generate_statistics", "true");
        }

        configuration.setProperty("hibernate.hbm2ddl.auto", "update");

        configuration.addAnnotatedClass(PlayerData.class);
        configuration.addAnnotatedClass(HomeData.class);
        configuration.addAnnotatedClass(PumpkinData.class);

        final ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        Thread.currentThread().setContextClassLoader(MCRekus.class.getClassLoader());
        return configuration.buildSessionFactory(serviceRegistry);
    }

    private String getUrl(DatabaseConfig authConfig) {
        return switch (authConfig.getDatabaseType()) {
            case MYSQL -> authConfig.getDatabaseType().getPrefix() + authConfig.getHost() + "/"
                    + authConfig.getDatabase();
            case MARIADB -> authConfig.getDatabaseType().getPrefix() + authConfig.getHost() + "/"
                    + authConfig.getDatabase() + authConfig.getDatabaseType().getSuffix();
            case H2 -> authConfig.getDatabaseType().getPrefix() + new File(MCRekus.getInstance().getDataFolder(), authConfig.getDatabase()).getAbsolutePath();
            default -> throw new IllegalArgumentException();
        };
    }

    private String getHibernateDialect(final dev.isnow.mcrekus.config.impl.database.DatabaseType type) {
        return switch (type) {
            case MYSQL -> "org.hibernate.dialect.MySQLDialect";
            case MARIADB -> "org.hibernate.dialect.MariaDBDialect";
            case H2 -> "org.hibernate.dialect.H2Dialect";
            default -> throw new IllegalArgumentException();
        };
    }

    private String getDriverClass(final dev.isnow.mcrekus.config.impl.database.DatabaseType type) {
        return switch (type) {
            case MYSQL -> "com.mysql.cj.jdbc.Driver";
            case MARIADB -> "org.mariadb.jdbc.Driver";
            case H2 -> "org.h2.Driver";
            default -> throw new IllegalArgumentException();
        };
    }

    public void shutdown() {
        RekusLogger.info("Shutting down the database connection.");
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public void saveUser(final PlayerData user, final ExpiringSession expiringSession) {
        if (user == null) {
            RekusLogger.warn("Attempted to save null user.");
            return;
        }

        if (expiringSession.isOpen()) {
            Transaction tx = null;
            try {
                tx = expiringSession.getSession().beginTransaction();
                expiringSession.getSession().merge(user);
                tx.commit();
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                RekusLogger.error("Error saving user: " + e.getMessage());
            } finally {
                expiringSession.closeSession();
            }
        }
    }

    public void getUserAsync(final OfflinePlayer player, final BiConsumer<ExpiringSession, PlayerData> callback) {
        CompletableFuture.runAsync(() -> {
            final ExpiringSession session = new ExpiringSession(sessionFactory.openSession());

            PlayerData user = session.getSession().createQuery("FROM PlayerData WHERE uuid = :uuid", PlayerData.class)
                    .setParameter("uuid", player.getUniqueId())
                    .uniqueResult();

            callback.accept(session, user);
        }, MCRekus.getInstance().getThreadPool()).exceptionally(e -> {
            RekusLogger.error("Failed to get user " + player.getName() + ": " + e.getMessage());
            e.printStackTrace();
            callback.accept(null, null);
            return null;
        });
    }

    public void getUserAsync(final String playerName, final BiConsumer<ExpiringSession, PlayerData> callback) {
        CompletableFuture.runAsync(() -> {
            final ExpiringSession session = new ExpiringSession(sessionFactory.openSession());

            PlayerData user = session.getSession().createQuery("FROM PlayerData WHERE name = :player_name", PlayerData.class)
                    .setParameter("player_name", playerName).uniqueResult();

            callback.accept(session, user);
        }, MCRekus.getInstance().getThreadPool()).exceptionally(e -> {
            RekusLogger.error("Failed to get user " + playerName + ": " + e.getMessage());
            e.printStackTrace();
            callback.accept(null, null);
            return null;
        });
    }

    public void getPumpkinAsync(final RekusLocation pumpkinLocation, final BiConsumer<ExpiringSession, PumpkinData> callback) {
        CompletableFuture.runAsync(() -> {
            final ExpiringSession session = new ExpiringSession(sessionFactory.openSession());

            PumpkinData pumpkin = session.getSession().createQuery("FROM PumpkinData WHERE location = :location", PumpkinData.class)
                    .setParameter("location", pumpkinLocation).uniqueResult();

            callback.accept(session, pumpkin);
        }, MCRekus.getInstance().getThreadPool()).exceptionally(e -> {
            RekusLogger.error("Failed to get pumpkin at " + pumpkinLocation + ": " + e.getMessage());
            e.printStackTrace();
            callback.accept(null, null);
            return null;
        });
    }

    public void savePumpkin(final PumpkinData data, final ExpiringSession expiringSession) {
        if (data == null) {
            RekusLogger.warn("Attempted to save null data.");
            return;
        }

        if (expiringSession.isOpen()) {
            Transaction tx = null;
            try {
                tx = expiringSession.getSession().beginTransaction();
                expiringSession.getSession().merge(data);
                tx.commit();
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                RekusLogger.error("Error saving data: " + e.getMessage());
            } finally {
                expiringSession.closeSession();
            }
        }
    }

    public void deletePumpkin(final PumpkinData data, final ExpiringSession expiringSession) {
        if (data == null) {
            RekusLogger.warn("Attempted to delete null data.");
            return;
        }

        if (expiringSession.isOpen()) {
            Transaction tx = null;
            try {
                tx = expiringSession.getSession().beginTransaction();
                for(final PlayerData player : data.getCollectedPlayers()) {
                    player.getPumpkins().remove(data);
                    expiringSession.getSession().merge(player);
                }
                expiringSession.getSession().delete(data);
                tx.commit();
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                RekusLogger.error("Error deleting data: " + e.getMessage());
            } finally {
                expiringSession.closeSession();
            }
        }
    }

    public void saveAllUsers() {
        try (final Session session = sessionFactory.openSession()) {
            final Transaction tx = session.beginTransaction();
            session.createQuery("FROM PlayerData", PlayerData.class).list().forEach(session::saveOrUpdate);
            tx.commit();
        } catch (HibernateException e) {
            RekusLogger.error("Failed to save all users: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return sessionFactory != null && sessionFactory.isOpen();
    }

    public ExpiringSession openSession() {
        return new ExpiringSession(sessionFactory.openSession());
    }

    public ExpiringSession openSession(final int delay) {
        return new ExpiringSession(sessionFactory.openSession(), delay);
    }

    public long hitCount() {
        return sessionFactory.getStatistics().getSecondLevelCacheHitCount();
    }

    public long missCount() {
        return sessionFactory.getStatistics().getSecondLevelCacheMissCount();
    }

    public long putCount() {
        return sessionFactory.getStatistics().getSecondLevelCachePutCount();
    }
}
