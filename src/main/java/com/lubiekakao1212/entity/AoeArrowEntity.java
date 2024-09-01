package com.lubiekakao1212.entity;

import com.lubiekakao1212.damage.RadicalDamageTypes;
import com.lubiekakao1212.network.RadicalNetwork;
import com.lubiekakao1212.network.packet.PacketClientAoeExplosion;
import com.lubiekakao1212.qulib.math.extensions.AABBExtensionsKt;
import com.lubiekakao1212.qulib.math.mc.Vector3m;
import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public abstract class AoeArrowEntity extends PersistentProjectileEntity {

    protected static final NbtKey<Float> radiusKey = new NbtKey<>("aoeRadius", NbtKey.Type.FLOAT);
    protected static final NbtKey<Float> powerKey = new NbtKey<>("power", NbtKey.Type.FLOAT);
    protected static final NbtKey<Integer> fuseKey = new NbtKey<>("fuse", NbtKey.Type.INT);

    protected float radius;
    protected float power;
    protected int fuse = 20;
    private boolean onCountdown = false;

    public AoeArrowEntity(EntityType<? extends AoeArrowEntity> entityType, World world) {
        super(entityType, world);
        power = 3f;
        radius = 3f;
    }

    public AoeArrowEntity(EntityType<? extends AoeArrowEntity> entityType, LivingEntity owner, World world) {
        super(entityType, owner, world);
        power = 3f;
        radius = 3f;
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public AoeArrowEntity initFromStack(ItemStack stackNbt) {
        this.radius = stackNbt.getOr(radiusKey, 3f);
        this.power = stackNbt.getOr(powerKey, 1f);

        fuse = stackNbt.getOr(fuseKey, 20);

        return this;
    }

    /**
     * Initializes data tracker.
     *
     * @apiNote Subclasses should override this and call {@link DataTracker#startTracking}
     * for any data that needs to be tracked.
     */
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    @Override
    public void tick() {
        super.tick();

        if(onCountdown && fuse-- == 0) {
            explodeAndRemove();
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        onCountdown = true;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        //No super
        //super.onEntityHit(entityHitResult);
        if(entityHitResult.getEntity() instanceof LivingEntity living) {
            affectEntityDirect(living);
        }
        explodeAndRemove();
    }

    private void explodeAndRemove() {
        if(world.isClient) {
            return;
        }

        float radius = (world.getRandom().nextFloat() * 0.25f + 0.75f) * 5f;

        var aabb = new Box(getPos(), getPos()).expand(radius);
        var owner = getOwner();

        var livingEntities = world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), aabb, e -> e != owner);

        for (var living : livingEntities) {
            var dst = (float) Math.sqrt(AABBExtensionsKt.sqrDistanceTo(living.getBoundingBox(), new Vector3m(getPos())));//player.getPos().distanceTo(getPos());
            if (dst > radius) {
                continue;
            }
            var ratio = 1f - (dst / radius);
            affectEntity(living, ratio);
        }

        sendExplodeClient(radius);

        world.playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.PLAYERS, 0.5f, 10);

        discard();
    }

    private void sendExplodeClient(float radius) {
        RadicalNetwork.CHANNEL.serverHandle((ServerWorld) world, getBlockPos()).send(createExplodePacket(radius, power));
    }

    protected abstract void affectEntity(LivingEntity entity, float distanceRatio);

    protected abstract void affectEntityDirect(LivingEntity entity);

    protected abstract Record createExplodePacket(float radius, float strength);
}
