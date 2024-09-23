package com.lubiekakao1212.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.command.TagCommand;
import net.minecraft.util.JsonHelper;

public class RJsonHelper {

    public static ItemStack asItemStack(JsonObject obj) {
        var item = JsonHelper.getItem(obj, "item");
        var amount = JsonHelper.getInt(obj, "count");

        return new ItemStack(item, amount);
    }

    public static ItemStack asItemStackWithNbt(JsonObject obj) {
        var stack = asItemStack(obj);

        try {
            var nbt = NbtHelper.fromNbtProviderString(JsonHelper.getString(obj, "data", "{}"));

            stack.setNbt(nbt);
        } catch (CommandSyntaxException e) {
            throw new JsonParseException(e);
        }

        return stack;
    }

}
