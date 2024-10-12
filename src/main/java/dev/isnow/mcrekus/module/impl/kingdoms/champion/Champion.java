package dev.isnow.mcrekus.module.impl.kingdoms.champion;

import java.util.Comparator;
import java.util.List;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@Data
public class Champion {
    final List<Player> attackingPlayers;
    final Creature creature;
    final Location startingLocation;
    final BukkitTask championTargetTask;

    BukkitTask teleportationTask;

    public void teleportChampion() {
        final Block block = startingLocation.getChunk().getBlock(8, 0, 8);
        final int topY = startingLocation.getWorld().getHighestBlockYAt(block.getX(), block.getZ());

        final Location topLocation = block.getLocation().clone().add(0.5, topY + 1, 0.5);;

        creature.teleport(topLocation);

        final List<Player> entities = attackingPlayers.stream()
                .sorted(Comparator.comparingDouble(o -> creature.getLocation().distance(o.getLocation())))
                .toList();

        if(entities.isEmpty()) {
            return;
        }

        final Player nearestPlayer = entities.getFirst();

        creature.setTarget(nearestPlayer);
    }
}
