package dev.isnow.mcrekus.module.impl.spawn.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawn.SpawnModule;
import dev.isnow.mcrekus.module.impl.spawn.teleport.SpawnTeleportManager;
import dev.isnow.mcrekus.util.ComponentUtil;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpawnMoveEvent extends ModuleAccessor<SpawnModule> implements Listener {

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            final SpawnTeleportManager spawnTeleportManager = getModule().getSpawnTeleportManager();
            final Player player = event.getPlayer();
            final UUID uuid = player.getUniqueId();

            if(!spawnTeleportManager.isPlayerTeleporting(uuid)) return;

            final int taskId = spawnTeleportManager.getTaskId(uuid);

            Bukkit.getScheduler().cancelTask(taskId);
            spawnTeleportManager.removePlayerTeleporting(uuid);

            player.sendMessage(ComponentUtil.deserialize(getModule().getConfig().getSpawnOnPlayerMoveMessage()));
        }
    }
}
