package dev.isnow.mcrekus.module.impl.essentials.minecart.codec;

import io.netty.buffer.ByteBuf;
import java.util.function.Function;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;

public class RegistryFriendlyByteBuf extends FriendlyByteBuf {

    private final RegistryAccess registryAccess;

    public RegistryFriendlyByteBuf(ByteBuf bytebuf, RegistryAccess registryaccess) {
        super(bytebuf);
        this.registryAccess = registryaccess;
    }

    public RegistryAccess registryAccess() {
        return this.registryAccess;
    }

    public static Function<ByteBuf, RegistryFriendlyByteBuf> decorator(RegistryAccess registryaccess) {
        return (bytebuf) -> {
            return new RegistryFriendlyByteBuf(bytebuf, registryaccess);
        };
    }
}
