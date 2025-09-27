package com.eiqui.gcbmod.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;

import static com.eiqui.gcbmod.Gcbmod.GCB_IDENTIFIER;

public class StringPayload implements CustomPayload {
    private final Id<CustomPayload> id;
    private final String data;

    public StringPayload(String data) {
        this.id = CustomPayload.id(GCB_IDENTIFIER);
        this.data = data;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public static void encode(StringPayload payload,PacketByteBuf buffer) {
        buffer.writeString(payload.data);
    }

    public static StringPayload decode(PacketByteBuf buffer) {
        return new StringPayload(buffer.readString());
    }
}
