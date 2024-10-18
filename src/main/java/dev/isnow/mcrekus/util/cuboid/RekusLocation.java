package dev.isnow.mcrekus.util.cuboid;

import lombok.Data;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@Data
@ToString
public class RekusLocation {
    public final String world;
    public final double x, y, z;
    public final float pitch, yaw;

    public RekusLocation(World world, double x, double y, double z, float pitch, float yaw) {
        this.world = world.getName();
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public RekusLocation(World world, double x, double y, double z) {
        this(world, x, y, z, 0, 0);
    }

    public RekusLocation(final Location loc) {
        this(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
    }

    public Location toBukkitLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public static RekusLocation fromBukkitLocation(Location loc) {
        return new RekusLocation(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
    }

    public static RekusLocation fromBukkitLocationTrimmed(Location loc) {
        return new RekusLocation(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 0, 0);
    }
}
