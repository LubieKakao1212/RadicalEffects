package com.lubiekakao1212.mixin;

import com.lubiekakao1212.RadicalEffects;
import com.lubiekakao1212.damage.RadicalDamageTags;
import com.lubiekakao1212.util.EmpUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.joml.Math;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class PlayerDamageMixin extends LivingEntity {

	protected PlayerDamageMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Redirect(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;modifyAppliedDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"))
	private float modifyDamage(PlayerEntity player, DamageSource damageSource, float amount) {
		amount = modifyAppliedDamage(damageSource, amount);

		if(damageSource.isIn(RadicalDamageTags.CONVERT_ENERGY)) {

			var energyToDrain = (int)(Math.min(amount * RadicalEffects.powerDamageScale, Integer.MAX_VALUE));

			try(var transaction = Transaction.openOuter()) {
				var energyDrained = EmpUtil.drainPlayerEnergyRandomly(player, energyToDrain, transaction);
				if (energyDrained > 0) {
					transaction.commit();
					amount *= Math.lerp(1f, RadicalEffects.energyDamageMin, (float) energyDrained / (float) energyToDrain);
				}
			}
		}

		if(damageSource.isIn(RadicalDamageTags.NO_DAMAGE)) {
			return 0;
		}
		return amount;
	}


}