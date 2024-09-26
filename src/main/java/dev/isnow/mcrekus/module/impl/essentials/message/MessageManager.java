package dev.isnow.mcrekus.module.impl.essentials.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MessageManager {
    private final HashMap<UUID, UUID> lastMessage = new HashMap<>();
    private final HashMap<UUID, ArrayList<UUID>> ignoredPlayers = new HashMap<>();

    private final ArrayList<UUID> messageToggle = new ArrayList<>();

    public final void setLastMessage(final UUID sender, final UUID receiver) {
        lastMessage.put(sender, receiver);
    }

    public final UUID getLastMessage(final UUID sender) {
        return lastMessage.get(sender);
    }

    public final void removeLastMessage(final UUID sender) {
        lastMessage.remove(sender);
    }

    public final boolean hasLastMessage(final UUID sender) {
        return lastMessage.containsKey(sender);
    }

    public final void addIgnoredPlayer(final UUID player, final UUID ignored) {
        if(!ignoredPlayers.containsKey(player)) {
            ignoredPlayers.put(player, new ArrayList<>());
        }

        ignoredPlayers.get(player).add(ignored);
    }

    public final void removeIgnoredPlayer(final UUID player, final UUID ignored) {
        if(!ignoredPlayers.containsKey(player)) {
            return;
        }

        ignoredPlayers.get(player).remove(ignored);
    }

    public final boolean isIgnored(final UUID player, final UUID ignored) {
        if(!ignoredPlayers.containsKey(player)) {
            return false;
        }

        return ignoredPlayers.get(player).contains(ignored);
    }

    public final void disableMessages(final UUID player) {
        messageToggle.add(player);
    }

    public final void enableMessages(final UUID player) {
        messageToggle.remove(player);
    }

    public final boolean isMessagesEnabled(final UUID player) {
        return !messageToggle.contains(player);
    }

}
