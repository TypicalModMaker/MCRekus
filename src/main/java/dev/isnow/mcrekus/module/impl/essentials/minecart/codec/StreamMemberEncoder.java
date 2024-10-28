package dev.isnow.mcrekus.module.impl.essentials.minecart.codec;

@FunctionalInterface
public interface StreamMemberEncoder<O, T> {

    void encode(T t0, O o0);
}
