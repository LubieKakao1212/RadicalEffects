package com.lubiekakao1212.components;

import com.lubiekakao1212.RadicalEffects;
import com.lubiekakao1212.entityeffects.EffectCounterContainer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class RadicalComponents implements EntityComponentInitializer {

    public static final ComponentKey<EffectCounterContainer> EFFECT_COUNTERS =
            ComponentRegistry.getOrCreate(
                    new Identifier(RadicalEffects.MODID, "effect_counters"),
                    EffectCounterContainer.class);

    /**
     * Called to register component factories for statically declared component types.
     *
     * <p><strong>The passed registry must not be held onto!</strong> Static component factories
     * must not be registered outside of this method.
     *
     * @param registry an {@link EntityComponentFactoryRegistry} for <em>statically declared</em> components
     */
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(LivingEntity.class, EFFECT_COUNTERS, EffectCounterContainer::new);
    }
}
