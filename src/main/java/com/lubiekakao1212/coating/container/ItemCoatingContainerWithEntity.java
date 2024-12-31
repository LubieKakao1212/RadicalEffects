package com.lubiekakao1212.coating.container;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemCoatingContainerWithEntity extends ItemCoatingContainer {

    private final LivingEntity entity;

    public ItemCoatingContainerWithEntity(@NotNull LivingEntity entity, @NotNull EquipmentSlot slot) {
        this(entity.getEquippedStack(slot).copy(), entity);
    }

    public ItemCoatingContainerWithEntity( @NotNull ItemStack stack, @NotNull LivingEntity entity) {
        super(stack);
        this.entity = entity;
    }


    @NotNull
    public LivingEntity getOwningEntity() {
        return entity;
    }

    /**
     * Does not modify the entity
     */
    @Override
    public void applyChanges() {
        super.applyChanges();
    }
}
