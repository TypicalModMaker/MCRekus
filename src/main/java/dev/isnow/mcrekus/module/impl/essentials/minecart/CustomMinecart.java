package dev.isnow.mcrekus.module.impl.essentials.minecart;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CustomMinecart extends CustomMinecartAbstract {
    private float rotationOffset;
    private float playerRotationOffset;

    public CustomMinecart(EntityType<?> entitytype, Level level) {
        super(entitytype, level);
    }

    public CustomMinecart(Level level, double d0, double d1, double d2) {
        super(EntityType.MINECART, level, d0, d1, d2);

        this.setPos(d0, d1, d2);
        level.getWorld().addEntityToWorld(this, CreatureSpawnEvent.SpawnReason.CUSTOM);

    }

    @Override
    public InteractionResult interact(Player player, InteractionHand interactionhand) {
        if (!player.isSecondaryUseActive() && !this.isVehicle() && (this.level().isClientSide || player.startRiding(this))) {
            this.playerRotationOffset = this.rotationOffset;
            return (InteractionResult) (!this.level().isClientSide ? (player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS) : InteractionResult.SUCCESS);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void activateMinecart(int i, int j, int k, boolean flag) {
        if (flag) {
            if (this.isVehicle()) {
                this.ejectPassengers();
            }

            if (this.getHurtTime() == 0) {
                this.setHurtDir(-this.getHurtDir());
                this.setHurtTime(10);
                this.setDamage(50.0F);
                this.markHurt();
            }
        }


    }

    @Override
    public Item getDropItem() {
        return Items.MINECART;
    }

    @Override
    public CustomMinecartAbstract.Type getMinecartType() {
        return CustomMinecartAbstract.Type.RIDEABLE;
    }

    @Override
    public void tick() {
        double d0 = (double) this.getYRot();
        Vec3 vec3 = this.position();

        super.tick();
        double d1 = ((double) this.getYRot() - d0) % 360.0D;

        if (this.level().isClientSide && vec3.distanceTo(this.position()) > 0.01D) {
            this.rotationOffset += (float) d1;
            this.rotationOffset %= 360.0F;
        }

    }

    @Override
    protected void positionRider(Entity entity, Entity.MoveFunction entity_movefunction) {
        super.positionRider(entity, entity_movefunction);
        if (this.level().isClientSide && entity instanceof Player player) {
            float f = (float) Mth.rotLerp(0.5D, (double) this.playerRotationOffset, (double) this.rotationOffset);

            player.setYRot(player.getYRot() - (f - this.playerRotationOffset));
            this.playerRotationOffset = f;
        }

    }
}
