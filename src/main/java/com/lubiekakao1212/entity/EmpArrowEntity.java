package com.lubiekakao1212.entity;

import com.lubiekakao1212.apilookup.IEmpLevel;
import com.lubiekakao1212.damage.RadicalDamageTypes;
import com.lubiekakao1212.item.EmpArrowItem;
import com.lubiekakao1212.item.RadicalItems;
import com.lubiekakao1212.network.packet.PacketClientAoeExplosion;
import com.lubiekakao1212.util.EmpUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Optional;

public class EmpArrowEntity extends AoeArrowEntity {

    public EmpArrowEntity(EntityType<EmpArrowEntity> type, World world) {
        super(type, world);
    }

    public EmpArrowEntity(World world, LivingEntity owner) {
        super(RadicalEntities.EMP_ARROW, owner, world);
    }

    @Override
    public AoeArrowEntity initFromStack(ItemStack stack) {
        super.initFromStack(stack);
        this.power = Optional.ofNullable(IEmpLevel.ITEM.find(stack, null)).map(IEmpLevel::getLevel).orElse(1L);
        return this;
    }

    @Override
    protected void affectEntity(LivingEntity entity, float distanceRatio) {
        var damageSource = world.getDamageSources().create(RadicalDamageTypes.DAMAGE_EMP);
        entity.damage(damageSource, (1f - distanceRatio) * power * (float)getDamage());

        if(entity instanceof PlayerEntity player) {
            if(player.canTakeDamage()) {
                try(var transaction = Transaction.openOuter()) {
                    EmpUtil.drainPlayerEnergyRandomlyByLevel(player, (int) power, transaction);

                    transaction.commit();
                }
            }
        }
    }

    @Override
    protected void affectEntityDirect(LivingEntity entity) {

    }

    @Override
    protected Record createExplodePacket(float radius, float strength) {
        return new PacketClientAoeExplosion(getPos(), radius);
    }
}
