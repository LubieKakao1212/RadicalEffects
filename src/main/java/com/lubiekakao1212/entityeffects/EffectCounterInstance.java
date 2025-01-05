package com.lubiekakao1212.entityeffects;

import com.lubiekakao1212.RadicalEffects;
import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EffectCounterInstance {

    private static final NbtKey<EffectCounterType> ID_KEY = new NbtKey<>("id", NbtKey.Type.ofRegistry(EffectCounters.REGISTRY));
    private static final NbtKey<Integer> COUNT_KEY = new NbtKey<>("count", NbtKey.Type.INT);
    private static final NbtKey<Long> STAMP_KEY = new NbtKey<>("stamp", NbtKey.Type.LONG);
    private static final NbtKey<Integer> TIMER_KEY = new NbtKey<>("timer", NbtKey.Type.INT);
    private static final NbtKey<NbtCompound> DATA_KEY = new NbtKey<>("data", NbtKey.Type.COMPOUND);

    public final EffectCounterType type;
    private int count;
    private long lastApplicationStamp;
    private int decayTimer;
    private NbtCompound effectData;

    public EffectCounterInstance(@NotNull EffectCounterType type) {
        this.type = type;
        count = 0;
    }

    public void resetDecay(World timeProvider) {
        lastApplicationStamp = timeProvider.getTime();
        decayTimer = 0;
    }

    public boolean apply(World timeProvider, int amount) {
        count += amount;

        boolean flag = false;

        //TODO Include resistance attribute
        if(count > type.baseResistance) {
            count -= type.baseResistance;
            flag = true;
        }

        resetDecay(timeProvider);
        return flag;
    }

    public void tick(LivingEntity entity) {
        var world = entity.world;
        var timeSinceApplied = world.getTime() - lastApplicationStamp;

        if (timeSinceApplied > type.decayStartCooldown) {
            if(decayTimer++ == type.decayCooldown && count > 0) {
                decayTimer = 0;
                count--;
            }
            if(decayTimer > type.decayCooldown) {
                RadicalEffects.LOGGER.warn("Timer overflow");
            }
        }
        else {
            decayTimer = 0;
        }
    }

    public int getCount() {
        return count;
    }

    public NbtCompound getEffectData() {
        return effectData;
    }

    public boolean isEmpty() {
        return count <= 0;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.put(ID_KEY, type);
        nbt.put(COUNT_KEY, count);
        nbt.put(STAMP_KEY, lastApplicationStamp);
        nbt.put(TIMER_KEY, decayTimer);
        nbt.put(DATA_KEY, effectData);

        return nbt;
    }

    @Nullable
    public static EffectCounterInstance fromNbt(NbtCompound nbt) {
        var type = nbt.get(ID_KEY);
        var count = nbt.get(COUNT_KEY);
        var lastApplicationStamp = nbt.get(STAMP_KEY);
        var decayTimer = nbt.get(TIMER_KEY);
        var effectData = nbt.get(DATA_KEY);

        if(type == null || count <= 0) {
            return null;
        }
        var instance = new EffectCounterInstance(type);
        instance.lastApplicationStamp = lastApplicationStamp;
        instance.decayTimer = decayTimer;
        instance.effectData = effectData;

        return instance;
    }

}
