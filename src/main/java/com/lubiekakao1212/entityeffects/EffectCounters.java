package com.lubiekakao1212.entityeffects;

import com.lubiekakao1212.RadicalEffects;
import com.lubiekakao1212.util.RadicalUtil;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class EffectCounters {

    public static final RegistryKey<Registry<EffectCounterType>> REGISTRY_KEY = RegistryKey.ofRegistry(new Identifier(RadicalEffects.MODID, "effect_counter"));
    public static final SimpleRegistry<EffectCounterType> REGISTRY = FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister();//, new Identifier(RadicalEffects.MODID, "none")).buildAndRegister();

    public static final int DEFAULT_DECAY_START_COOLDOWN = 20 * 15;
    public static final int DEFAULT_DECAY_RATE_COOLDOWN = 20;
    public static final int DEFAULT_RESISTANCE = 10;

    public static void init() {
        RadicalUtil.forRegistryEntries(Registries.POTION,
                (id, potion) -> Registry.register(REGISTRY,
                        new Identifier(RadicalEffects.MODID, "potion_" + id.getNamespace() + "_" + id.getPath()), new PotionEffectCounterType(potion, 0.5f, DEFAULT_RESISTANCE, DEFAULT_DECAY_START_COOLDOWN, DEFAULT_DECAY_RATE_COOLDOWN)));
    }

}
