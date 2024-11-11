//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.isnow.mcrekus.module.impl.essentials.minecart;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.datafixers.util.Pair;
import dev.isnow.mcrekus.module.impl.essentials.minecart.behavior.MinecartBehavior;
import dev.isnow.mcrekus.module.impl.essentials.minecart.behavior.impl.NewMinecartBehavior;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.BlockUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public abstract class CustomMinecartAbstract extends CustomVehicleEntity {

    private static final Vec3 LOWERED_PASSENGER_ATTACHMENT = new Vec3(0.0D, 0.0D, 0.0D);
    private static final EntityDataAccessor<Integer> DATA_ID_DISPLAY_BLOCK = SynchedEntityData.defineId(CustomMinecartAbstract.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_DISPLAY_OFFSET = SynchedEntityData.defineId(
            CustomMinecartAbstract.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_ID_CUSTOM_DISPLAY = SynchedEntityData.defineId(CustomMinecartAbstract.class, EntityDataSerializers.BOOLEAN);
    private static final ImmutableMap<Pose, ImmutableList<Integer>> POSE_DISMOUNT_HEIGHTS = ImmutableMap.of(Pose.STANDING, ImmutableList.of(0, 1, -1), Pose.CROUCHING, ImmutableList.of(0, 1, -1), Pose.SWIMMING, ImmutableList.of(0, 1));
    protected static final float WATER_SLOWDOWN_FACTOR = 0.95F;
    private boolean onRails;
    private boolean flipped;
    private Vec3 passengerMoveIntent;
    private final MinecartBehavior behavior;
    private static final Map<RailShape, Pair<Vec3i, Vec3i>> EXITS = (Map) Util.make(Maps.newEnumMap(RailShape.class), (enummap) -> {
        Vec3i vec3i = Direction.WEST.getNormal();
        Vec3i vec3i1 = Direction.EAST.getNormal();
        Vec3i vec3i2 = Direction.NORTH.getNormal();
        Vec3i vec3i3 = Direction.SOUTH.getNormal();
        Vec3i vec3i4 = vec3i.below();
        Vec3i vec3i5 = vec3i1.below();
        Vec3i vec3i6 = vec3i2.below();
        Vec3i vec3i7 = vec3i3.below();

        enummap.put(RailShape.NORTH_SOUTH, Pair.of(vec3i2, vec3i3));
        enummap.put(RailShape.EAST_WEST, Pair.of(vec3i, vec3i1));
        enummap.put(RailShape.ASCENDING_EAST, Pair.of(vec3i4, vec3i1));
        enummap.put(RailShape.ASCENDING_WEST, Pair.of(vec3i, vec3i5));
        enummap.put(RailShape.ASCENDING_NORTH, Pair.of(vec3i2, vec3i7));
        enummap.put(RailShape.ASCENDING_SOUTH, Pair.of(vec3i6, vec3i3));
        enummap.put(RailShape.SOUTH_EAST, Pair.of(vec3i3, vec3i1));
        enummap.put(RailShape.SOUTH_WEST, Pair.of(vec3i3, vec3i));
        enummap.put(RailShape.NORTH_WEST, Pair.of(vec3i2, vec3i));
        enummap.put(RailShape.NORTH_EAST, Pair.of(vec3i2, vec3i1));
    });

    protected CustomMinecartAbstract(EntityType<?> entitytype, Level level) {
        super(entitytype, level);
        this.passengerMoveIntent = Vec3.ZERO;
        this.blocksBuilding = true;
        this.behavior = new NewMinecartBehavior(this);
    }

    protected CustomMinecartAbstract(EntityType<?> entitytype, Level level, double d0, double d1, double d2) {
        this(entitytype, level);
        this.setPos(d0, d1, d2);
        this.xo = d0;
        this.yo = d1;
        this.zo = d2;
    }

    public MinecartBehavior getBehavior() {
        return this.behavior;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return Boat.canVehicleCollide(this, entity);
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public Vec3 getRelativePortalPosition(Direction.Axis direction_axis, BlockUtil.FoundRectangle blockutil_foundrectangle) {
        return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(direction_axis, blockutil_foundrectangle));
    }

    @Override
    protected Vector3f getPassengerAttachmentPoint(Entity entity, EntityDimensions entitydimensions, float f) {
        boolean flag = entity instanceof Villager || entity instanceof WanderingTrader;

        return flag ? CustomMinecartAbstract.LOWERED_PASSENGER_ATTACHMENT.toVector3f()
                : super.getPassengerAttachmentPoint(entity, entitydimensions, f);
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingentity) {
        Direction direction = this.getMotionDirection();

        if (direction.getAxis() == Direction.Axis.Y) {
            return super.getDismountLocationForPassenger(livingentity);
        } else {
            int[][] aint = DismountHelper.offsetsForDirection(direction);
            BlockPos blockpos = this.blockPosition();
            BlockPos.MutableBlockPos blockpos_mutableblockpos = new BlockPos.MutableBlockPos();
            ImmutableList immutablelist = livingentity.getDismountPoses();
            UnmodifiableIterator unmodifiableiterator = immutablelist.iterator();

            while (unmodifiableiterator.hasNext()) {
                Pose pose = (Pose) unmodifiableiterator.next();
                EntityDimensions entitydimensions = livingentity.getDimensions(pose);
                float f = Math.min(entitydimensions.width, 1.0F) / 2.0F;
                UnmodifiableIterator unmodifiableiterator1 = ((ImmutableList) CustomMinecartAbstract.POSE_DISMOUNT_HEIGHTS.get(pose)).iterator();

                while (unmodifiableiterator1.hasNext()) {
                    int i = (Integer) unmodifiableiterator1.next();
                    int[][] aint1 = aint;
                    int j = aint.length;

                    for (int k = 0; k < j; ++k) {
                        int[] aint2 = aint1[k];

                        blockpos_mutableblockpos.set(blockpos.getX() + aint2[0], blockpos.getY() + i, blockpos.getZ() + aint2[1]);
                        double d0 = this.level().getBlockFloorHeight(DismountHelper.nonClimbableShape(this.level(), blockpos_mutableblockpos), () -> {
                            return DismountHelper.nonClimbableShape(this.level(), blockpos_mutableblockpos.below());
                        });

                        if (DismountHelper.isBlockFloorValid(d0)) {
                            AABB aabb = new AABB((double) (-f), 0.0D, (double) (-f), (double) f, (double) entitydimensions.height, (double) f);
                            Vec3 vec3 = Vec3.upFromBottomCenterOf(blockpos_mutableblockpos, d0);

                            if (DismountHelper.canDismountTo(this.level(), livingentity, aabb.move(vec3))) {
                                livingentity.setPose(pose);
                                return vec3;
                            }
                        }
                    }
                }
            }

            double d1 = this.getBoundingBox().maxY;

            blockpos_mutableblockpos.set((double) blockpos.getX(), d1, (double) blockpos.getZ());
            UnmodifiableIterator unmodifiableiterator2 = immutablelist.iterator();

            while (unmodifiableiterator2.hasNext()) {
                Pose pose1 = (Pose) unmodifiableiterator2.next();
                double d2 = (double) livingentity.getDimensions(pose1).height;
                int l = Mth.ceil(d1 - (double) blockpos_mutableblockpos.getY() + d2);
                double d3 = DismountHelper.findCeilingFrom(blockpos_mutableblockpos, l, (blockpos1) -> {
                    return this.level().getBlockState(blockpos1).getCollisionShape(this.level(), blockpos1);
                });

                if (d1 + d2 <= d3) {
                    livingentity.setPose(pose1);
                    break;
                }
            }

            return super.getDismountLocationForPassenger(livingentity);
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_DISPLAY_BLOCK, Block.getId(Blocks.AIR.defaultBlockState()));
        this.entityData.define(DATA_ID_DISPLAY_OFFSET, 6);
        this.entityData.define(DATA_ID_CUSTOM_DISPLAY, false);
    }

    @Override
    protected float getBlockSpeedFactor() {
        BlockState blockstate = this.level().getBlockState(this.blockPosition());

        return blockstate.is(BlockTags.RAILS) ? 1.0F : super.getBlockSpeedFactor();
    }

    @Override
    public void animateHurt(float f) {
        this.setHurtDir(-this.getHurtDir());
        this.setHurtTime(10);
        this.setDamage(this.getDamage() + this.getDamage() * 10.0F);
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    public static Pair<Vec3i, Vec3i> exits(RailShape railshape) {
        return (Pair) CustomMinecartAbstract.EXITS.get(railshape);
    }

    @Override
    public Direction getMotionDirection() {
        return this.behavior.getMotionDirection();
    }

    @Override
    public void tick() {
        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1);
        }

        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }

        this.checkBelowWorld();
//        this.handlePortal();
        this.behavior.tick();
        this.updateInWaterStateAndDoFluidPushing();
        if (this.isInLava()) {
            this.lavaHurt();
            this.fallDistance *= 0.5F;
        }

        this.firstTick = false;
    }

    public BlockPos getCurrentBlockPos() {
        int i = Mth.floor(this.getX());
        int j = Mth.floor(this.getY());
        int k = Mth.floor(this.getZ());

        if (this.level().getBlockState(new BlockPos(i, j - 1, k)).is(BlockTags.RAILS)) {
            --j;
        }

        return new BlockPos(i, j, k);
    }

    public boolean pushOrPickUpEntities(AABB aabb, double d0) {
        boolean flag = false;

        if (this.getMinecartType() == CustomMinecartAbstract.Type.RIDEABLE && this.getDeltaMovement().horizontalDistanceSqr() >= d0) {
            List list = this.level().getEntities((Entity) this, aabb, EntitySelector.pushableBy(this));

            if (!list.isEmpty()) {
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();

                    if (!(entity instanceof Player) && !(entity instanceof IronGolem) && !(entity instanceof CustomMinecartAbstract) && !this.isVehicle() && !entity.isPassenger()) {
                        entity.startRiding(this);
                        flag = true;
                    } else {
                        entity.push((Entity) this);
                    }
                }
            }
        } else {
            Iterator iterator1 = this.level().getEntities(this, aabb).iterator();

            while (iterator1.hasNext()) {
                Entity entity1 = (Entity) iterator1.next();

                if (!this.hasPassenger(entity1) && entity1.isPushable() && entity1 instanceof CustomMinecartAbstract) {
                    entity1.push((Entity) this);
                }
            }
        }

        return flag;
    }

    public double getMaxSpeed() {
        return this.behavior.getMaxSpeed();
    }

    public void activateMinecart(int i, int j, int k, boolean flag) {}

    @Override
    public void lerpPositionAndRotationStep(int i, double d0, double d1, double d2, double d3, double d4) {
        super.lerpPositionAndRotationStep(i, d0, d1, d2, d3, d4);
    }

    @Override
    public void reapplyPosition() {
        super.reapplyPosition();
    }

    @Override
    public boolean updateInWaterStateAndDoFluidPushing() {
        return super.updateInWaterStateAndDoFluidPushing();
    }


    @Override
    public void lerpTo(double d0, double d1, double d2, float f, float f1, int i) {
        this.behavior.lerpTo(d0, d1, d2, f, f1, i);
    }

    @Override
    public double lerpTargetX() {
        return this.behavior.lerpTargetX();
    }

    @Override
    public double lerpTargetY() {
        return this.behavior.lerpTargetY();
    }

    @Override
    public double lerpTargetZ() {
        return this.behavior.lerpTargetZ();
    }

    @Override
    public float lerpTargetXRot() {
        return this.behavior.lerpTargetXRot();
    }

    @Override
    public float lerpTargetYRot() {
        return this.behavior.lerpTargetYRot();
    }

    @Override
    public void lerpMotion(double d0, double d1, double d2) {
        this.behavior.lerpMotion(d0, d1, d2);
    }

    public void moveAlongTrack() {
        this.behavior.moveAlongTrack();
    }

    public void comeOffTrack() {
        double d0 = this.getMaxSpeed();
        Vec3 vec3 = this.getDeltaMovement();

        this.setDeltaMovement(Mth.clamp(vec3.x, -d0, d0), vec3.y, Mth.clamp(vec3.z, -d0, d0));
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        if (!this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95D));
        }

    }

    public double makeStepAlongTrack(BlockPos blockpos, RailShape railshape, double d0) {
        return this.behavior.stepAlongTrack(blockpos, railshape, d0);
    }

    @Override
    public void move(MoverType movertype, Vec3 vec3) {
        Vec3 vec31 = this.position().add(vec3);

        super.move(movertype, vec3);
        if (this.horizontalCollision || this.verticalCollision) {
            boolean flag = this.pushOrPickUpEntities(this.getBoundingBox().inflate(1.0E-7D), 0.0D);

            if (flag) {
                super.move(movertype, vec31.subtract(this.position()));
            }
        }

    }

    @Override
    public boolean isOnRails() {
        return this.onRails;
    }

    public void setOnRails(boolean flag) {
        this.onRails = flag;
    }

    public boolean isflipped() {
        return this.flipped;
    }

    public void setFlipped(boolean flag) {
        this.flipped = flag;
    }

    public Vec3 getRedstoneDirection(BlockPos blockpos) {
        BlockState blockstate = this.level().getBlockState(blockpos);

        if (blockstate.is(Blocks.POWERED_RAIL) && (Boolean) blockstate.getValue(PoweredRailBlock.POWERED)) {
            RailShape railshape = (RailShape) blockstate.getValue(((BaseRailBlock) blockstate.getBlock()).getShapeProperty());

            if (railshape == RailShape.EAST_WEST) {
                if (this.isRedstoneConductor(blockpos.west())) {
                    return new Vec3(1.0D, 0.0D, 0.0D);
                }

                if (this.isRedstoneConductor(blockpos.east())) {
                    return new Vec3(-1.0D, 0.0D, 0.0D);
                }
            } else if (railshape == RailShape.NORTH_SOUTH) {
                if (this.isRedstoneConductor(blockpos.north())) {
                    return new Vec3(0.0D, 0.0D, 1.0D);
                }

                if (this.isRedstoneConductor(blockpos.south())) {
                    return new Vec3(0.0D, 0.0D, -1.0D);
                }
            }

            return Vec3.ZERO;
        } else {
            return Vec3.ZERO;
        }
    }

    public boolean isRedstoneConductor(BlockPos blockpos) {
        return this.level().getBlockState(blockpos).isRedstoneConductor(this.level(), blockpos);
    }

    public Vec3 applyNaturalSlowdown(Vec3 vec3) {
        double d0 = this.behavior.getSlowdownFactor();
        Vec3 vec31 = vec3.multiply(d0, 0.0D, d0);

        if (this.isInWater()) {
            vec31 = vec31.scale(0.949999988079071D);
        }

        return vec31;
    }

    protected void readAdditionalSaveData(CompoundTag nbttagcompound) {
        if (nbttagcompound.getBoolean("CustomDisplayTile")) {
            this.setDisplayBlockState(NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), nbttagcompound.getCompound("DisplayState")));
            this.setDisplayOffset(nbttagcompound.getInt("DisplayOffset"));
        }

    }

    protected void addAdditionalSaveData(CompoundTag nbttagcompound) {
        if (this.hasCustomDisplay()) {
            nbttagcompound.putBoolean("CustomDisplayTile", true);
            nbttagcompound.put("DisplayState", NbtUtils.writeBlockState(this.getDisplayBlockState()));
            nbttagcompound.putInt("DisplayOffset", this.getDisplayOffset());
        }

    }

    @Override
    public void push(Entity entity) {
        if (!this.level().isClientSide) {
            if (!entity.noPhysics && !this.noPhysics) {
                if (!this.hasPassenger(entity)) {
                    double d0 = entity.getX() - this.getX();
                    double d1 = entity.getZ() - this.getZ();
                    double d2 = d0 * d0 + d1 * d1;

                    if (d2 >= 9.999999747378752E-5D) {
                        d2 = Math.sqrt(d2);
                        d0 /= d2;
                        d1 /= d2;
                        double d3 = 1.0D / d2;

                        if (d3 > 1.0D) {
                            d3 = 1.0D;
                        }

                        d0 *= d3;
                        d1 *= d3;
                        d0 *= 0.10000000149011612D;
                        d1 *= 0.10000000149011612D;
                        d0 *= 0.5D;
                        d1 *= 0.5D;
                        if (entity instanceof CustomMinecartAbstract) {
                            double d4 = entity.getX() - this.getX();
                            double d5 = entity.getZ() - this.getZ();
                            Vec3 vec3 = (new Vec3(d4, 0.0D, d5)).normalize();
                            Vec3 vec31 = (new Vec3((double) Mth.cos(this.getYRot() * 0.017453292F), 0.0D, (double) Mth.sin(this.getYRot() * 0.017453292F))).normalize();
                            double d6 = Math.abs(vec3.dot(vec31));

                            if (d6 < 0.800000011920929D) {
                                return;
                            }

                            Vec3 vec32 = this.getDeltaMovement();
                            Vec3 vec33 = entity.getDeltaMovement();

                            if (((CustomMinecartAbstract) entity).getMinecartType() == CustomMinecartAbstract.Type.FURNACE && this.getMinecartType() != CustomMinecartAbstract.Type.FURNACE) {
                                this.setDeltaMovement(vec32.multiply(0.2D, 1.0D, 0.2D));
                                this.push(vec33.x - d0, 0.0D, vec33.z - d1);
                                entity.setDeltaMovement(vec33.multiply(0.95D, 1.0D, 0.95D));
                            } else if (((CustomMinecartAbstract) entity).getMinecartType() != CustomMinecartAbstract.Type.FURNACE && this.getMinecartType() == CustomMinecartAbstract.Type.FURNACE) {
                                entity.setDeltaMovement(vec33.multiply(0.2D, 1.0D, 0.2D));
                                entity.push(vec32.x + d0, 0.0D, vec32.z + d1);
                                this.setDeltaMovement(vec32.multiply(0.95D, 1.0D, 0.95D));
                            } else {
                                double d7 = (vec33.x + vec32.x) / 2.0D;
                                double d8 = (vec33.z + vec32.z) / 2.0D;

                                this.setDeltaMovement(vec32.multiply(0.2D, 1.0D, 0.2D));
                                this.push(d7 - d0, 0.0D, d8 - d1);
                                entity.setDeltaMovement(vec33.multiply(0.2D, 1.0D, 0.2D));
                                entity.push(d7 + d0, 0.0D, d8 + d1);
                            }
                        } else {
                            this.push(-d0, 0.0D, -d1);
                            entity.push(d0 / 4.0D, 0.0D, d1 / 4.0D);
                        }
                    }

                }
            }
        }
    }

    public abstract CustomMinecartAbstract.Type getMinecartType();

    public BlockState getDisplayBlockState() {
        return !this.hasCustomDisplay() ? this.getDefaultDisplayBlockState() : Block.stateById((Integer) this.getEntityData().get(CustomMinecartAbstract.DATA_ID_DISPLAY_BLOCK));
    }

    public BlockState getDefaultDisplayBlockState() {
        return Blocks.AIR.defaultBlockState();
    }

    public int getDisplayOffset() {
        return !this.hasCustomDisplay() ? this.getDefaultDisplayOffset() : (Integer) this.getEntityData().get(CustomMinecartAbstract.DATA_ID_DISPLAY_OFFSET);
    }

    public int getDefaultDisplayOffset() {
        return 6;
    }

    public void setDisplayBlockState(BlockState blockstate) {
        this.getEntityData().set(CustomMinecartAbstract.DATA_ID_DISPLAY_BLOCK, Block.getId(blockstate));
        this.setCustomDisplay(true);
    }

    public void setDisplayOffset(int i) {
        this.getEntityData().set(CustomMinecartAbstract.DATA_ID_DISPLAY_OFFSET, i);
        this.setCustomDisplay(true);
    }

    public boolean hasCustomDisplay() {
        return (Boolean) this.getEntityData().get(CustomMinecartAbstract.DATA_ID_CUSTOM_DISPLAY);
    }

    public void setCustomDisplay(boolean flag) {
        this.getEntityData().set(CustomMinecartAbstract.DATA_ID_CUSTOM_DISPLAY, flag);
    }

    @Override
    public ItemStack getPickResult() {
        Item item;

        switch (this.getMinecartType().ordinal()) {
            case 1:
                item = Items.CHEST_MINECART;
                break;
            case 2:
                item = Items.FURNACE_MINECART;
                break;
            case 3:
                item = Items.TNT_MINECART;
                break;
            case 4:
            default:
                item = Items.MINECART;
                break;
            case 5:
                item = Items.HOPPER_MINECART;
                break;
            case 6:
                item = Items.COMMAND_BLOCK_MINECART;
        }

        return new ItemStack(item);
    }

//    public void setPassengerMoveIntentFromInput(LivingEntity livingentity, Vec3 vec3) {
//        Vec3 vec31 = getInputVector(vec3, 1.0F, livingentity.getYRot());
//
//        this.setPassengerMoveIntent(vec31);
//    }

    public void setPassengerMoveIntent(Vec3 vec3) {
        this.passengerMoveIntent = vec3;
    }

    public Vec3 getPassengerMoveIntent() {
        return this.passengerMoveIntent;
    }


    public static enum Type {

        RIDEABLE, CHEST, FURNACE, TNT, SPAWNER, HOPPER, COMMAND_BLOCK;

        private Type() {}
    }
}
