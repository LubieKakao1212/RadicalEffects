package com.lubiekakao1212.coating;

import com.lubiekakao1212.RadicalEffects;
import com.lubiekakao1212.util.RadicalUtil;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class Coatings implements AutoRegistryContainer<ICoating> {

    public static final RegistryKey<Registry<ICoating>> REGISTRY_KEY = RegistryKey.ofRegistry(new Identifier(RadicalEffects.MODID, "coating"));
    public static final DefaultedRegistry<ICoating> REGISTRY = FabricRegistryBuilder.createDefaulted(REGISTRY_KEY, new Identifier(RadicalEffects.MODID, "none")).buildAndRegister();

    public static final ICoating NONE = new NoneCoating();
//    public static final ICoating POTION = new PotionCoating();

    public static final ICoating FIRE = new FireCoating(3);
    public static final ICoating STRONG_FIRE = new FireCoating(15);

    @Override
    public Registry<ICoating> getRegistry() {
        return REGISTRY;
    }

    @Override
    public Class<ICoating> getTargetFieldType() {
        return ICoating.class;
    }

    public static void registerEffectCoatings() {
        RadicalUtil.forRegistryEntries(Registries.STATUS_EFFECT,
                (id, effect) -> Registry.register(REGISTRY,
                        new Identifier(RadicalEffects.MODID, "se_" + id.getNamespace() + "_" + id.getPath()), new StatusEffectCoating(effect)));
    }

}
