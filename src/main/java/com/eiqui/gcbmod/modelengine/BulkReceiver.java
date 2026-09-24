package com.eiqui.gcbmod.modelengine;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Display;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/** GCB 패치 ModelEngine 의 gcb:bulk_data 수신. 채널 등록 자체가 서버에 "GCB 모드 클라" 표시가 된다. 넷티 스레드에서 DataValue 까지 만들고 메인 스레드는 assign 만 한다. */
public final class BulkReceiver {
    private BulkReceiver() {}

    public record Entry(int entityId, List<DataValue<?>> values) {}

    public record Payload(Entry[] entries) implements CustomPacketPayload {
        public static final Type<Payload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("gcb", "bulk_data"));
        public static final StreamCodec<FriendlyByteBuf, Payload> CODEC = CustomPacketPayload.codec((p, buf) -> {}, BulkReceiver::decode);

        @Override
        public Type<Payload> type() {
            return TYPE;
        }
    }

    public static void register() {
        PayloadTypeRegistry.clientboundPlay().register(Payload.TYPE, Payload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(Payload.TYPE, (payload, ctx) -> apply(ctx.client().level, payload.entries()));
    }

    private static Payload decode(FriendlyByteBuf buf) {
        Entry[] entries = new Entry[buf.readVarInt()];
        for (int i = 0; i < entries.length; i++) {
            int entityId = buf.readVarInt();
            int mask = buf.readByte();
            List<DataValue<?>> v = new ArrayList<>(8);
            if ((mask & 0x0F) != 0) v.add(new DataValue<>(8, EntityDataSerializers.INT, 0)); // 변환이 하나라도 오면 보간 시작 = 이번 틱
            if ((mask & 1) != 0) v.add(new DataValue<>(11, EntityDataSerializers.VECTOR3, vec(buf)));
            if ((mask & 2) != 0) v.add(new DataValue<>(13, EntityDataSerializers.QUATERNION, quat(buf)));
            if ((mask & 4) != 0) v.add(new DataValue<>(12, EntityDataSerializers.VECTOR3, vec(buf)));
            if ((mask & 8) != 0) v.add(new DataValue<>(14, EntityDataSerializers.QUATERNION, quat(buf)));
            if ((mask & 16) != 0) v.add(new DataValue<>(9, EntityDataSerializers.INT, buf.readVarInt()));
            if ((mask & 32) != 0) {
                v.add(new DataValue<>(0, EntityDataSerializers.BYTE, buf.readByte()));
                v.add(new DataValue<>(22, EntityDataSerializers.INT, buf.readInt()));
            }
            if ((mask & 64) != 0) v.add(new DataValue<>(16, EntityDataSerializers.INT, buf.readInt()));
            if ((mask & 128) != 0) {
                v.add(new DataValue<>(15, EntityDataSerializers.BYTE, buf.readByte()));
                v.add(new DataValue<>(17, EntityDataSerializers.FLOAT, buf.readFloat()));
                v.add(new DataValue<>(24, EntityDataSerializers.BYTE, buf.readByte()));
            }
            entries[i] = new Entry(entityId, v);
        }
        return new Payload(entries);
    }

    private static Vector3f vec(FriendlyByteBuf buf) {
        return new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
    }

    private static Quaternionf quat(FriendlyByteBuf buf) {
        return new Quaternionf(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
    }

    private static void apply(ClientLevel level, Entry[] entries) {
        if (level == null) return;
        for (Entry e : entries) {
            if (level.getEntity(e.entityId()) instanceof Display d) d.getEntityData().assignValues(e.values());
        }
    }
}
