package dev.isnow.mcrekus.util.serializer.database;

import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.bukkit.Bukkit;
import org.bukkit.World;

@Converter
public final class RekusLocationSerializer implements AttributeConverter<RekusLocation, String> {

    @Override
    public String convertToDatabaseColumn(final RekusLocation rekusLocation) {
        if(rekusLocation == null) {
            return "";
        }

        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(rekusLocation.getWorld()).append(";")
                .append(rekusLocation.getX()).append(";")
                .append(rekusLocation.getY()).append(";")
                .append(rekusLocation.getZ()).append(";")
                .append(rekusLocation.getPitch()).append(";")
                .append(rekusLocation.getYaw()).append(";");

        return stringBuilder.toString();
    }

    @Override
    public RekusLocation convertToEntityAttribute(final String s) {
        if(s.isEmpty()) {
            return null;
        }

        final String[] split = s.split(";");
        final World world = Bukkit.getWorld(split[0]);
        final double x = Double.parseDouble(split[1]);
        final double y = Double.parseDouble(split[2]);
        final double z = Double.parseDouble(split[3]);
        final float pitch = Float.parseFloat(split[4]);
        final float yaw = Float.parseFloat(split[5]);

        return new RekusLocation(world != null ? world : Bukkit.getWorlds().getFirst(), x, y, z, pitch, yaw);
    }


}