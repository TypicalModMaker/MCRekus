package dev.isnow.mcrekus.module.impl.essentials.minecart.codec;

@FunctionalInterface
public interface StreamEncoder<O, T> {

    void encode(O o0, T t0);
}
