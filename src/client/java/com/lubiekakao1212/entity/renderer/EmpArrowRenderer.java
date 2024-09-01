package com.lubiekakao1212.entity.renderer;

import com.lubiekakao1212.entity.AoeArrowEntity;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class EmpArrowRenderer extends ProjectileEntityRenderer<AoeArrowEntity> {


    public EmpArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(AoeArrowEntity entity) {
        return ArrowEntityRenderer.TEXTURE;
    }
}
