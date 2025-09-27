package com.eiqui.gcbmod.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

import static com.eiqui.gcbmod.Gcbmod.GCB_IDENTIFIER;

public class PacketS2C {
    public static void registerHandler() {
        PacketCodec<RegistryByteBuf, StringPayload> myCodec =
                CustomPayload.codecOf(StringPayload::encode, StringPayload::decode);

        // 클라이언트에서 서버로 보내는 플레이 채널에 페이로드 타입 등록
        PayloadTypeRegistry.playC2S().register(CustomPayload.id(GCB_IDENTIFIER), myCodec);

        // 서버에서 클라이언트로 보내는 플레이 채널에 페이로드 타입 등록
        PayloadTypeRegistry.playS2C().register(CustomPayload.id(GCB_IDENTIFIER), myCodec);

        ClientPlayNetworking.registerGlobalReceiver(CustomPayload.id(GCB_IDENTIFIER), new StringPayloadHandler());
    }

}
