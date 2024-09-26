package dev.isnow.mcrekus.module.impl.spawnprotection.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawnprotection.SpawnProtectionModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class EnderPearlEvent extends ModuleAccessor<SpawnProtectionModule> implements Listener {

    @EventHandler
    public void onEnderPearl(final ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() == null) return;

        if (!(event.getEntity() instanceof EnderPearl)) return;

        if (!(event.getEntity().getShooter() instanceof Player player)) return;

        if (player.hasPermission("mcrekus.spawnprotection")) return;

        if (!getModule().getSpawnCuboid().isIn(player)) return;

        event.setCancelled(true);
        player.sendMessage(ComponentUtil.deserialize(getModule().getConfig().getSpawnEnderPearlMessage()));
    }

}
