package com.lubiekakao1212.network;

import com.lubiekakao1212.RadicalEffects;
import com.lubiekakao1212.network.packet.PacketClientAoeExplosion;
import io.wispforest.owo.network.OwoNetChannel;
import net.minecraft.util.Identifier;

public class RadicalNetwork {

    public static final OwoNetChannel CHANNEL = OwoNetChannel.create(new Identifier(RadicalEffects.MODID));

    public static void init() {
        CHANNEL.registerClientboundDeferred(PacketClientAoeExplosion.class);

    }

}
