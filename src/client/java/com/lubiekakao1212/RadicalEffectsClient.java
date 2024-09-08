package com.lubiekakao1212;

import com.lubiekakao1212.entity.RadicalEntities;
import com.lubiekakao1212.entity.renderer.EmpArrowRenderer;
import com.lubiekakao1212.event.RadicalEventsClient;
import com.lubiekakao1212.network.RadicalNetwork;
import com.lubiekakao1212.network.RadicalNetworkClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ArrowEntityRenderer;

public class RadicalEffectsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

		RadicalNetworkClient.init();
		EntityRendererRegistry.register(RadicalEntities.EMP_ARROW, EmpArrowRenderer::new);

		RadicalEventsClient.init();

	}
}