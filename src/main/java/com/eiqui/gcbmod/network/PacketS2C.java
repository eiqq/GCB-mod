package com.eiqui.gcbmod.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class PacketS2C {
    public static void registerHandler() {
        StreamCodec<RegistryFriendlyByteBuf, StringPayload> myCodec =
                CustomPacketPayload.codec(StringPayload::encode, StringPayload::decode);

        // 클라이언트에서 서버로 보내는 플레이 채널에 페이로드 타입 등록
        PayloadTypeRegistry.serverboundPlay().register(StringPayload.TYPE, myCodec);

        // 서버에서 클라이언트로 보내는 플레이 채널에 페이로드 타입 등록
        PayloadTypeRegistry.clientboundPlay().register(StringPayload.TYPE, myCodec);

        ClientPlayNetworking.registerGlobalReceiver(StringPayload.TYPE, new StringPayloadHandler());
    }

}
