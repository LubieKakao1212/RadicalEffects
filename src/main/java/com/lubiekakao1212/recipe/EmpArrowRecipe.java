package com.lubiekakao1212.recipe;

import com.lubiekakao1212.item.EmpShardItem;
import com.lubiekakao1212.item.RadicalItems;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class EmpArrowRecipe extends SpecialCraftingRecipe {

    public static final Ingredient EMP_SHARD = Ingredient.ofItems(RadicalItems.EMP_SHARD);
    public static final Ingredient STICK = Ingredient.ofItems(Items.STICK);
    public static final Ingredient FEATHER = Ingredient.ofItems(Items.FEATHER);

    public EmpArrowRecipe(Identifier id, CraftingRecipeCategory category) {
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
        boolean foundPattern = false;
        long level = -1;

        for (int i=0; i<inventory.getWidth(); i++) {
            var s0 = inventory.getStack(i);
            var s1 = inventory.getStack(i + inventory.getWidth());
            var s2 = inventory.getStack(i + 2 * inventory.getWidth());

            if(foundPattern && (!Ingredient.EMPTY.test(s0) || ! Ingredient.EMPTY.test(s1) || !Ingredient.EMPTY.test(s2))) {
                return false;
            }

            if(EMP_SHARD.test(s0) && STICK.test(s1) && FEATHER.test(s2)) {
                foundPattern = true;
                level = s0.get(EmpShardItem.LEVEL_KEY);
            }
        }

        return foundPattern && level > 0;
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
        var output = new ItemStack(RadicalItems.EMP_ARROW, 4);
        long level = -1;

        for (int i=0; i<inventory.getWidth(); i++) {
            var s = inventory.getStack(i);
            if(!s.isEmpty()) {
                level = s.get(EmpShardItem.LEVEL_KEY);
                break;
            }
        }
        output.put(EmpShardItem.LEVEL_KEY, level);

        return output;
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
        return width == 3 && height == 3;
    }

    /**
     * {@return the serializer associated with this recipe}
     */
    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
}
