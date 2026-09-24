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

    private static void send(CameraType perspective) {
        ClientPlayNetworking.send(new StringPayload(HEADER + ":" + perspective.toString()));
    }
}
