package com.lubiekakao1212.entity;

import io.wispforest.owo.registration.reflect.EntityRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class RadicalEntities implements EntityRegistryContainer {

    public static final EntityType<EmpArrowEntity> EMP_ARROW = FabricEntityTypeBuilder.<EmpArrowEntity>create(SpawnGroup.MISC, EmpArrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f,0.5f))
            .trackRangeChunks(4)
            .trackedUpdateRate(20)
            .build();
}
