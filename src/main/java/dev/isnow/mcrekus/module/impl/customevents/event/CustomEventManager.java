package dev.isnow.mcrekus.module.impl.customevents.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.customevents.CustomEventsModule;
import dev.isnow.mcrekus.module.impl.customevents.config.CustomEventsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.ReflectionUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@Getter @Setter
public class CustomEventManager extends ModuleAccessor<CustomEventsModule> {
    private final HashMap<Player, Long> score = new HashMap<>();
    private final List<CustomEvent> events = new ArrayList<>();

    private CustomEvent currentEvent;
    private BukkitTask eventScheduler;
    private long startTime;

    public CustomEventManager() {
        super(CustomEventsModule.class);
    }

    public void setup() {
        try {
            for(final Class<?> clazz : ReflectionUtil.getClasses("dev.isnow.mcrekus.module.impl.customevents.event.impl")) {
                try {
                    if (CustomEvent.class.isAssignableFrom(clazz) && !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
                        events.add((CustomEvent) clazz.getDeclaredConstructor().newInstance());

                        RekusLogger.debug("Loaded event " + clazz.getSimpleName());
                    }
                } catch (final Exception e) {
                    RekusLogger.error("Failed to load event " + clazz.getSimpleName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (final Exception e) {
            RekusLogger.error("Failed to load data classes: " + e.getMessage());
            e.printStackTrace();
        }

        setupScheduler();
    }

    private void setupScheduler() {
        final CustomEventsConfig config = getModule().getConfig();

        eventScheduler = new BukkitRunnable() {
            @Override
            public void run() {
                if (isCancelled() || Bukkit.getOnlinePlayers().size() < config.getMinPlayers()) {
                    eventScheduler = null;
                    return;
                }

                startEvent();
            }
        }.runTaskLaterAsynchronously(MCRekus.getInstance(), new Random().nextLong(config.getMinDelay() * 20 * 60L, config.getMaxDelay() * 20 * 60L));
    }

    public void startEvent(final CustomEvent event) {
        eventScheduler.cancel();

        if (currentEvent != null) {
            RekusLogger.warn("Event already active, skipping.");
            return;
        }

        score.clear();

        final CustomEventsConfig config = getModule().getConfig();

        event.start();
        currentEvent = event;

        startTime = System.currentTimeMillis() / 1000L;

        for(final String message : config.getEventStartBroadcast()) {
            Bukkit.broadcast(ComponentUtil.deserialize(message, null, "%event%", event.getDisplayName(), "%time%", config.getTimeLength(), "%description%", event.getDescription()));
        }
    }

    public void startEvent() {
        final CustomEvent event = events.get((int) (Math.random() * events.size()));

        startEvent(event);
    }

    public void stopEvent() {
        if (currentEvent == null) {
            RekusLogger.warn("No active event to stop.");
            return;
        }

        final CustomEventsConfig config = getModule().getConfig();

        for(final Entry<Player, Long> playerEntry : score.entrySet().stream().sorted((a, b) -> b.getValue().compareTo(a.getValue())).limit(3).toList()) {
            final int playerCount = Bukkit.getOnlinePlayers().size();

            final double multiplier = (double) playerCount / config.getBaseDivider();

            final int baseReward = switch (score.size()) {
                case 1 -> config.getBaseReward();
                case 2 -> 4000;
                case 3 -> 3000;
                default -> 0;
            };

            final int reward = (int) (baseReward * multiplier);
            final Player player = playerEntry.getKey();

            RekusLogger.debug("Rewarding " + player.getName() + " with " + reward + " pesos. (Multiplier: " + multiplier + ", BaseReward: " + baseReward + ")");

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "money " + player.getName() + " pesos give " + reward);
        }



        final List<Player> topPlayers = score.entrySet().stream().sorted((a, b) -> b.getValue().compareTo(a.getValue())).limit(3).map(Entry::getKey).toList();

        currentEvent.stop();
        for(final String message : config.getEventEndBroadcast()) {
            Bukkit.broadcast(ComponentUtil.deserialize(message, null, "%event%", currentEvent.getDisplayName(), "%time%", config.getTimeLength(), "%description%", currentEvent.getDescription(), "%first%",
                    !topPlayers.isEmpty() ? topPlayers.get(0).getName() : config.getPlayerNotFound(), "%second%", topPlayers.size() > 1 ? topPlayers.get(1).getName() : config.getPlayerNotFound(), "%third%", topPlayers.size() > 2 ? topPlayers.get(2).getName() : config.getPlayerNotFound()));
        }

        currentEvent = null;

        if(eventScheduler != null && !eventScheduler.isCancelled()) {
            RekusLogger.warn("Event scheduler already running, skipping.");
            return;
        }

        setupScheduler();
    }

    public CustomEvent getEventByName(final String name) {
        return events.stream().filter(event -> event.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
