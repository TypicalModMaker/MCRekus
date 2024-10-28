package dev.isnow.mcrekus.module.impl.customevents.placeholder;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.impl.customevents.CustomEventsModule;
import dev.isnow.mcrekus.module.impl.customevents.event.CustomEventManager;
import dev.isnow.mcrekus.util.DurationUtils;
import java.util.HashMap;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CustomEventsExtension extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "events";
    }

    @Override
    public @NotNull String getAuthor() {
        return "5170";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(final Player player, final String identifier) {
        final CustomEventsModule customEventsModule = MCRekus.getInstance().getModuleManager().getModuleByName("CustomEvents");
        final CustomEventManager customEventManager = customEventsModule.getCustomEventManager();

        if (identifier.equals("isactive")) {
            return customEventManager.getCurrentEvent() != null ? "true" : "false";
        } else if (identifier.startsWith("place")) {
            final String[] split = identifier.split("_");

            if(split.length == 2) {
                final int place = Integer.parseInt(split[1]);

                final HashMap<Player, Long> score = customEventManager.getScore();

                return score.entrySet().stream()
                        .filter(entry -> entry.getValue() > 0)
                        .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                        .skip(place - 1)
                        .findFirst()
                        .map(entry -> entry.getKey().getName())
                        .orElse(customEventsModule.getConfig().getPlayerNotFound());
            } else if (split.length == 3 && split[2].equals("value")) {
                final int place = Integer.parseInt(split[1]);

                final HashMap<Player, Long> score = customEventManager.getScore();

                return score.entrySet().stream()
                        .filter(entry -> entry.getValue() > 0)
                        .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                        .skip(place - 1)
                        .findFirst()
                        .map(entry -> entry.getValue().toString())
                        .orElse(customEventsModule.getConfig().getScoreNotFound());
            }
        } else if (identifier.equals("name")) {
            return customEventManager.getCurrentEvent().getDisplayName();
        } else if (identifier.equals("timeleft")) {
            final long duration = customEventsModule.getConfig().getTimeLength() * 60L - ((System.currentTimeMillis() / 1000L) - customEventManager.getStartTime());

            if (duration < 0) {
                customEventManager.stopEvent();
            }

            return DurationUtils.formatRemaining(duration);
        }
        return null;
    }

}
