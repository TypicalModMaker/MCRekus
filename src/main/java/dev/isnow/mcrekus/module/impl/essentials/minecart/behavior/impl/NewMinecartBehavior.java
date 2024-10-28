package dev.isnow.mcrekus.module.impl.essentials.minecart.behavior.impl;

import com.mojang.datafixers.util.Pair;
import dev.isnow.mcrekus.module.impl.essentials.minecart.CustomMinecartAbstract;
import dev.isnow.mcrekus.module.impl.essentials.minecart.MthNew;
import dev.isnow.mcrekus.module.impl.essentials.minecart.behavior.MinecartBehavior;
import dev.isnow.mcrekus.module.impl.essentials.minecart.codec.ByteBufCodecs;
import dev.isnow.mcrekus.module.impl.essentials.minecart.codec.StreamCodec;
import dev.isnow.mcrekus.module.impl.essentials.minecart.codec.Vec3Codec;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class NewMinecartBehavior extends MinecartBehavior {

    public static final int POS_ROT_LERP_TICKS = 3;
    public static final double ON_RAIL_Y_OFFSET = 0.1D;
    private NewMinecartBehavior.StepPartialTicks cacheIndexAlpha;
    private int cachedLerpDelay;
    private float cachedPartialTick;
    private int lerpDelay = 0;
    public final List<MinecartStep> lerpSteps = new LinkedList();
    public final List<NewMinecartBehavior.MinecartStep> currentLerpSteps = new LinkedList();
    public double currentLerpStepsTotalWeight = 0.0D;
    public NewMinecartBehavior.MinecartStep oldLerp;
    private boolean firstTick;

    public NewMinecartBehavior(CustomMinecartAbstract abstractminecart) {
        super(abstractminecart);
        this.oldLerp = NewMinecartBehavior.MinecartStep.ZERO;
        this.firstTick = true;
    }

    @Override
    public void tick() {
        if (this.level().isClientSide) {
            this.lerpClientPositionAndRotation();
            boolean flag = BaseRailBlock.isRail(this.level().getBlockState(this.minecart.getCurrentBlockPos()));

            this.minecart.setOnRails(flag);
            this.firstTick = false;
        } else {
            BlockPos blockpos = this.minecart.getCurrentBlockPos();
            BlockState blockstate = this.level().getBlockState(blockpos);

            if (this.firstTick) {
                this.minecart.setOnRails(BaseRailBlock.isRail(blockstate));
                this.adjustToRails(blockpos, blockstate);
            }

            this.minecart.moveAlongTrack();
            this.firstTick = false;
        }
    }

    private void lerpClientPositionAndRotation() {
        if (--this.lerpDelay <= 0) {
            this.setOldLerpValues();
            this.currentLerpSteps.clear();
            if (!this.lerpSteps.isEmpty()) {
                this.currentLerpSteps.addAll(this.lerpSteps);
                this.lerpSteps.clear();
                this.lerpDelay = 3;
                this.currentLerpStepsTotalWeight = 0.0D;

                NewMinecartBehavior.MinecartStep newminecartbehavior_minecartstep;

                for (Iterator iterator = this.currentLerpSteps.iterator(); iterator.hasNext(); this.currentLerpStepsTotalWeight += (double) newminecartbehavior_minecartstep.weight) {
                    newminecartbehavior_minecartstep = (NewMinecartBehavior.MinecartStep) iterator.next();
                }
            }
        }

        if (this.cartHasPosRotLerp()) {
            this.setPos(this.getCartLerpPosition(1.0F));
            this.setDeltaMovement(this.getCartLerpMovements(1.0F));
            this.setXRot(this.getCartLerpXRot(1.0F));
            this.setYRot(this.getCartLerpYRot(1.0F));
        }

    }

    public void setOldLerpValues() {
        this.oldLerp = new NewMinecartBehavior.MinecartStep(this.position(), this.getDeltaMovement(), this.getYRot(), this.getXRot(), 0.0F);
    }

    public boolean cartHasPosRotLerp() {
        return !this.currentLerpSteps.isEmpty();
    }

    public float getCartLerpXRot(float f) {
        NewMinecartBehavior.StepPartialTicks newminecartbehavior_steppartialticks = this.getCurrentLerpStep(f);

        return Mth.rotLerp(newminecartbehavior_steppartialticks.partialTicksInStep, newminecartbehavior_steppartialticks.previousStep.xRot, newminecartbehavior_steppartialticks.currentStep.xRot);
    }

    public float getCartLerpYRot(float f) {
        NewMinecartBehavior.StepPartialTicks newminecartbehavior_steppartialticks = this.getCurrentLerpStep(f);

        return Mth.rotLerp(newminecartbehavior_steppartialticks.partialTicksInStep, newminecartbehavior_steppartialticks.previousStep.yRot, newminecartbehavior_steppartialticks.currentStep.yRot);
    }

    public Vec3 getCartLerpPosition(float f) {
        NewMinecartBehavior.StepPartialTicks newminecartbehavior_steppartialticks = this.getCurrentLerpStep(f);

        return MthNew.lerp(newminecartbehavior_steppartialticks.partialTicksInStep, newminecartbehavior_steppartialticks.previousStep.position, newminecartbehavior_steppartialticks.currentStep.position);
    }

    public Vec3 getCartLerpMovements(float f) {
        NewMinecartBehavior.StepPartialTicks newminecartbehavior_steppartialticks = this.getCurrentLerpStep(f);

        return MthNew.lerp((double) newminecartbehavior_steppartialticks.partialTicksInStep, newminecartbehavior_steppartialticks.previousStep.movement, newminecartbehavior_steppartialticks.currentStep.movement);
    }

    private NewMinecartBehavior.StepPartialTicks getCurrentLerpStep(float f) {
        if (f == this.cachedPartialTick && this.lerpDelay == this.cachedLerpDelay && this.cacheIndexAlpha != null) {
            return this.cacheIndexAlpha;
        } else {
            float f1 = ((float) (3 - this.lerpDelay) + f) / 3.0F;
            float f2 = 0.0F;
            float f3 = 1.0F;
            boolean flag = false;

            int i;

            for (i = 0; i < this.currentLerpSteps.size(); ++i) {
                float f4 = ((NewMinecartBehavior.MinecartStep) this.currentLerpSteps.get(i)).weight;

                if (f4 > 0.0F) {
                    f2 += f4;
                    if ((double) f2 >= this.currentLerpStepsTotalWeight * (double) f1) {
                        float f5 = f2 - f4;

                        f3 = (float) (((double) f1 * this.currentLerpStepsTotalWeight - (double) f5) / (double) f4);
                        flag = true;
                        break;
                    }
                }
            }

            if (!flag) {
                i = this.currentLerpSteps.size() - 1;
            }

            NewMinecartBehavior.MinecartStep newminecartbehavior_minecartstep = (NewMinecartBehavior.MinecartStep) this.currentLerpSteps.get(i);
            NewMinecartBehavior.MinecartStep newminecartbehavior_minecartstep1 = i > 0 ? (NewMinecartBehavior.MinecartStep) this.currentLerpSteps.get(i - 1) : this.oldLerp;

            this.cacheIndexAlpha = new NewMinecartBehavior.StepPartialTicks(f3, newminecartbehavior_minecartstep, newminecartbehavior_minecartstep1);
            this.cachedLerpDelay = this.lerpDelay;
            this.cachedPartialTick = f;
            return this.cacheIndexAlpha;
        }
    }

    private void adjustToRails(BlockPos blockpos, BlockState blockstate) {
        if (BaseRailBlock.isRail(blockstate)) {
            RailShape railshape = (RailShape) blockstate.getValue(((BaseRailBlock) blockstate.getBlock()).getShapeProperty());
            Pair pair = CustomMinecartAbstract.exits(railshape);
            Vec3i vec3i = (Vec3i) pair.getFirst();
            Vec3i vec3i1 = (Vec3i) pair.getSecond();
            Vec3 vec3 = (new Vec3(new Vector3f(vec3i.getX(), vec3i1.getY(), vec3i1.getZ()))).scale(0.5D);
            Vec3 vec31 = (new Vec3(new Vector3f(vec3i1.getX(), vec3i1.getY(), vec3i1.getZ())).scale(0.5D));

            if (this.getDeltaMovement().length() > 9.999999747378752E-6D && this.getDeltaMovement().dot(vec3) < this.getDeltaMovement().dot(vec31)) {
                vec3 = vec31;
            }

            float f = 180.0F - (float) (Math.atan2(vec3.z, vec3.x) * 180.0D / Math.PI);

            f += this.minecart.isflipped() ? 180.0F : 0.0F;
            this.setYRot(f);
            boolean flag = vec3i.getY() != vec3i1.getY();
            Vec3 vec32 = this.position();
            Vec3 vec33 = blockpos.getCenter().subtract(vec32);

            this.setPos(vec32.add(vec33));
            if (flag) {
                Vec3 vec34 = blockpos.getCenter().add(vec31);
                double d0 = vec34.distanceTo(this.position());

                this.setPos(this.position().add(0.0D, d0 + 0.1D, 0.0D));
            } else {
                this.setPos(this.position().add(0.0D, 0.1D, 0.0D));
                this.setXRot(0.0F);
            }

            double d1 = vec32.distanceTo(this.position());

            if (d1 > 0.0D) {
                this.lerpSteps.add(new NewMinecartBehavior.MinecartStep(this.position(), this.getDeltaMovement(), this.getYRot(), this.getXRot(), (float) d1));
            }

        }
    }

    @Override
    public void moveAlongTrack() {
        for (NewMinecartBehavior.TrackIteration newminecartbehavior_trackiteration = new NewMinecartBehavior.TrackIteration(); newminecartbehavior_trackiteration.shouldIterate(); newminecartbehavior_trackiteration.firstIteration = false) {
            BlockPos blockpos = this.minecart.getCurrentBlockPos();
            BlockState blockstate = this.level().getBlockState(blockpos);
            boolean flag = BaseRailBlock.isRail(blockstate);

            if (this.minecart.isOnRails() != flag) {
                this.minecart.setOnRails(flag);
                this.adjustToRails(blockpos, blockstate);
            }

            if (flag) {
                this.minecart.resetFallDistance();
                this.minecart.setOldPosAndRot();
                if (blockstate.is(Blocks.ACTIVATOR_RAIL)) {
                    this.minecart.activateMinecart(blockpos.getX(), blockpos.getY(), blockpos.getZ(), (Boolean) blockstate.getValue(
                            PoweredRailBlock.POWERED));
                }

                RailShape railshape = (RailShape) blockstate.getValue(((BaseRailBlock) blockstate.getBlock()).getShapeProperty());
                Vec3 vec3 = this.calculateTrackSpeed(this.getDeltaMovement(), newminecartbehavior_trackiteration, blockpos, blockstate, railshape);

                if (newminecartbehavior_trackiteration.firstIteration) {
                    newminecartbehavior_trackiteration.movementLeft = vec3.horizontalDistance();
                } else {
                    newminecartbehavior_trackiteration.movementLeft += vec3.horizontalDistance() - this.getDeltaMovement().horizontalDistance();
                }

                this.setDeltaMovement(vec3);
                newminecartbehavior_trackiteration.movementLeft = this.minecart.makeStepAlongTrack(blockpos, railshape, newminecartbehavior_trackiteration.movementLeft);
            } else {
                this.minecart.comeOffTrack();
                newminecartbehavior_trackiteration.movementLeft = 0.0D;
            }

            Vec3 vec31 = this.position();
            double d0 = new Vec3(this.minecart.xOld, this.minecart.yOld, this.minecart.zOld).subtract(vec31).length();

            if (d0 > 9.999999747378752E-6D) {
                float f = this.getYRot();

                if (this.getDeltaMovement().horizontalDistanceSqr() > 0.0D) {
                    f = 180.0F - (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0D / Math.PI);
                    f += this.minecart.isflipped() ? 180.0F : 0.0F;
                }

                float f1 = this.minecart.onGround() && !this.minecart.isOnRails() ? 0.0F : 90.0F - (float) (Math.atan2(this.getDeltaMovement().horizontalDistance(), this.getDeltaMovement().y) * 180.0D / Math.PI);

                f1 *= this.minecart.isflipped() ? -1.0F : 1.0F;
                double d1 = (double) Math.abs(f - this.getYRot());

                if (d1 >= 175.0D && d1 <= 185.0D) {
                    this.minecart.setFlipped(!this.minecart.isflipped());
                    f -= 180.0F;
                    f1 *= -1.0F;
                }

                f1 = Math.clamp(f1, -45.0F, 45.0F);
                this.setXRot(f1 % 360.0F);
                this.setYRot(f % 360.0F);
                this.lerpSteps.add(new NewMinecartBehavior.MinecartStep(vec31, this.getDeltaMovement(), f, f1, (float) d0));
            }
        }

    }

    private Vec3 calculateTrackSpeed(Vec3 vec3, NewMinecartBehavior.TrackIteration newminecartbehavior_trackiteration, BlockPos blockpos, BlockState blockstate, RailShape railshape) {
        Vec3 vec31 = vec3;
        Vec3 vec32;

        if (!newminecartbehavior_trackiteration.hasGainedSlopeSpeed) {
            vec32 = this.calculateSlopeSpeed(vec3, railshape);
            if (vec32.horizontalDistanceSqr() != vec3.horizontalDistanceSqr()) {
                newminecartbehavior_trackiteration.hasGainedSlopeSpeed = true;
                vec31 = vec32;
            }
        }

        if (newminecartbehavior_trackiteration.firstIteration) {
            vec32 = this.calculatePlayerInputSpeed(vec31);
            if (vec32.horizontalDistanceSqr() != vec31.horizontalDistanceSqr()) {
                newminecartbehavior_trackiteration.hasHalted = true;
                vec31 = vec32;
            }
        }

        if (!newminecartbehavior_trackiteration.hasHalted) {
            vec32 = this.calculateHaltTrackSpeed(vec31, blockstate);
            if (vec32.horizontalDistanceSqr() != vec31.horizontalDistanceSqr()) {
                newminecartbehavior_trackiteration.hasHalted = true;
                vec31 = vec32;
            }
        }

        if (newminecartbehavior_trackiteration.firstIteration) {
            vec31 = this.minecart.applyNaturalSlowdown(vec31);
            if (vec31.lengthSqr() > 0.0D) {
                double d0 = Math.min(vec31.length(), this.minecart.getMaxSpeed());

                vec31 = vec31.normalize().scale(d0);
            }
        }

        if (!newminecartbehavior_trackiteration.hasBoosted) {
            vec32 = this.calculateBoostTrackSpeed(vec31, blockpos, blockstate);
            if (vec32.horizontalDistanceSqr() != vec31.horizontalDistanceSqr()) {
                newminecartbehavior_trackiteration.hasBoosted = true;
                vec31 = vec32;
            }
        }

        return vec31;
    }

    private Vec3 calculateSlopeSpeed(Vec3 vec3, RailShape railshape) {
        double d0 = Math.max(0.0078125D, vec3.horizontalDistance() * 0.02D);

        if (this.minecart.isInWater()) {
            d0 *= 0.2D;
        }

        Vec3 vec31;

        switch (railshape) {
            case ASCENDING_EAST:
                vec31 = vec3.add(-d0, 0.0D, 0.0D);
                break;
            case ASCENDING_WEST:
                vec31 = vec3.add(d0, 0.0D, 0.0D);
                break;
            case ASCENDING_NORTH:
                vec31 = vec3.add(0.0D, 0.0D, d0);
                break;
            case ASCENDING_SOUTH:
                vec31 = vec3.add(0.0D, 0.0D, -d0);
                break;
            default:
                vec31 = vec3;
        }

        return vec31;
    }

    private Vec3 calculatePlayerInputSpeed(Vec3 vec3) {
        Entity entity = this.minecart.getFirstPassenger();
        Vec3 vec31 = this.minecart.getPassengerMoveIntent();

        if (entity instanceof ServerPlayer && vec31.lengthSqr() > 0.0D) {
            Vec3 vec32 = vec31.normalize();
            double d0 = vec3.horizontalDistanceSqr();

            if (vec32.lengthSqr() > 0.0D && d0 < 0.01D) {
                return vec3.add((new Vec3(vec32.x, 0.0D, vec32.z)).normalize().scale(0.001D));
            }
        } else {
            this.minecart.setPassengerMoveIntent(Vec3.ZERO);
        }

        return vec3;
    }

    private Vec3 calculateHaltTrackSpeed(Vec3 vec3, BlockState blockstate) {
        return blockstate.is(Blocks.POWERED_RAIL) && !(Boolean) blockstate.getValue(PoweredRailBlock.POWERED) ? (vec3.length() < 0.03D ? Vec3.ZERO : vec3.scale(0.5D)) : vec3;
    }

    private Vec3 calculateBoostTrackSpeed(Vec3 vec3, BlockPos blockpos, BlockState blockstate) {
        if (blockstate.is(Blocks.POWERED_RAIL) && (Boolean) blockstate.getValue(PoweredRailBlock.POWERED)) {
            if (vec3.length() > 0.01D) {
                return vec3.normalize().scale(vec3.length() + 0.06D);
            } else {
                Vec3 vec31 = this.minecart.getRedstoneDirection(blockpos);

                return vec31.lengthSqr() <= 0.0D ? vec3 : vec31.scale(vec3.length() + 0.2D);
            }
        } else {
            return vec3;
        }
    }

    @Override
    public double stepAlongTrack(BlockPos blockpos, RailShape railshape, double d0) {
        if (d0 < 9.999999747378752E-6D) {
            return 0.0D;
        } else {
            Vec3 vec3 = this.position();
            Pair pair = CustomMinecartAbstract.exits(railshape);
            Vec3i vec3i = (Vec3i) pair.getFirst();
            Vec3i vec3i1 = (Vec3i) pair.getSecond();
            Vec3 vec31 = this.getDeltaMovement();

            if (vec31.length() < 9.999999747378752E-6D) {
                this.setDeltaMovement(Vec3.ZERO);
                return 0.0D;
            } else {
                boolean flag = vec3i.getY() != vec3i1.getY();
                Vec3 vec32 = (new Vec3(new Vector3f(vec3i1.getX(), vec3i1.getY(), vec3i1.getZ()))).scale(0.5D);
                Vec3 vec33 = (new Vec3(new Vector3f(vec3i.getX(), vec3i.getY(), vec3i.getZ())).scale(0.5D));

                if (vec31.dot(vec33) < vec31.dot(vec32)) {
                    vec33 = vec32;
                }

                Vec3 vec34 = blockpos.getCenter().add(vec33).add(0.0D, 0.1D, 0.0D).add(vec33.normalize().scale(9.999999747378752E-6D));

                if (flag && !this.isDecending(vec31, railshape)) {
                    vec34 = vec34.add(0.0D, 1.0D, 0.0D);
                }

                Vec3 vec35 = vec34.subtract(this.position()).normalize();

                vec31 = vec35.scale(vec31.length() / vec35.horizontalDistance());
                Vec3 vec36 = vec3.add(vec31.normalize().scale(d0 * (double) (flag ? Mth.SQRT_OF_TWO : 1.0F)));

                if (vec3.distanceToSqr(vec34) <= vec3.distanceToSqr(vec36)) {
                    d0 = vec34.subtract(vec36).horizontalDistance();
                    vec36 = vec34;
                } else {
                    d0 = 0.0D;
                }

                this.minecart.move(MoverType.SELF, vec36.subtract(vec3));
                BlockPos blockpos1 = BlockPos.containing(vec36);
                BlockState blockstate = this.level().getBlockState(blockpos1);

                if (flag && BaseRailBlock.isRail(blockstate)) {
                    this.setPos(vec36);
                }

                if (this.position().distanceTo(vec3) < 9.999999747378752E-6D && vec36.distanceTo(vec3) > 9.999999747378752E-6D) {
                    this.setDeltaMovement(Vec3.ZERO);
                    return 0.0D;
                } else {
                    this.setDeltaMovement(vec31);
                    return d0;
                }
            }
        }
    }

    @Override
    public double getMaxSpeed() {
        return (double) 500 * (this.minecart.isInWater() ? 0.5D : 1.0D) / 20.0D;
    }

    private boolean isDecending(Vec3 vec3, RailShape railshape) {
        boolean flag;

        switch (railshape) {
            case ASCENDING_EAST:
                flag = vec3.x < 0.0D;
                break;
            case ASCENDING_WEST:
                flag = vec3.x > 0.0D;
                break;
            case ASCENDING_NORTH:
                flag = vec3.z > 0.0D;
                break;
            case ASCENDING_SOUTH:
                flag = vec3.z < 0.0D;
                break;
            default:
                flag = false;
        }

        return flag;
    }

    @Override
    public double getSlowdownFactor() {
        return this.minecart.isVehicle() ? 0.997D : 0.975D;
    }

    public static record MinecartStep(Vec3 position, Vec3 movement, float yRot, float xRot, float weight) {

        public static final StreamCodec<ByteBuf, Float> ROTATION_STREAM_CODEC = ByteBufCodecs.BYTE.map(NewMinecartBehavior.MinecartStep::uncompressRotation, NewMinecartBehavior.MinecartStep::compressRotation);
        public static final StreamCodec<ByteBuf, MinecartStep> STREAM_CODEC = StreamCodec.composite(
                Vec3Codec.STREAM_CODEC, NewMinecartBehavior.MinecartStep::position, Vec3Codec.STREAM_CODEC, NewMinecartBehavior.MinecartStep::movement, NewMinecartBehavior.MinecartStep.ROTATION_STREAM_CODEC, NewMinecartBehavior.MinecartStep::yRot, NewMinecartBehavior.MinecartStep.ROTATION_STREAM_CODEC, NewMinecartBehavior.MinecartStep::xRot, ByteBufCodecs.FLOAT, NewMinecartBehavior.MinecartStep::weight, NewMinecartBehavior.MinecartStep::new);
        public static NewMinecartBehavior.MinecartStep ZERO = new NewMinecartBehavior.MinecartStep(Vec3.ZERO, Vec3.ZERO, 0.0F, 0.0F, 0.0F);

        private static byte compressRotation(float f) {
            return (byte) Mth.floor(f * 256.0F / 360.0F);
        }

        private static float uncompressRotation(byte b0) {
            return (float) b0 * 360.0F / 256.0F;
        }
    }

    private static record StepPartialTicks(float partialTicksInStep, NewMinecartBehavior.MinecartStep currentStep, NewMinecartBehavior.MinecartStep previousStep) {

    }

    private static class TrackIteration {

        double movementLeft = 0.0D;
        boolean firstIteration = true;
        boolean hasGainedSlopeSpeed = false;
        boolean hasHalted = false;
        boolean hasBoosted = false;

        TrackIteration() {}

        public boolean shouldIterate() {
            return this.firstIteration || this.movementLeft > 9.999999747378752E-6D;
        }
    }
}
