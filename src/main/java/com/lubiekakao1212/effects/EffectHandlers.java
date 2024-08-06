package com.lubiekakao1212.effects;

import com.lubiekakao1212.RadicalEffects;
import com.lubiekakao1212.damage.RadicalDamageTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class EffectHandlers {

    private static final RegistryKey<DamageType> DAMAGE_VOLATILE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(RadicalEffects.MODID, "volatile_explosion"));

    public static void explodeVolatile(LivingEntity entity) {
        var effect = entity.getStatusEffect(RadicalStatusEffects.VOLATILE);
        if(effect == null) {
            return;
        }

        var world = entity.world;
        var random = entity.getRandom();
        var damageSource = world.getDamageSources().create(DAMAGE_VOLATILE);

        var amp = (effect.getAmplifier() + 1);
        var knockback = (int) (effect.getDuration() / RadicalEffects.volatileKnockbackDurationStep + 2) * amp * RadicalEffects.volatileKnockbackScale;
        var damage = amp * RadicalEffects.volatileDamageScale;



        entity.damage(damageSource, damage);

        var theta = random.nextFloat() * (float)Math.PI;
        entity.takeKnockback(knockback, Math.cos(theta), Math.sin(theta));

        var newDuration = effect.getDuration() * RadicalEffects.volatileDurationDecay;

        entity.setStatusEffect(
                new StatusEffectInstance(
                        effect.getEffectType(),
                        (int) newDuration,
                        effect.getAmplifier(),
                        effect.isAmbient(),
                        effect.shouldShowParticles()
                ), null);

        var x = entity.getX();
        var y = entity.getY();
        var z = entity.getZ();
        ((ServerWorld)world).spawnParticles(ParticleTypes.EXPLOSION, x, y, z, random.nextBetween(2, 5),0.5,0.5,0.5, 0.5);
        world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS,0.1f, 2f);
    }

    public static float handleMark(LivingEntity entity, DamageSource damageSource, float damage) {
        if(damageSource.isIn(RadicalDamageTags.IGNORE_MARK)) {
            return damage;
        }
        var effect = entity.getStatusEffect(RadicalStatusEffects.MARK);
        if(effect != null) {
            var scale = 1f + (effect.getAmplifier() + 1f) * RadicalEffects.markDamageBoost;

            return damage * scale;
        }
        return damage;
    }
}
