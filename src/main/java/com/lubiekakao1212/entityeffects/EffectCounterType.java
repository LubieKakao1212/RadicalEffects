package com.lubiekakao1212.entityeffects;

import com.lubiekakao1212.RadicalEffects;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public abstract class EffectCounterType {

    public final int baseResistance;
    public final int decayStartCooldown;
    public final int decayCooldown;

    public EffectCounterType(int baseResistance, int decayStartCooldown, int decayCooldown) {
        this.baseResistance = baseResistance;
        this.decayStartCooldown = decayStartCooldown;
        this.decayCooldown = decayCooldown;
    }

    public abstract void affectEntity(LivingEntity entity, NbtCompound effectData);

    public void apply(EffectCounterInstance instance, LivingEntity entity, int incomingAmount, @Nullable NbtCompound incomingData) {
        if(instance.apply(entity.world, incomingAmount)) {
            affectEntity(entity, instance.getEffectData());
        }
    }

}
