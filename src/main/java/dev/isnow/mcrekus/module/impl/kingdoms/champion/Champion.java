package dev.isnow.mcrekus.module.impl.kingdoms.champion;

import java.util.List;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@Data
public class Champion {
    final List<Player> attackingPlayers;
    final LivingEntity creature;
    final Location startingLocation;
    final BukkitTask championTargetTask;

    BukkitTask teleportationTask;

    public void teleportChampion() {
        final Block block = startingLocation.getChunk().getBlock(8, 0, 8);
        final int topY = startingLocation.getWorld().getHighestBlockYAt(block.getX(), block.getZ());

        final Location topLocation = block.getLocation().clone().add(0.5, topY + 1, 0.5);;

        creature.teleport(topLocation);
    }
}
