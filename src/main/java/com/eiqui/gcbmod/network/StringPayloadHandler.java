package com.eiqui.gcbmod.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class StringPayloadHandler implements ClientPlayNetworking.PlayPayloadHandler<StringPayload> {
    @Override
    public void receive(StringPayload payload, ClientPlayNetworking.Context context) {
        Packet.receive(payload.getData());
    }
}
