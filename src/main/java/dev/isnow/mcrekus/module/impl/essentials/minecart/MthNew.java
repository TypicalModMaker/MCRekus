package dev.isnow.mcrekus.module.impl.essentials.minecart;

import net.minecraft.world.phys.Vec3;

public class MthNew {

    public static double lerp(double d0, double d1, double d2) {
        return d1 + d0 * (d2 - d1);
    }

    public static Vec3 lerp(double d0, Vec3 vec3, Vec3 vec31) {
        return new Vec3(lerp(d0, vec3.x, vec31.x), lerp(d0, vec3.y, vec31.y), lerp(d0, vec3.z, vec31.z));
    }
}
