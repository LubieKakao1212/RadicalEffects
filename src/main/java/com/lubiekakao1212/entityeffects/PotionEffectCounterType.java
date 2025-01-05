package com.lubiekakao1212.entityeffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;

public class PotionEffectCounterType extends EffectCounterType {

    public final Potion potion;
    public final float durationScale;

    public PotionEffectCounterType(Potion potion, float durationScale, int baseResistance, int decayStartCooldown, int decayCooldown) {
        super(baseResistance, decayStartCooldown, decayCooldown);
        this.potion = potion;
        this.durationScale = durationScale;
    }

    @Override
    public void affectEntity(LivingEntity entity, NbtCompound effectData) {
        for (var effect : potion.getEffects()) {
            entity.addStatusEffect(
                    new StatusEffectInstance(
                            effect.getEffectType(),
                            effect.mapDuration(duration -> (int)(duration * durationScale)),
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.shouldShowParticles(),
                            effect.shouldShowIcon()
                    )
            );
        }
    }
}
