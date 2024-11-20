package com.lubiekakao1212.coating;

import com.lubiekakao1212.RadicalEffects;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class Coatings implements AutoRegistryContainer<ICoating> {

    public static final DefaultedRegistry<ICoating> REGISTRY = FabricRegistryBuilder.createDefaulted(RegistryKey.<ICoating>ofRegistry(new Identifier(RadicalEffects.MODID, "coating")), new Identifier(RadicalEffects.MODID, "none")).buildAndRegister();

    public static final ICoating NONE = new NoneCoating();
    public static final ICoating POTION_0 = new PotionCoating();
    public static final ICoating POTION_1 = new PotionCoating();
    public static final ICoating POTION_2 = new PotionCoating();
    public static final ICoating POTION_3 = new PotionCoating();

    public static final ICoating FIRE = new FireCoating();

    @Override
    public Registry<ICoating> getRegistry() {
        return REGISTRY;
    }

    @Override
    public Class<ICoating> getTargetFieldType() {
        return ICoating.class;
    }
}
