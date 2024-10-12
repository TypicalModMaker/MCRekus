package dev.isnow.mcrekus.module.impl.spawners.event;

import static org.apache.commons.lang3.RandomUtils.nextInt;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawners.SpawnersModule;
import dev.isnow.mcrekus.module.impl.spawners.config.SpawnersConfig;
import dev.isnow.mcrekus.module.impl.spawners.progress.ProgressTracker;
import dev.isnow.mcrekus.module.impl.spawners.spawners.RekusSpawner;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import java.time.Duration;
import java.util.Random;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class InteractEvent extends ModuleAccessor<SpawnersModule> implements Listener {

    @EventHandler
    public void onPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();

        if (!getModule().hasProgressTracker(player)) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Action action = event.getAction();
        final Player player = event.getPlayer();

        if (event.getHand() != EquipmentSlot.HAND) return;

        if (action != Action.RIGHT_CLICK_BLOCK) return;

        final Block block = event.getClickedBlock();

        if (block == null) return;

        if (block.getType() != Material.SPAWNER) return;

        final Location location = block.getLocation();

        final RekusLocation spawnerLocation = RekusLocation.fromBukkitLocation(location);

        final RekusSpawner spawner = getModule().getSpawners().get(spawnerLocation);

        if (!spawner.isBroken()) return;

        final String cooldown = spawner.getCooldowns().isOnCooldown(player.getUniqueId());

        final SpawnersConfig config = getModule().getConfig();

        if (!cooldown.equals("-1")) {
            player.sendMessage(ComponentUtil.deserialize(config.getCooldownMessage().replaceAll("%time%", cooldown)));
            return;
        }

        if (spawner.getRepairingPlayer() == null) {
            if(getModule().hasProgressTracker(player)) {
                player.sendMessage(ComponentUtil.deserialize(config.getRepairingDifferentSpawnerMessage()));
                return;
            }

            final ProgressTracker progressTracker = new ProgressTracker(player);
            progressTracker.setup();
            getModule().addProgressTracker(player, progressTracker);
            spawner.setRepairingPlayer(player);
            return;
        } else if (spawner.getRepairingPlayer() != player) {
            player.sendMessage(ComponentUtil.deserialize(config.getBeingRepairedByAnotherPlayerMessage()));
            return;
        }

        final ProgressTracker progressTracker = getModule().getProgressTracker(player);

        if (progressTracker == null) return;

        if (progressTracker.getPosition() != progressTracker.getTargetPosition()) {
            player.playSound(player, Sound.ENTITY_CAT_HISS, 1F, 0.8F);
            progressTracker.setProgress(progressTracker.getProgress() - 1);
            if(progressTracker.getProgress() == -1) {
                progressTracker.getTask().cancel();
                getModule().removeProgressTracker(player);

                spawner.addCooldown(player.getUniqueId());
                player.sendMessage(ComponentUtil.deserialize(config.getFailedToRepairMessage()));
            } else {
                String randomMessage = ProgressTracker.SPAWNER_MESSAGES.get(nextInt(0, ProgressTracker.SPAWNER_MESSAGES.size()));
                while (randomMessage.equals(progressTracker.getLastFailedMessage())) {
                    randomMessage = ProgressTracker.SPAWNER_MESSAGES.get(nextInt(0, ProgressTracker.SPAWNER_MESSAGES.size()));
                }

                progressTracker.setLastFailedMessage(randomMessage);

                player.sendMessage(ComponentUtil.deserialize("&c" + randomMessage));
            }
            progressTracker.showBar();

            return;
        }

        progressTracker.setProgress(progressTracker.getProgress() + 1);
        if (progressTracker.getProgress() == 4) {
            progressTracker.getTask().cancel();
            progressTracker.showBar();
            getModule().removeProgressTracker(player);


            spawner.repairSpawner();

            player.resetTitle();
            player.showTitle(Title.title(
                    ComponentUtil.deserialize("&#00FF45&lN&#00FC43&la&#00F941&lp&#00F73F&lr&#00F43D&la&#00F13A&lw&#00EE38&li&#00EC36&lł&#00E934&le&#00E632&lś &#00E02E&ls&#00DE2C&lp&#00DB2A&la&#00D827&lw&#00D525&ln&#00D323&le&#00D021&lr&#00CD1F&l!"),
                    ComponentUtil.deserialize(""),
                    Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(3), Duration.ofMillis(500))
            ));

            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.5f);
            Bukkit.getScheduler().runTaskLater(MCRekus.getInstance(), () ->
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.7f), 10L);
            Bukkit.getScheduler().runTaskLater(MCRekus.getInstance(), () ->
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2.0f), 20L);
            Bukkit.getScheduler().runTaskLater(MCRekus.getInstance(), () ->
                    player.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 1, 0.9f), 20L);
        } else {
            final int baseLineLength = ProgressTracker.BASE_LINE.length();

            int randomPosition = new Random().nextInt(baseLineLength);
            while (randomPosition == progressTracker.getTargetPosition()) {
                randomPosition = new Random().nextInt(baseLineLength);
            }

            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1.3F);

            progressTracker.setTargetPosition(randomPosition);
            progressTracker.showTitle();
            progressTracker.showBar();
        }
    }
}
