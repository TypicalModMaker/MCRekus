package dev.isnow.mcrekus.module.impl.spawn.teleport;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawn.SpawnModule;
import dev.isnow.mcrekus.util.cooldown.Cooldown;
import java.util.HashMap;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitTask;

@Getter@Setter
public class SpawnTeleportManager extends ModuleAccessor<SpawnModule> {
    private final HashMap<UUID, BukkitTask> playersTeleporting = new HashMap<>();
    private final Cooldown<UUID> cooldown = new Cooldown<>(getModule().getConfig().getSpawnTeleportCooldown() * 1000L);

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
}
