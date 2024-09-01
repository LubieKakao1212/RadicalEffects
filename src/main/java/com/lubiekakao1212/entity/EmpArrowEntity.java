package com.lubiekakao1212.entity;

import com.lubiekakao1212.damage.RadicalDamageTypes;
import com.lubiekakao1212.item.EmpArrowItem;
import com.lubiekakao1212.item.RadicalItems;
import com.lubiekakao1212.network.packet.PacketClientAoeExplosion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EmpArrowEntity extends AoeArrowEntity {

    public EmpArrowEntity(EntityType<EmpArrowEntity> type, World world) {
        super(type, world);
    }

    public EmpArrowEntity(World world, LivingEntity owner) {
        super(RadicalEntities.EMP_ARROW, owner, world);
    }

    @Override
    protected void affectEntity(LivingEntity entity, float distanceRatio) {
        var damageSource = world.getDamageSources().create(RadicalDamageTypes.DAMAGE_EMP);
        entity.damage(damageSource, (1f - distanceRatio) * power * (float)getDamage());
    }

    @Override
    protected void affectEntityDirect(LivingEntity entity) {

    }

    @Override
    protected Record createExplodePacket(float radius, float strength) {
        return new PacketClientAoeExplosion(getPos(), radius);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(RadicalItems.EMP_ARROW);
    }
}
