package dev.isnow.mcrekus.module.impl.spawn.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawn.SpawnModule;
import dev.isnow.mcrekus.module.impl.spawn.config.SpawnConfig;
import dev.isnow.mcrekus.module.impl.spawn.teleport.SpawnTeleportManager;
import dev.isnow.mcrekus.util.ComponentUtil;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@CommandAlias("spawn")
@Description("Command to teleport to spawn")
@CommandPermission("mcrekus.spawn")
@SuppressWarnings("unused")
public class SpawnCommand extends BaseCommand {

    private final ModuleAccessor<SpawnModule> moduleAccessor = new ModuleAccessor<>(SpawnModule.class);

    @Default
    public void execute(Player player, String[] args) {
        final SpawnTeleportManager spawnTeleportManager = moduleAccessor.getModule().getSpawnTeleportManager();
        final SpawnConfig spawnConfig = moduleAccessor.getModule().getConfig();
        final UUID uuid = player.getUniqueId();

        final String cooldown = spawnTeleportManager.getCooldown().isOnCooldown(uuid);

        if(!player.hasPermission(spawnConfig.getSpawnCooldownBypassPermission()) && !cooldown.equals("-1")) {
            player.sendMessage(ComponentUtil.deserialize(spawnConfig.getSpawnOnCooldownMessage(), null, "%time%", cooldown));
            return;
        }

        if(!player.hasPermission(spawnConfig.getSpawnCooldownBypassPermission())) {
            player.sendMessage(ComponentUtil.deserialize(spawnConfig.getSpawnDelayMessage(), null, "%time%", spawnConfig.getSpawnTeleportDelay()));
            spawnTeleportManager.getCooldown().addCooldown(player.getUniqueId());

            BukkitTask bukkitRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    spawnTeleportManager.removePlayerTeleporting(uuid);
                    teleportPlayer(player, spawnTeleportManager, spawnConfig);
                }
            }.runTaskLater(MCRekus.getInstance(), spawnConfig.getSpawnTeleportDelay() * 20L);

            spawnTeleportManager.addPlayerTeleporting(uuid, bukkitRunnable);
        } else {
            teleportPlayer(player, spawnTeleportManager, spawnConfig);
        }
    }

    private void teleportPlayer(final Player player, final SpawnTeleportManager spawnTeleportManager, final SpawnConfig spawnConfig) {
        if(!player.isOnline()) {
            return;
        }

        final Location spawnLocation = spawnConfig.getSpawnLocation().toBukkitLocation();

        player.teleport(spawnLocation);
        player.sendMessage(ComponentUtil.deserialize(spawnConfig.getSpawnTeleportedMessage()));
        if(spawnConfig.getSpawnEffect() != null) {
            player.getWorld().playEffect(spawnLocation.clone().add(0, 0.2, 0), spawnConfig.getSpawnEffect(), 0);
        }

        if(spawnConfig.getSpawnSound() != null) {
            player.playSound(spawnLocation, spawnConfig.getSpawnSound(), 1, 1);
        }
    }
}
