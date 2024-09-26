package dev.isnow.mcrekus.util.cuboid;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@Getter
public class Cuboid {

    private int xMin;
    private int xMax;

    private int yMin;
    private int yMax;

    private int zMin;
    private int zMax;

    private final World world;

    public Cuboid(final Location point1, final Location point2) {
        this.xMin = Math.min(point1.getBlockX(), point2.getBlockX());
        this.xMax = Math.max(point1.getBlockX(), point2.getBlockX());
        this.yMin = Math.min(point1.getBlockY(), point2.getBlockY());
        this.yMax = Math.max(point1.getBlockY(), point2.getBlockY());
        this.zMin = Math.min(point1.getBlockZ(), point2.getBlockZ());
        this.zMax = Math.max(point1.getBlockZ(), point2.getBlockZ());

        this.world = point1.getWorld();
    }

    private boolean contains(World world, int x, int y, int z) {
        return world.getName().equals(this.world.getName()) && x >= xMin && x <= xMax && y >= yMin && y <= yMax && z >= zMin && z <= zMax;
    }

    private boolean contains(Location loc) {
        return contains(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public boolean isIn(final Player player) {
        return contains(player.getLocation());
    }

    public boolean isIn(final Block block) {
        return contains(block.getLocation());
    }

    public boolean isIn(final Location loc) {
        return contains(loc);
    }

    public boolean isIn(final Entity entity) {
        return contains(entity.getLocation());
    }

    public void setFirst(final Location loc) {
        this.xMin = loc.getBlockX();
        this.yMin = loc.getBlockY();
        this.zMin = loc.getBlockZ();
    }

    public void setSecond(final Location loc) {
        this.xMax = loc.getBlockX();
        this.yMax = loc.getBlockY();
        this.zMax = loc.getBlockZ();
    }
}