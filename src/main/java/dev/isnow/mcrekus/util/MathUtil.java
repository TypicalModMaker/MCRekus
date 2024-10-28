package dev.isnow.mcrekus.util;

import com.github.retrooper.packetevents.util.Quaternion4f;
import lombok.experimental.UtilityClass;
import org.joml.Math;

@UtilityClass
public class MathUtil {
    public void rotateY(float angle, Quaternion4f dest) {
        float sin = org.joml.Math.sin(angle * 0.5F);
        float cos = Math.cosFromSin(sin, angle * 0.5F);

        dest.setX(dest.getX() * cos - dest.getZ() * sin);
        dest.setY(dest.getW() * sin + dest.getY() * cos);
        dest.setZ(dest.getX() * sin + dest.getZ() * cos);
        dest.setW(dest.getW() * cos - dest.getZ() * sin);
    }

}
