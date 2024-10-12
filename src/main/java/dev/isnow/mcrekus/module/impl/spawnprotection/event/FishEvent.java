package dev.isnow.mcrekus.module.impl.spawnprotection.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawnprotection.SpawnProtectionModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishEvent extends ModuleAccessor<SpawnProtectionModule> implements Listener {

    @EventHandler
    public void onRod(final PlayerFishEvent event) {
        if (event.getCaught() == null) return;

        final Player player = event.getPlayer();

        if (player.hasPermission("mcrekus.spawnprotection")) return;

        if (!(event.getCaught() instanceof Player)) return;

        if (!getModule().getSpawnCuboid().isIn(event.getCaught())) return;

        event.setCancelled(true);
        player.sendMessage(ComponentUtil.deserialize(getModule().getConfig().getSpawnHookPlayerMessage()));
    }
}
