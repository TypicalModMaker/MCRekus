package dev.isnow.mcrekus.module.impl.customevents.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.impl.customevents.CustomEventsModule;
import dev.isnow.mcrekus.util.RekusLogger;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

@Getter@Setter
public abstract class CustomEvent implements ICustomEvent {
    private final List<Listener> eventListeners = new ArrayList<>();
    private final String name;
    private final String displayName;
    private final String description;

    public CustomEvent(final String name, final String displayName, final String description, final Class<?>... eventClasses) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;

        for(final Class<?> eventClass : eventClasses) {
            if(!Listener.class.isAssignableFrom(eventClass)) {
                throw new IllegalArgumentException("Event class must implement Listener");
            }

            try {
                eventListeners.add((Listener) eventClass.getDeclaredConstructor(CustomEvent.class).newInstance(this));
            } catch (final Exception e) {
                RekusLogger.error("Failed to create listener for event " + name);
                e.printStackTrace();
            }
        }
    }

    public void start() {
        for(final Listener listener : eventListeners) {
            Bukkit.getPluginManager().registerEvents(listener, MCRekus.getInstance());
        }

        onStart();
    }

    public void stop() {
        for(final Listener listener : eventListeners) {
            HandlerList.unregisterAll(listener);
        }

        onStop();
    }

    public void addScore(final Player player, final long score) {
        final CustomEventManager customEventManager = getModule().getCustomEventManager();

        if (customEventManager.getScore().containsKey(player)) {
            customEventManager.getScore().compute(player, (k, currentScore) -> currentScore + score);
        } else {
            customEventManager.getScore().put(player, score);
        }
    }

    public void addScore(final Player player) {
        addScore(player, 1);
    }

    public long getScore(final Player player) {
        return getModule().getCustomEventManager().getScore().getOrDefault(player, 0L);
    }

    public void removeScore(final Player player, final long score) {
        final CustomEventManager customEventManager = getModule().getCustomEventManager();

        if (customEventManager.getScore().containsKey(player)) {
            customEventManager.getScore().compute(player, (k, currentScore) -> currentScore - score);
        } else {
            customEventManager.getScore().put(player, 0L);
        }
    }

    public void removeScore(final Player player) {
        removeScore(player, 1);
    }

    private CustomEventsModule getModule() {
        return MCRekus.getInstance().getModuleManager().getModuleByClass(CustomEventsModule.class);
    }
}