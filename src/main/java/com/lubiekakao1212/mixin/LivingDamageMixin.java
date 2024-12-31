package com.lubiekakao1212.mixin;

import com.lubiekakao1212.coating.CoatingUsage;
import com.lubiekakao1212.coating.ICoating;
import com.lubiekakao1212.coating.container.ItemCoatingContainerWithEntity;
import com.lubiekakao1212.damage.RadicalDamageTags;
import com.lubiekakao1212.effects.EffectHandlers;
import com.lubiekakao1212.util.ReadOnly;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingDamageMixin {

    @Shadow protected abstract void applyDamage(DamageSource source, float amount);

    @Shadow protected abstract float modifyAppliedDamage(DamageSource source, float amount);

    @Shadow public abstract boolean canTakeDamage();

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    public void handleVolatileAndCoatings(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if(source.isIn(DamageTypeTags.IS_FIRE)) {
            EffectHandlers.explodeVolatile((LivingEntity) (Object) this);
        }

        var attacker = source.getAttacker();

        if(attacker instanceof LivingEntity livingAttacker) {
            var stack = livingAttacker.getEquippedStack(EquipmentSlot.MAINHAND);
            if(stack == null) {
                stack = livingAttacker.getEquippedStack(EquipmentSlot.OFFHAND);
            }

            var coatingContainer = new ItemCoatingContainerWithEntity(stack, livingAttacker);

            for(var coatingInstance : coatingContainer.getCoatings()) {
                var coating = coatingInstance.getCoating();

                var usage = CoatingUsage.hit((LivingEntity)(Object)this);
                coating.affectTarget(usage, new ReadOnly<>(coatingInstance), coatingContainer, livingAttacker.world);
                coating.modifyOnUse(usage, coatingInstance, coatingContainer, livingAttacker.world);
            }

            coatingContainer.applyChanges();
        }

    }

    @Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    public void handleMark(LivingEntity entity, DamageSource source, float amount) {
        amount = EffectHandlers.handleMark(entity, source, amount);
        this.applyDamage(source, amount);
    }

    @Redirect(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;modifyAppliedDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"))
    private float modifyDamage(LivingEntity player, DamageSource damageSource, float amount) {
        amount = modifyAppliedDamage(damageSource, amount);

        if(damageSource.isIn(RadicalDamageTags.NO_DAMAGE)) {
            return 0;
        }
        return amount;
    }
}
