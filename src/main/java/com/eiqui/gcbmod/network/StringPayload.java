package com.eiqui.gcbmod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import static com.eiqui.gcbmod.Gcbmod.GCB_IDENTIFIER;

public class StringPayload implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<StringPayload> TYPE = CustomPacketPayload.createType(GCB_IDENTIFIER);
    private final String data;

    public StringPayload(String data) {
        this.data = data;
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public String getData() {
        return data;
    }

    public static void encode(StringPayload payload,FriendlyByteBuf buffer) {
        buffer.writeUtf(payload.data);
    }

    public static StringPayload decode(FriendlyByteBuf buffer) {
        return new StringPayload(buffer.readUtf());
    }
}
