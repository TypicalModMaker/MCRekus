package dev.isnow.mcrekus.util.serializer.config;

import de.exlll.configlib.Serializer;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class RekusLocationSerializer implements Serializer<RekusLocation, Map<String, String>> {
    @Override
    public Map<String, String> serialize(RekusLocation rekusLocation) {
        final Map<String, String> map = new HashMap<>();

        map.put("world", rekusLocation.getWorld());

        map.put("x", String.valueOf(rekusLocation.getX()));
        map.put("y", String.valueOf(rekusLocation.getY()));
        map.put("z", String.valueOf(rekusLocation.getZ()));

        map.put("pitch", String.valueOf(rekusLocation.getPitch()));
        map.put("yaw", String.valueOf(rekusLocation.getYaw()));

        return map;
    }

    @Override
    public RekusLocation deserialize(Map<String, String> stringStringMap) {

        final World world = Bukkit.getWorld(stringStringMap.get("world"));

        final double x = Double.parseDouble(stringStringMap.get("x"));
        final double y = Double.parseDouble(stringStringMap.get("y"));
        final double z = Double.parseDouble(stringStringMap.get("z"));

        final float pitch = Float.parseFloat(stringStringMap.get("pitch"));
        final float yaw = Float.parseFloat(stringStringMap.get("yaw"));

        return new RekusLocation(world, x, y, z, pitch, yaw);
    }
}