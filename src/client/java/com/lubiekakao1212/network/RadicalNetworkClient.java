package com.lubiekakao1212.network;

import com.lubiekakao1212.network.packet.PacketClientAoeExplosion;
public class RadicalNetworkClient {

    public static void init() {
        var net = RadicalNetwork.CHANNEL;
        net.registerClientbound(PacketClientAoeExplosion.class,PacketHandlersClient::handle);
    }

}
