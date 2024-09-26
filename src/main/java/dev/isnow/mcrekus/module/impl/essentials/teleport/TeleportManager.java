package dev.isnow.mcrekus.module.impl.essentials.teleport;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.scheduler.BukkitTask;


public class TeleportManager {
    private final HashMap<UUID, UUID> tpaRequests = new HashMap<>();
    private final HashMap<UUID, BukkitTask> tpaRequestsTasks = new HashMap<>();
    private final HashMap<UUID, BukkitTask> playersTeleporting = new HashMap<>();

    public final void addTpaRequest(final UUID sender, final UUID target) {
        tpaRequests.put(target, sender);
    }

    public final void removeTpaRequest(final UUID sender) {
        tpaRequests.remove(sender);
    }

    public final UUID getTpaRequest(final UUID sender) {
        return tpaRequests.get(sender);
    }

    public final boolean hasTpaRequest(final UUID sender) {
        return tpaRequests.containsKey(sender);
    }

    public int getTaskId(final UUID uuid) {
        return playersTeleporting.get(uuid).getTaskId();
    }

    public final void addPlayerTeleporting(final UUID uuid, final BukkitTask task) {
        playersTeleporting.put(uuid, task);
    }

    public final void removePlayerTeleporting(final UUID uuid) {
        playersTeleporting.remove(uuid);
    }

    public final boolean isPlayerTeleporting(final UUID uuid) {
        return playersTeleporting.containsKey(uuid);
    }

    public final void addTpaRequestTask(final UUID sender, final BukkitTask task) {
        tpaRequestsTasks.put(sender, task);
    }

    public final void removeTpaRequestTask(final UUID sender) {
        tpaRequestsTasks.remove(sender);
    }

    public final int getTpaRequestTaskId(final UUID sender) {
        final BukkitTask task = tpaRequestsTasks.get(sender);
        if(task != null) {
            return task.getTaskId();
        } else {
            return -1;
        }
    }

}
