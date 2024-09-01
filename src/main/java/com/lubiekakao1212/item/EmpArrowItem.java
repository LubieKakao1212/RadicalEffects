package com.lubiekakao1212.item;

import com.lubiekakao1212.entity.EmpArrowEntity;
import com.lubiekakao1212.entity.RadicalEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Objects;

public class EmpArrowItem extends ArrowItem {

    public EmpArrowItem(Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        var arrow = new EmpArrowEntity(world, shooter).initFromStack(stack);
        arrow.setOwner(shooter);
        return arrow;
    }
}
