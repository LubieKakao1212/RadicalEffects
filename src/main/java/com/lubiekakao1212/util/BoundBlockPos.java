package com.lubiekakao1212.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public record BoundBlockPos(@NotNull BlockPos pos, @NotNull World world) {

}
