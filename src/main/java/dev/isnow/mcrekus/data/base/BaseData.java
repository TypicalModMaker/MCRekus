package dev.isnow.mcrekus.data.base;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.database.DatabaseManager;
import dev.isnow.mcrekus.util.ExpiringSession;
import jakarta.persistence.Cacheable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import lombok.Getter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@MappedSuperclass
@Getter
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public abstract class BaseData implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public void save() {
        final DatabaseManager databaseManager = MCRekus.getInstance().getDatabaseManager();

        save(databaseManager.getDatabase().openSession());
    }

    public void save(final ExpiringSession expiringSession) {
        final DatabaseManager databaseManager = MCRekus.getInstance().getDatabaseManager();

        databaseManager.getDatabase().executeTransaction((session, transaction) -> session.merge(this), expiringSession);
    }

    public void delete() {
        final DatabaseManager databaseManager = MCRekus.getInstance().getDatabaseManager();

        delete(databaseManager.getDatabase().openSession());
    }

    public void delete(final ExpiringSession expiringSession) {
        final DatabaseManager databaseManager = MCRekus.getInstance().getDatabaseManager();

        databaseManager.getDatabase().executeTransaction((session, transaction) -> session.remove(this), expiringSession);
    }
}
