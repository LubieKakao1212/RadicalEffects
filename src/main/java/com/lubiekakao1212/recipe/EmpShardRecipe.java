package com.lubiekakao1212.recipe;

import com.lubiekakao1212.item.EmpChargeItem;
import com.lubiekakao1212.item.EmpCrystalItem;
import com.lubiekakao1212.item.EmpShardItem;
import com.lubiekakao1212.item.RadicalItems;
import com.lubiekakao1212.util.EmpLevelUtil;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class EmpShardRecipe extends SpecialCraftingRecipe {

    public static final Ingredient EMP_CRYSTAL = Ingredient.ofItems(RadicalItems.EMP_CRYSTAL);
    public static final Ingredient EMP_ARROW = Ingredient.ofItems(RadicalItems.EMP_ARROW);

    public EmpShardRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    /**
     * {@return whether this recipe matches the contents inside the
     * {@code inventory} in the given {@code world}}
     *
     * <p>The {@code world} currently is only used by the map cloning recipe to
     * prevent duplication of explorer maps.
     *
     * @param inventory the input inventory
     * @param world     the input world
     */
    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        boolean hasCrystal = false;

        for (int j = 0; j < inventory.size(); j++) {
            ItemStack itemStack = inventory.getStack(j);
            if (!itemStack.isEmpty()) {
                if (EMP_CRYSTAL.test(itemStack)) {
                    if (hasCrystal) {
                        return false;
                    }

                    var level = EmpLevelUtil.levelFromEnergy(itemStack.get(EmpCrystalItem.ENERGY_KEY));

                    if(level > 0) {
                        hasCrystal = true;
                        continue;
                    }
                    return false;
                }
                return false;
            }
        }

        return hasCrystal;
    }

    /**
     * Crafts this recipe.
     *
     * <p>This method does not perform side effects on the {@code inventory}.
     *
     * <p>This method should return a new item stack on each call.
     *
     * @param inventory       the input inventory
     * @param registryManager
     * @return the resulting item stack
     */
    @Override
    public ItemStack craft(CraftingInventory inventory, DynamicRegistryManager registryManager) {
        //TODO unhardcode 64
        var result = new ItemStack(RadicalItems.EMP_SHARD, 64);
        long level = -1;

        for (int j = 0; j < inventory.size(); j++) {
            var stack = inventory.getStack(j);
            if(!stack.isEmpty()) {
                level = EmpLevelUtil.levelFromEnergy(stack.get(EmpCrystalItem.ENERGY_KEY));
                break;
            }
        }

        result.put(EmpShardItem.LEVEL_KEY, level);
        return result;
    }

    /**
     * {@return whether this recipe will fit into the given grid size}
     *
     * <p>This is currently only used by recipe book.
     *
     * @param width  the width of the input inventory
     * @param height the height of the input inventory
     */
    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    /**
     * {@return the serializer associated with this recipe}
     */
    @Override
    public RecipeSerializer<?> getSerializer() {
        return RadicalRecipes.EMP_SHARD;
    }
}
