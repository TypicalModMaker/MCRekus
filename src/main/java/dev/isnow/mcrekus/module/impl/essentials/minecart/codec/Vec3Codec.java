package dev.isnow.mcrekus.module.impl.essentials.minecart.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class Vec3Codec {

    public static final StreamCodec<ByteBuf, Vec3> STREAM_CODEC = new StreamCodec<ByteBuf, Vec3>() {
        public Vec3 decode(ByteBuf bytebuf) {
            return readVec3(bytebuf);
        }

        public void encode(ByteBuf bytebuf, Vec3 vec3) {
            writeVec3(bytebuf, vec3);
        }
    };

    public static Vec3 readVec3(ByteBuf bytebuf) {
        return new Vec3(bytebuf.readDouble(), bytebuf.readDouble(), bytebuf.readDouble());
    }

    public static void writeVec3(ByteBuf bytebuf, Vec3 vec3) {
        bytebuf.writeDouble(vec3.x());
        bytebuf.writeDouble(vec3.y());
        bytebuf.writeDouble(vec3.z());
    }
}
