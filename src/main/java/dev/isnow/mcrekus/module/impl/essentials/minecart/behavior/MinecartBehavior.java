package dev.isnow.mcrekus.module.impl.essentials.minecart.behavior;

import dev.isnow.mcrekus.module.impl.essentials.minecart.CustomMinecartAbstract;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

public abstract class MinecartBehavior {

    protected final CustomMinecartAbstract minecart;

    protected MinecartBehavior(CustomMinecartAbstract abstractminecart) {
        this.minecart = abstractminecart;
    }

    public void lerpTo(double d0, double d1, double d2, float f, float f1, int i) {
        this.setPos(d0, d1, d2);
        this.setYRot(f % 360.0F);
        this.setXRot(f1 % 360.0F);
    }

    public double lerpTargetX() {
        return this.getX();
    }

    public double lerpTargetY() {
        return this.getY();
    }

    public double lerpTargetZ() {
        return this.getZ();
    }

    public float lerpTargetXRot() {
        return this.getXRot();
    }

    public float lerpTargetYRot() {
        return this.getYRot();
    }

    public void lerpMotion(double d0, double d1, double d2) {
        this.setDeltaMovement(d0, d1, d2);
    }

    public abstract void tick();

    public Level level() {
        return this.minecart.level();
    }

    public abstract void moveAlongTrack();

    public abstract double stepAlongTrack(BlockPos blockpos, RailShape railshape, double d0);

    public Vec3 getDeltaMovement() {
        return this.minecart.getDeltaMovement();
    }

    public void setDeltaMovement(Vec3 vec3) {
        this.minecart.setDeltaMovement(vec3);
    }

    public void setDeltaMovement(double d0, double d1, double d2) {
        this.minecart.setDeltaMovement(d0, d1, d2);
    }

    public Vec3 position() {
        return this.minecart.position();
    }

    public double getX() {
        return this.minecart.getX();
    }

    public double getY() {
        return this.minecart.getY();
    }

    public double getZ() {
        return this.minecart.getZ();
    }

    public void setPos(Vec3 vec3) {
        this.minecart.setPos(vec3);
    }

    public void setPos(double d0, double d1, double d2) {
        this.minecart.setPos(d0, d1, d2);
    }

    public float getXRot() {
        return this.minecart.getXRot();
    }

    public void setXRot(float f) {
        this.minecart.setXRot(f);
    }

    public float getYRot() {
        return this.minecart.getYRot();
    }

    public void setYRot(float f) {
        this.minecart.setYRot(f);
    }

    public Direction getMotionDirection() {
        return this.minecart.getDirection();
    }

    public Vec3 getKnownMovement(Vec3 vec3) {
        return vec3;
    }

    public abstract double getMaxSpeed();

    public abstract double getSlowdownFactor();
}
