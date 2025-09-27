package com.eiqui.gcbmod.camera;

import com.eiqui.gcbmod.network.StringPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;

public class GCBPerspective {
    public static final String HEADER = "PERSPECTIVE";
    public static Perspective LAST = Perspective.FIRST_PERSON;

    public static void Initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                Perspective currentPerspective = MinecraftClient.getInstance().options.getPerspective();
                if(currentPerspective != LAST){
                    LAST = currentPerspective;
                    send(LAST);
                }
            }
        });
    }

    private static void send(Perspective perspective) {
        ClientPlayNetworking.send(new StringPayload(HEADER + ":" + perspective.toString()));
    }
}
