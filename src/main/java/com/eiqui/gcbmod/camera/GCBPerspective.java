package com.eiqui.gcbmod.camera;

import com.eiqui.gcbmod.network.StringPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.CameraType;

public class GCBPerspective {
    public static final String HEADER = "PERSPECTIVE";
    public static CameraType LAST = CameraType.FIRST_PERSON;

    public static void Initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                CameraType currentPerspective = Minecraft.getInstance().options.getCameraType();
                if(currentPerspective != LAST){
                    LAST = currentPerspective;
                    send(LAST);
                }
            }
        });
    }

    // 접속할 때마다 지금 시점을 알린다(바꿀 때만 보내면 3인칭으로 나갔다 들어온 경우 서버가 1인칭으로 안다)
    public static void sendCurrent() {
        LAST = Minecraft.getInstance().options.getCameraType();
        send(LAST);
    }

    // 서버가 정한 시점으로 바꾼다. F5 를 누른 것과 같게 처리하고, 바뀐 시점은 틱 훅이 서버로 다시 보낸다
    public static void set(String type) {
        Minecraft client = Minecraft.getInstance();
        CameraType before = client.options.getCameraType();
        client.options.setCameraType(CameraType.valueOf(type));
        if (before.isFirstPerson() != client.options.getCameraType().isFirstPerson()) {
            client.gameRenderer.checkEntityPostEffect(client.options.getCameraType().isFirstPerson() ? client.getCameraEntity() : null);
        }
    }

    private static void send(CameraType perspective) {
        ClientPlayNetworking.send(new StringPayload(HEADER + ":" + perspective.toString()));
    }
}
