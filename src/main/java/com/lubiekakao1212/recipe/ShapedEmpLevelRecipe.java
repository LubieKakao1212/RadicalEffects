package com.lubiekakao1212.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.lubiekakao1212.apilookup.IEmpLevel;
import com.lubiekakao1212.apilookup.IMutableEmpLevel;
import com.lubiekakao1212.util.RJsonHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.lubiekakao1212.util.CollectionUtil.indexOfFirst;

public class ShapedEmpLevelRecipe implements CraftingRecipe {

    private final Identifier id;
    private final CraftingRecipeCategory category;
    private final byte[] pattern;
    private final List<Ingredient> ingredients;
    private final ItemStack result;
    private final int width, height;
    private final int minLevel;
    private final String group;

    public ShapedEmpLevelRecipe(Identifier id, String group, CraftingRecipeCategory category, byte[] pattern, List<Ingredient> ingredients, ItemStack result, int width, int height, int minLevel) {
        this.id = id;
        this.category = category;
        this.pattern = pattern;
        this.ingredients = ingredients;
        this.result = result;
        this.width = width;
        this.height = height;
        this.minLevel = minLevel;
        this.group = group;
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return category;
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
        var w = inventory.getWidth() - width + 1;
        var h = inventory.getHeight() - height + 1;

        for(int x=0; x<w; x++)
            for(int y=0; y<h; y++) {
                if(matchPattern(inventory, x, y)) {
                    return true;
                }
            }

        return false;
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
        var result = this.result.copy();

        var l = IEmpLevel.ITEM.find(result, null);
        if(l instanceof IMutableEmpLevel mLevel) {
            for(int i=0; i<inventory.size(); i++) {
                var stack = inventory.getStack(i);
                var level = IEmpLevel.ITEM.find(stack, null);
                if(level != null) {
                    mLevel.setLevel(level.getLevel());
                    break;
                }
            }
        }

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
        return width >= this.width && height >= this.height;
    }

    /**
     * {@return a preview of the recipe's output}
     *
     * <p>The returned stack should not be modified. To obtain the actual output,
     * call {@link #craft(CraftingInventory, DynamicRegistryManager)}.
     *
     * @param registryManager
     */
    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return result;
    }

    /**
     * {@return the ID of this recipe}
     */
    @Override
    public Identifier getId() {
        return id;
    }

    /**
     * {@return a group this recipe belongs in, or an empty string} This is
     * only used by the recipe book.
     *
     * <p>The group string is arbitrary, and is not rendered anywhere; in
     * the recipe book, recipes with the same group will belong to the same
     * cell in the grid of recipes. If the string is empty, this recipe will
     * belong to its own cell.
     */
    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    /**
     * {@return the serializer associated with this recipe}
     */
    @Override
    public RecipeSerializer<?> getSerializer() {
        return RadicalRecipes.SHAPED_EMP;
    }

    private boolean matchPattern(CraftingInventory inv, int x, int y) {
        var empLevel = -1L;

        for (int i=0; i<inv.getWidth(); i++)
            for (int j=0; j<inv.getHeight(); j++) {
                var i1 = i - x;
                var j1 = j - y;

                var ing = Ingredient.EMPTY;

                if(i1>=0 && j1>=0 && i1<width && j1<height) {
                    ing = getIngredientAt(i1, j1);
                }

                var stack = inv.getStack(i + j * inv.getWidth());

                if(!ing.test(stack)) {
                    return false;
                }

                var itemEmp = IEmpLevel.ITEM.find(stack, null);
                if(itemEmp != null) {
                    if(empLevel < 0) {
                        empLevel = itemEmp.getLevel();
                    }
                    else if(empLevel != itemEmp.getLevel()) {
                        return false;
                    }
                }
            }

        return empLevel >= minLevel;
    }

    public Ingredient getIngredientAt(int x, int y) {
        var idx = pattern[x + y * width];
        if(idx >= 0) {
            return ingredients.get(idx);
        }
        return Ingredient.EMPTY;
    }

    public static class Serializer implements RecipeSerializer<ShapedEmpLevelRecipe> {

        private static class Data {
            public String group;
            public String category;
            public Map<Character, JsonObject> key;
            public List<String> pattern;
            public JsonObject result;
            public int minLevel;
        }

        @Override
        public ShapedEmpLevelRecipe read(Identifier id, JsonObject json) {
            var data = new Gson().fromJson(json, Data.class);

            String group = JsonHelper.getString(json, "group", "");
            CraftingRecipeCategory category = (CraftingRecipeCategory)CraftingRecipeCategory.CODEC
                    .byId(data.category, CraftingRecipeCategory.MISC);

            int width = -1;
            int height = data.pattern.size();

            char[][] rawPattern = new char[height][];

            for (int i = 0; i < rawPattern.length; i++) {
                var row = data.pattern.get(i);
                if(width == -1) {
                    width = row.length();
                }
                else if(row.length() != width) {
                    throw new JsonParseException("Could not determine rawPattern size!!");
                }

                rawPattern[i] = row.toCharArray();
            }

            var ingredients = data.key.entrySet().stream()
                    .map(entry -> Map.entry(entry.getKey(), Ingredient.fromJson(entry.getValue())))
                    .toList();

            byte[] pattern = new byte[width * height];

            for(int i=0; i<height; i++)
                for(int j=0; j<width; j++) {
                    var c = rawPattern[i][j];
                    if(c == ' ') {
                        pattern[j + i * width] = -1;
                    }
                    else {
                        var ingIdx = indexOfFirst(ingredients, entry -> entry.getKey().equals(c));
                        if(ingIdx < 0) {
                            throw new JsonParseException("Invalid key in pattern!!");
                        }
                        pattern[j + i * width] = (byte) ingIdx;
                    }
                }

            var ingredients2 = ingredients.stream().map(Map.Entry::getValue).toList();

            return new ShapedEmpLevelRecipe(
                    id, data.group, category,
                    pattern, ingredients2,
                    RJsonHelper.asItemStack(data.result),
                    width, height,
                    data.minLevel);
        }

        /**
         * Reads a recipe from a packet byte buf, usually on the client.
         *
         * <p>This can throw whatever exception the packet byte buf throws. This may be
         * called in the netty event loop than the client game engine thread.
         *
         * @param id  the recipe's ID
         * @param buf the recipe buf
         * @return the read recipe
         */
        @Override
        public ShapedEmpLevelRecipe read(Identifier id, PacketByteBuf buf) {
            var width = buf.readVarInt();
            var height = buf.readVarInt();
            var minLevel = buf.readVarInt();

            var group = buf.readString();
            var category = buf.readEnumConstant(CraftingRecipeCategory.class);

            byte[] pattern = new byte[width * height];

            for (int i =0; i<pattern.length; i++) {
                pattern[i] = buf.readByte();
            }

            var ingCount = buf.readVarInt();

            var ingredients = new ArrayList<Ingredient>(ingCount);

            for(int i=0; i<ingCount; i++) {
                ingredients.add(Ingredient.fromPacket(buf));
            }

            var result = buf.readItemStack();

            return new ShapedEmpLevelRecipe(
                    id, group, category,
                    pattern, ingredients, result,
                    width, height, minLevel);
        }

        /**
         * Writes a recipe to a packet byte buf, usually on the server.
         *
         * <p>The recipe's ID is already written into the buf when this is called.
         *
         * <p>This can throw whatever exception the packet byte buf throws. This may be
         * called in the netty event loop than the server game engine thread.
         *
         * @param buf    the recipe buf
         * @param recipe the recipe
         */
        @Override
        public void write(PacketByteBuf buf, ShapedEmpLevelRecipe recipe) {
            buf.writeVarInt(recipe.width);
            buf.writeVarInt(recipe.height);
            buf.writeVarInt(recipe.minLevel);

            buf.writeString(recipe.group);
            buf.writeEnumConstant(recipe.category);


            for (var idx : recipe.pattern) {
                buf.writeByte(idx);
            }

            buf.writeVarInt(recipe.ingredients.size());
            for (var ing : recipe.ingredients) {
                ing.write(buf);
            }

            buf.writeItemStack(recipe.result);
        }
    }
}
