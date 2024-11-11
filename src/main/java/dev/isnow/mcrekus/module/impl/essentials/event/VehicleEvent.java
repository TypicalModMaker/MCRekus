package dev.isnow.mcrekus.module.impl.essentials.event;

import com.mojang.serialization.Lifecycle;
import dev.isnow.mcrekus.module.impl.essentials.minecart.CustomMinecart;
import dev.isnow.mcrekus.module.impl.essentials.minecart.CustomMinecartAbstract;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.AbstractMinecart.Type;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntityTypes;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftMinecartRideable;
import org.bukkit.event.Listener;

public class VehicleEvent implements Listener {

    public void registerCart()
            throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        CraftServer server = ((CraftServer) Bukkit.getServer());
        DedicatedServer dedicatedServer = server.getServer();
        WritableRegistry<EntityType<?>> entityTypeRegistry = (WritableRegistry<EntityType<?>>)
                dedicatedServer.registryAccess().registryOrThrow(Registries.ENTITY_TYPE);

        Field frozen = MappedRegistry.class.getDeclaredField("l");
        frozen.setAccessible(true);
        frozen.set(entityTypeRegistry, false);

        Field unregisteredHolderMap = MappedRegistry.class.getDeclaredField("m");
        unregisteredHolderMap.setAccessible(true);
        unregisteredHolderMap.set(BuiltInRegistries.ENTITY_TYPE, new HashMap<>());

        EntityType.Builder<Entity> builder = EntityType.Builder.of(CustomMinecart::new, MobCategory.MISC)
                .sized(0.98F, 0.7F)
                .clientTrackingRange(8);

        EntityType<Entity> minecart = builder.build("minecart");

        entityTypeRegistry.createIntrusiveHolder(minecart);

        ResourceKey<EntityType<?>> newKey = ResourceKey.create(Registries.ENTITY_TYPE, new ResourceLocation("settlements", "minecart"));
        entityTypeRegistry.register(newKey, minecart, Lifecycle.stable());

        Method register = EntityType.class.getDeclaredMethod("a", String.class, EntityType.Builder.class);
        register.setAccessible(true);
        register.invoke(null, "minecart", builder);

        frozen.set(entityTypeRegistry, true);
        unregisteredHolderMap.set(BuiltInRegistries.ENTITY_TYPE, null);

    }

    public CustomMinecartAbstract createMinecart(ServerLevel serverlevel, double d0, double d1, double d2, ItemStack itemstack, Player player) {
        CustomMinecartAbstract object = new CustomMinecart(serverlevel, d0, d1, d2);

        EntityType.createDefaultStackConfig(serverlevel, itemstack, player).accept(object);
        return object;
    }

    private static Object convertFunction(CraftServer server, CustomMinecart customMinecart) {

        return new CraftMinecartRideable(server, AbstractMinecart.createMinecart(customMinecart.level().getWorld().getHandle(), customMinecart.getX(), customMinecart.getY(), customMinecart.getZ(), Type.RIDEABLE, new ItemStack(Items.MINECART, 1), null));
    }

    private static CustomMinecart spawnFunction(CraftEntityTypes.SpawnData spawn) {
        return new CustomMinecart(spawn.world().getLevel(), spawn.location().x(), spawn.location().y(), spawn.location().z());
    }
}
