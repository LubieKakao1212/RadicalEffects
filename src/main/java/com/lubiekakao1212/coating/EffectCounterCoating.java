package com.lubiekakao1212.coating;

import com.lubiekakao1212.coating.container.IItemCoatingContainer;
import com.lubiekakao1212.components.RadicalComponents;
import com.lubiekakao1212.entityeffects.EffectCounterType;
import com.lubiekakao1212.util.ReadOnly;
import net.minecraft.world.World;

public class EffectCounterCoating implements ICoating {

    private final EffectCounterType counterToApply;

    public EffectCounterCoating(EffectCounterType counterToApply) {
        this.counterToApply = counterToApply;
    }

    /**
     * Used to apply effects
     *
     * @param usage
     * @param instance can't be modified
     * @param container
     * @param world
     */
    @Override
    public <T> void affectTarget(CoatingUsage<T> usage, ReadOnly<CoatingInstance> instance, IItemCoatingContainer container, World world) {
        usage.asEntity().ifPresent(entityUsage -> {
            var counters = RadicalComponents.EFFECT_COUNTERS.get(entityUsage.target);
            counters.applyEffect(counterToApply, 1, null);
        });
    }
}
