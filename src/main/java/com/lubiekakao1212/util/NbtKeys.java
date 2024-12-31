package com.lubiekakao1212.util;

import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class NbtKeys {

    public static final NbtKey.Type<StatusEffect> EFFECT = NbtKey.Type.ofRegistry(Registries.STATUS_EFFECT);
    public static final NbtKey.Type<StatusEffectInstance> EFFECT_INSTANCE = NbtKey.Type.of(NbtElement.COMPOUND_TYPE, NbtKeys::readEffectInstance, NbtKeys::writeEffectInstance);

    @Nullable
    private static StatusEffectInstance readEffectInstance(NbtCompound nbt, String key) {
        return nbt.contains(key) ? StatusEffectInstance.fromNbt(nbt.getCompound(key)) : null;
    }

    private static void writeEffectInstance(NbtCompound nbt, String key, StatusEffectInstance effectInstance) {
        nbt.put(key, effectInstance.writeNbt(new NbtCompound()));
    }

}
