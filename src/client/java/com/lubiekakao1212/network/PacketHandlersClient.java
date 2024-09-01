package com.lubiekakao1212.network;

import com.lubiekakao1212.network.packet.PacketClientAoeExplosion;
import com.lubiekakao1212.qulib.math.mc.Vector3m;
import com.lubiekakao1212.qulib.random.RandomEx;
import io.wispforest.owo.network.ClientAccess;
import net.minecraft.particle.ParticleTypes;

public class PacketHandlersClient {

    private static final RandomEx particleRandom = new RandomEx();


    public static void handle(PacketClientAoeExplosion packet, ClientAccess access) {
        var thisPos = new Vector3m(packet.pos());
        var radius = packet.radius();
        var count = (int)(64 * (radius * radius * Math.pow(radius, 1f / 2f) / 5f));
        for (var i = 0; i < count; i++) {
            var onSphere = particleRandom.nextOnSphere(packet.radius());
            var r = Math.pow(particleRandom.nextFloat((float)radius), 1f / 6f);
            onSphere.mul(r);
            var pos = onSphere.add(thisPos);
            var world = access.player().clientWorld;
            //ParticleTypes.ENTITY_EFFECT
            access.player().clientWorld.addParticle(ParticleTypes.ENCHANTED_HIT, pos.x, pos.y, pos.z, 0,0,0);
        }
    }

}
