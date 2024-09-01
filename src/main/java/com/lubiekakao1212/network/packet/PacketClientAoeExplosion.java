package com.lubiekakao1212.network.packet;

import net.minecraft.util.math.Vec3d;

public record PacketClientAoeExplosion(Vec3d pos, double radius) { }
