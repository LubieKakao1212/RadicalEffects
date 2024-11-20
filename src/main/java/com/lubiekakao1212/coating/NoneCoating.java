package com.lubiekakao1212.coating;

import net.minecraft.entity.LivingEntity;

import java.util.EnumSet;

public class NoneCoating implements ICoating {

    @Override
    public EnumSet<Designation> getDesignation() {
        return EnumSet.of(Designation.ITEM, Designation.ENTITY, Designation.ITEM_FOOD);
    }
}
