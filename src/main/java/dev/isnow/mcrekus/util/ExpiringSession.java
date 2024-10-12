package dev.isnow.mcrekus.util;

import dev.isnow.mcrekus.MCRekus;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.hibernate.Session;

@Getter
public final class ExpiringSession {
    private final Session session;
    private final int delay;
    private ScheduledFuture<?> scheduledFuture;

    public ExpiringSession(final Session session) {
        this.session = session;
        this.delay = 5;

        scheduleSessionClose();
    }

    public ExpiringSession(final Session session, final int delay) {
        this.session = session;
        this.delay = delay;

        scheduleSessionClose();
    }

    private void scheduleSessionClose() {
        scheduledFuture = MCRekus.getInstance().getScheduler().schedule(() -> {
            if (session.isOpen()) {
                session.close();
                RekusLogger.debug("Closed session due to expiration.");
            }
        }, delay, TimeUnit.SECONDS);
    }

    public void closeSession() {
        if (session.isOpen()) {
            session.close();

            scheduledFuture.cancel(true);
        }
    }

    public boolean isOpen() {
        return session.isOpen();
    }
}
