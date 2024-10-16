package dev.isnow.mcrekus.database;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.config.impl.GeneralConfig;
import dev.isnow.mcrekus.config.impl.database.DatabaseConfig;
import dev.isnow.mcrekus.config.impl.database.DatabaseTypeConfig;
import dev.isnow.mcrekus.util.ExpiringSession;
import dev.isnow.mcrekus.util.ReflectionUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.stat.Statistics;

@Data
public class Database {
    private final SessionFactory sessionFactory;

    public Database(final GeneralConfig mainConfig, final DatabaseConfig authConfig) {
        this.sessionFactory = initializeSessionFactory(mainConfig, authConfig);
    }

    private SessionFactory initializeSessionFactory(final GeneralConfig mainConfig, final DatabaseConfig authConfig) {
        try {
            return configureHibernate(mainConfig, authConfig);
        } catch (Exception e) {
            RekusLogger.error("Failed to initialize Hibernate session factory: " + e.getMessage());
            return null;
        }
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
            enableHibernateDebug(configuration);
        }

        configuration.setProperty("hibernate.hbm2ddl.auto", "update");

        try {
            for(final Class<?> clazz : ReflectionUtil.getClasses("dev.isnow.mcrekus.data")) {
                configuration.addAnnotatedClass(clazz);
            }
        } catch (final Exception e) {
            RekusLogger.error("Failed to load data classes: " + e.getMessage());
        }

        final ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        Thread.currentThread().setContextClassLoader(MCRekus.class.getClassLoader());
        return configuration.buildSessionFactory(serviceRegistry);
    }

    private void enableHibernateDebug(Configuration configuration) {
        RekusLogger.debug("Enabling Hibernate debug mode.");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.format_sql", "true");
        configuration.setProperty("hibernate.use_sql_comments", "true");
        configuration.setProperty("hibernate.generate_statistics", "true");
    }

    private String getUrl(final DatabaseConfig authConfig) {
        return switch (authConfig.getDatabaseType()) {
            case MYSQL, MARIADB -> authConfig.getDatabaseType().getPrefix() + authConfig.getHost() + "/" + authConfig.getDatabase();
            case H2 -> authConfig.getDatabaseType().getPrefix() + new File(MCRekus.getInstance().getDataFolder(), authConfig.getDatabase()).getAbsolutePath();
        };
    }

    private String getHibernateDialect(final DatabaseTypeConfig type) {
        return switch (type) {
            case MYSQL -> "org.hibernate.dialect.MySQLDialect";
            case MARIADB -> "org.hibernate.dialect.MariaDBDialect";
            case H2 -> "org.hibernate.dialect.H2Dialect";
        };
    }

    private String getDriverClass(final DatabaseTypeConfig type) {
        return switch (type) {
            case MYSQL -> "com.mysql.cj.jdbc.Driver";
            case MARIADB -> "org.mariadb.jdbc.Driver";
            case H2 -> "org.h2.Driver";
        };
    }

    public void executeTransaction(final BiConsumer<Session, Transaction> action,
            final ExpiringSession expiringSession) {
        if (expiringSession.isOpen()) {
            Transaction tx = null;
            try {
                tx = expiringSession.getSession().beginTransaction();
                action.accept(expiringSession.getSession(), tx);
                tx.commit();
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                RekusLogger.error("Transaction failed: " + e.getMessage());
            } finally {
                expiringSession.closeSession();
            }
        }
    }

    public void shutdown() {
        RekusLogger.info("Shutting down the database connection.");

        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public boolean isConnected() {
        return sessionFactory != null && sessionFactory.isOpen();
    }

    public ExpiringSession openSession() {
        return new ExpiringSession(sessionFactory.openSession());
    }

    public Statistics getStatistics() {
        return sessionFactory.getStatistics();
    }

    public <T> void fetchEntityAsync(final String query, final String param, final Object value, final Class<T> clazz, final BiConsumer<ExpiringSession, T> callback) {
        CompletableFuture.runAsync(() -> {
            try (ExpiringSession session = openSession()) {
                T entity = session.getSession().createQuery(query, clazz)
                        .setParameter(param, value)
                        .setCacheable(true)
                        .uniqueResult();
                callback.accept(session, entity);
            } catch (Exception e) {
                RekusLogger.error("Failed to fetch entity: " + e.getMessage());
                callback.accept(null, null);
            }
        }, MCRekus.getInstance().getThreadPool());
    }

}
