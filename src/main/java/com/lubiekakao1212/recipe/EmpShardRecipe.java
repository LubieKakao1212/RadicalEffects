package com.lubiekakao1212.recipe;

import com.lubiekakao1212.apilookup.IEmpLevel;
import com.lubiekakao1212.apilookup.IMutableEmpLevel;
import com.lubiekakao1212.item.EmpCrystalItem;
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

import java.util.Objects;

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

                    var level = Objects.requireNonNull(IEmpLevel.ITEM.find(itemStack, null)).getLevel(); //EmpLevelUtil.levelFromEnergy(itemStack.get(EmpCrystalItem.ENERGY_KEY));

                    if(level > 0) {
                        hasCrystal = true;
                        //not break
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
        var level = (IMutableEmpLevel)IEmpLevel.ITEM.find(result, null);
        assert level != null;

        for (int j = 0; j < inventory.size(); j++) {
            var stack = inventory.getStack(j);
            if(!stack.isEmpty()) {
                //emp crystal is the only item here
                level.setLevel(Objects.requireNonNull(IEmpLevel.ITEM.find(stack, null)).getLevel());
                return result;
            }
        }

        throw new RuntimeException("Invalid crafting: emp shard");
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
