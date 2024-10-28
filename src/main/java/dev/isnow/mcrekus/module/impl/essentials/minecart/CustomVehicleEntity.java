package dev.isnow.mcrekus.module.impl.essentials.minecart;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Event;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;

public abstract class CustomVehicleEntity extends Entity
{
    protected static final EntityDataAccessor<Integer> DATA_ID_HURT;
    protected static final EntityDataAccessor<Integer> DATA_ID_HURTDIR;
    protected static final EntityDataAccessor<Float> DATA_ID_DAMAGE;

    static {
        DATA_ID_HURT = SynchedEntityData.defineId(CustomVehicleEntity.class, EntityDataSerializers.INT);
        DATA_ID_HURTDIR = SynchedEntityData.defineId(CustomVehicleEntity.class, EntityDataSerializers.INT);
        DATA_ID_DAMAGE = SynchedEntityData.defineId(CustomVehicleEntity.class, EntityDataSerializers.FLOAT);
    }

    public CustomVehicleEntity(final EntityType<?> entitytypes, final Level world) {
        super(entitytypes, world);
    }

    @Override
    public boolean hurt(final DamageSource damagesource, float f) {
        if (this.level().isClientSide || this.isRemoved()) {
            return true;
        }
        if (this.isInvulnerableTo(damagesource)) {
            return false;
        }
        final Vehicle vehicle = (Vehicle)this.getBukkitEntity();
        final org.bukkit.entity.Entity attacker = (org.bukkit.entity.Entity)((damagesource.getEntity() == null) ? null : damagesource.getEntity().getBukkitEntity());
        final VehicleDamageEvent event = new VehicleDamageEvent(vehicle, attacker, (double)f);
        this.level().getCraftServer().getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            return false;
        }
        f = (float)event.getDamage();
        this.setHurtDir(-this.getHurtDir());
        this.setHurtTime(10);
        this.markHurt();
        this.setDamage(this.getDamage() + f * 10.0f);
        this.gameEvent(GameEvent.ENTITY_DAMAGE, damagesource.getEntity());
        final boolean flag = damagesource.getEntity() instanceof Player && ((Player)damagesource.getEntity()).getAbilities().instabuild;
        if ((flag || this.getDamage() <= 40.0f) && !this.shouldSourceDestroy(damagesource)) {
            if (flag) {
                final VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, attacker);
                this.level().getCraftServer().getPluginManager().callEvent((Event)destroyEvent);
                if (destroyEvent.isCancelled()) {
                    this.setDamage(40.0f);
                    return true;
                }
                this.discard();
            }
        }
        else {
            final VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, attacker);
            this.level().getCraftServer().getPluginManager().callEvent((Event)destroyEvent);
            if (destroyEvent.isCancelled()) {
                this.setDamage(40.0f);
                return true;
            }
            this.destroy(damagesource);
        }
        return true;
    }

    boolean shouldSourceDestroy(final DamageSource damagesource) {
        return false;
    }

    public void destroy(final Item item) {
        this.kill();
        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            final ItemStack itemstack = new ItemStack(item);
            if (this.hasCustomName()) {
                itemstack.setHoverName(this.getCustomName());
            }
            this.spawnAtLocation(itemstack);
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(CustomVehicleEntity.DATA_ID_HURT, 0);
        this.entityData.define(CustomVehicleEntity.DATA_ID_HURTDIR, 1);
        this.entityData.define(CustomVehicleEntity.DATA_ID_DAMAGE, 0.0f);
    }

    public void setHurtTime(final int i) {
        this.entityData.set(CustomVehicleEntity.DATA_ID_HURT, i);
    }

    public void setHurtDir(final int i) {
        this.entityData.set(CustomVehicleEntity.DATA_ID_HURTDIR, i);
    }

    public void setDamage(final float f) {
        this.entityData.set(CustomVehicleEntity.DATA_ID_DAMAGE, f);
    }

    public float getDamage() {
        return this.entityData.get(CustomVehicleEntity.DATA_ID_DAMAGE);
    }

    public int getHurtTime() {
        return this.entityData.get(CustomVehicleEntity.DATA_ID_HURT);
    }

    public int getHurtDir() {
        return this.entityData.get(CustomVehicleEntity.DATA_ID_HURTDIR);
    }

    protected void destroy(final DamageSource damagesource) {
        this.destroy(this.getDropItem());
    }

    abstract Item getDropItem();
}

