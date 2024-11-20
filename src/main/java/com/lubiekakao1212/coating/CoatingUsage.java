package com.lubiekakao1212.coating;

import com.lubiekakao1212.util.BoundBlockPos;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CoatingUsage<T> {

    public final InteractionTarget targetType;
    public final InteractionType type;
    public final T target;

    private CoatingUsage(InteractionTarget targetType, InteractionType type, T target) {
        this.targetType = targetType;
        this.type = type;
        this.target = target;
    }

    @NotNull
    public static CoatingUsage<LivingEntity> hit(@NotNull LivingEntity entity) {
        return new CoatingUsage<>(InteractionTarget.Entity, InteractionType.Hit, entity);
    }

    @NotNull
    public static CoatingUsage<BoundBlockPos> hit(@NotNull BoundBlockPos block) {
        return new CoatingUsage<>(InteractionTarget.Block, InteractionType.Hit, block);
    }

    @NotNull
    public static CoatingUsage<LivingEntity> use(@NotNull LivingEntity entity) {
        return new CoatingUsage<>(InteractionTarget.Entity, InteractionType.Use, entity);
    }

    @NotNull
    public static CoatingUsage<BoundBlockPos> use(@NotNull BoundBlockPos block) {
        return new CoatingUsage<>(InteractionTarget.Block, InteractionType.Use, block);
    }

    @NotNull
    public static CoatingUsage<BoundBlockPos> miningTick(@NotNull BoundBlockPos block) {
        return new CoatingUsage<>(InteractionTarget.Block, InteractionType.MINING_TICK, block);
    }

    @NotNull
    public static CoatingUsage<BoundBlockPos> blockBroken(@NotNull BoundBlockPos block) {
        return new CoatingUsage<>(InteractionTarget.Block, InteractionType.BREAK, block);
    }

    @NotNull
    public Optional<CoatingUsage<BoundBlockPos>> asBlock() {
        return targetType == InteractionTarget.Block ? Optional.of((CoatingUsage<BoundBlockPos>) this) : Optional.empty();
    }
    @NotNull
    public Optional<CoatingUsage<LivingEntity>> asEntity() {
        return targetType == InteractionTarget.Entity ? Optional.of((CoatingUsage<LivingEntity>) this) : Optional.empty();
    }

    public enum InteractionTarget {
        Entity,
        Block
    }

    public enum InteractionType {
        Hit,
        Use,
        MINING_TICK,
        BREAK
    }
}
