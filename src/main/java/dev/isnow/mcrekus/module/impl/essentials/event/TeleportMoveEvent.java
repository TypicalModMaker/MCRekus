package dev.isnow.mcrekus.module.impl.essentials.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.teleport.TeleportManager;
import dev.isnow.mcrekus.util.ComponentUtil;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class TeleportMoveEvent extends ModuleAccessor<EssentialsModule> implements Listener {

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            final TeleportManager teleportManager = getModule().getTeleportManager();
            final Player player = event.getPlayer();
            final UUID uuid = player.getUniqueId();

            if(!teleportManager.isPlayerTeleporting(uuid)) return;

            final int taskId = teleportManager.getTaskId(uuid);

            Bukkit.getScheduler().cancelTask(taskId);
            teleportManager.removePlayerTeleporting(uuid);

            player.sendMessage(ComponentUtil.deserialize(getModule().getConfig().getTpaMovedMessage()));
        }
    }
}
