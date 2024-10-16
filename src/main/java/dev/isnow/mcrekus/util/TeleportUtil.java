package dev.isnow.mcrekus.util;

import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public class TeleportUtil {
    public void teleportPlayers(final Player executor, final Player target, final Player destination, final EssentialsConfig config) {
        if (target == null && executor != null) {
            executor.sendMessage(ComponentUtil.deserialize(config.getTeleportPlayerNotFoundMessage(), null));
            return;
        }

        target.teleport(destination);

        if (executor == null) {
            return;
        }

        if (executor == target) {
            executor.sendMessage(ComponentUtil.deserialize(config.getTeleportToPlayerMessage(), null, "%player%", destination.getName()));
        } else {
            executor.sendMessage(ComponentUtil.deserialize(config.getTeleportPlayerToPlayerMessage(), null, "%player%", target.getName(), "%destination%", destination.getName()));
        }

        executor.playSound(executor.getLocation(), config.getTeleportSuccessSound(), 1.0F, 1.0F);
    }
}
