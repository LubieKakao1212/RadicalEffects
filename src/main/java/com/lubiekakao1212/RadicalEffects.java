package com.lubiekakao1212;

import com.lubiekakao1212.apilookup.IEmpLevel;
import com.lubiekakao1212.config.RadicalConfigCommon;
import com.lubiekakao1212.effects.RadicalStatusEffects;
import com.lubiekakao1212.entity.RadicalEntities;
import com.lubiekakao1212.item.RadicalItems;
import com.lubiekakao1212.network.RadicalNetwork;
import com.lubiekakao1212.recipe.RadicalRecipes;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RadicalEffects implements ModInitializer {

	public static final RadicalConfigCommon CONFIG = RadicalConfigCommon.createAndLoad();

	public static final int powerDamageScale = 200000;
	public static final float energyDamageMin = 0.5f;

	public static final float volatileKnockbackScale = 0.05f;
	public static final float volatileKnockbackDurationStep = 400f;
	public static final float volatileDamageScale = 4f;
	public static final float volatileDurationDecay = 0.5f;

	public static final float markDamageBoost = 0.25f;

	public static final String MODID = "radical-effects";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("radical-effects");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		//LOGGER.info("Hello Fabric world!");
		RadicalStatusEffects.init();

		RadicalNetwork.init();

		FieldRegistrationHandler.register(RadicalItems.class, MODID, false);
		FieldRegistrationHandler.register(RadicalEntities.class, MODID, false);

		RadicalRecipes.init();

		IEmpLevel.init();
	}
}