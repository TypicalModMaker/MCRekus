package dev.isnow.mcrekus.module.impl.essentials.minecart.codec;

@FunctionalInterface
public interface StreamDecoder<I, T> {

    T decode(I i0);
}
