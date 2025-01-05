package com.lubiekakao1212.entityeffects;

import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class EffectCounterContainer implements ServerTickingComponent {

    private static final NbtKey.ListKey<NbtCompound> COUNTERS_KEY = new NbtKey.ListKey<>("counters", NbtKey.Type.COMPOUND);

    private final LivingEntity entity;
    private final Map<EffectCounterType, EffectCounterInstance> counters = new HashMap<>();

    public EffectCounterContainer(LivingEntity entity) {
        this.entity = entity;
    }

    public void applyEffect(EffectCounterType effectType, int amount, @Nullable NbtCompound data) {
        var counter = counters.computeIfAbsent(effectType, EffectCounterInstance::new);
        effectType.apply(counter, entity, amount, data);
    }

    @Override
    public void serverTick() {
        for (var counter : counters.values()) {
            counter.tick(entity);
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        var countersList = new NbtList();

        for(var counter : counters.values()) {
            if(!counter.isEmpty()) {
                countersList.add(counter.writeNbt(new NbtCompound()));
            }
        }

        nbt.put(COUNTERS_KEY, countersList);
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        var countersList = nbt.get(COUNTERS_KEY);

        for (int i = 0; i<countersList.size(); i++) {
            var instance = EffectCounterInstance.fromNbt(countersList.getCompound(i));

            if(instance != null) {
                counters.put(instance.type, instance);
            }
        }
    }
}
