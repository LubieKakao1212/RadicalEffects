package com.lubiekakao1212.commands;

import com.lubiekakao1212.coating.CoatingInstance;
import com.lubiekakao1212.coating.Coatings;
import com.lubiekakao1212.coating.container.ItemCoatingContainer;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.NbtCompoundArgumentType;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RadicalEffectsCommand {

    public static final SimpleCommandExceptionType INVALID_PERMISSION = new SimpleCommandExceptionType(Text.translatable("command.radical-effects.exception.permission"));
    public static final SimpleCommandExceptionType REQUIRES_ENTITY = new SimpleCommandExceptionType(Text.translatable("command.radical-effects.exception.living-only"));
    public static final SimpleCommandExceptionType ITEM_REQUIRED = new SimpleCommandExceptionType(Text.translatable("command.radical-effects.exception.requires-item"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {

        var command = literal("radeff");

        command = command.then(coatingSubCommand(literal("coating"), registryAccess));

        dispatcher.register(command);
    }

    public static LiteralArgumentBuilder<ServerCommandSource> coatingSubCommand(LiteralArgumentBuilder<ServerCommandSource> root, CommandRegistryAccess registryAccess) {
        root.then(
                literal("add").then(
                        argument("coating", RegistryEntryArgumentType.registryEntry(registryAccess, Coatings.REGISTRY_KEY)).then(
                                argument("uses", IntegerArgumentType.integer(0)).then(
                                       argument("nbt", NbtCompoundArgumentType.nbtCompound()).executes(
                                               RadicalEffectsCommand::executeAddCoating
                                       )
                                )
                        )
                )
        );
        return root;
    }

    public static int executeAddCoating(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {

        var source = ctx.getSource();
        validatePermission(source);

        if(source.getEntity() instanceof LivingEntity entity) {
            var stack = entity.getStackInHand(Hand.MAIN_HAND);

            if(stack == null) {
                throw ITEM_REQUIRED.create();
            }

            var coatingKey = RegistryEntryArgumentType.getRegistryEntry(ctx, "coating", Coatings.REGISTRY_KEY).registryKey();

            var coating = Coatings.REGISTRY.get(coatingKey);

            if(coating == null) {
                throw new RuntimeException("Something went wrong");
            }

            var uses = IntegerArgumentType.getInteger(ctx, "uses");
            var nbt = NbtCompoundArgumentType.getNbtCompound(ctx, "nbt");

            var coatings = new ItemCoatingContainer(stack);

            coatings.addCoating(new CoatingInstance(coating, uses, nbt));

            coatings.applyChanges();
        }
        else {
            throw REQUIRES_ENTITY.create();
        }

        return 1;
    }

    public static void validatePermission(ServerCommandSource source) throws CommandSyntaxException {
        if(!source.hasPermissionLevel(2)) {
            throw INVALID_PERMISSION.create();
        }
    }

}
