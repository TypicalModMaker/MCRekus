package dev.isnow.mcrekus.util.serializer.database;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.impl.essentials.home.Home;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.World;

@Converter
public class HomeSerializer implements AttributeConverter<HashMap<String, Home>, String> {
    @Override
    public String convertToDatabaseColumn(final HashMap<String, Home> locations) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, Home> entry : locations.entrySet()) {
            final Home value = entry.getValue();
            RekusLogger.info("Serializing home: " + value.getName());

            final RekusLocation location = value.getLocation();

            stringBuilder.append(value.getName()).append(";")
                    .append(location.getWorld()).append(";")
                    .append(location.getX()).append(";")
                    .append(location.getY()).append(";")
                    .append(location.getZ()).append(";")
                    .append(location.getPitch()).append(";")
                    .append(location.getYaw()).append(";");
        }

        return stringBuilder.toString();
    }


    @Override
    public HashMap<String, Home> convertToEntityAttribute(final String s) {
        final HashMap<String, Home> locations = new HashMap<>();

        if(s.isEmpty()) {
            return locations;
        }

        final String[] split = s.split(";");

        for (int i = 0; i < split.length; i += 7) {
            final String name = split[i];

            RekusLogger.info("Deserializing home: " + name);

            final World world = Bukkit.getWorld(split[i + 1]);
            final double x = Double.parseDouble(split[i + 2]);
            final double y = Double.parseDouble(split[i + 3]);
            final double z = Double.parseDouble(split[i + 4]);
            final float pitch = Float.parseFloat(split[i + 5]);
            final float yaw = Float.parseFloat(split[i + 6]);

            locations.put(name, new Home(name, new RekusLocation(world, x, y, z, pitch, yaw)));
        }

        return locations;
    }
}
