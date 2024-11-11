package dev.isnow.mcrekus.module.impl.essentials.minecart.codec;

import io.netty.buffer.ByteBuf;

public interface ByteBufCodecs {

    StreamCodec<ByteBuf, Byte> BYTE = new StreamCodec<ByteBuf, Byte>() {
        public Byte decode(ByteBuf bytebuf) {
            return bytebuf.readByte();
        }

        public void encode(ByteBuf bytebuf, Byte obyte) {
            bytebuf.writeByte(obyte);
        }
    };
    StreamCodec<ByteBuf, Float> FLOAT = new StreamCodec<ByteBuf, Float>() {
        public Float decode(ByteBuf bytebuf) {
            return bytebuf.readFloat();
        }

        public void encode(ByteBuf bytebuf, Float ofloat) {
            bytebuf.writeFloat(ofloat);
        }
    };

}