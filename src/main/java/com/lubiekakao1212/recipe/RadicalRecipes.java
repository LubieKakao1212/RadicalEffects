package com.lubiekakao1212.recipe;

import com.lubiekakao1212.RadicalEffects;
import com.lubiekakao1212.entity.RadicalEntities;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RadicalRecipes {

    public static final RecipeSerializer<EmpShardRecipe> EMP_SHARD = new SpecialRecipeSerializer<>(EmpShardRecipe::new);
    public static final RecipeSerializer<EmpArrowRecipe> EMP_ARROW = new SpecialRecipeSerializer<>(EmpArrowRecipe::new);

    public static void init() {
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(RadicalEffects.MODID, "emp_shard"), EMP_SHARD);
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(RadicalEffects.MODID, "emp_arrow"), EMP_ARROW);
    }

}
