package com.eiqui.gcbmod.keyinput;

import com.eiqui.gcbmod.network.StringPayload;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

import java.util.*;

public class KeyInput {
    public static final String HEADER = "KEYINPUT";
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("gcb", "gcb"));
    private static List<KeyMapping> KEYS = new ArrayList<>();
    private static Map<KeyMapping, Boolean> IS_PRESSED = new HashMap<>();

    public static void Initialize() {
        // ZXCV + 0 키 추가
        for (String name : new String[]{"key.keyboard.z", "key.keyboard.x", "key.keyboard.c", "key.keyboard.v", "key.keyboard.0"}) {
            KEYS.add(KeyMappingHelper.registerKeyMapping(new KeyMapping(name, InputConstants.getKey(name).getValue(), CATEGORY)));
        }

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            run();
        });
    }

    public static void run(){
        // 숫자 1~9 키 추가
        Collections.addAll(KEYS, Minecraft.getInstance().options.keyHotbarSlots);

        // 키 바인딩 등록
        KEYS.add(Minecraft.getInstance().options.keyAttack);
        KEYS.add(Minecraft.getInstance().options.keyUse);
        KEYS.add(Minecraft.getInstance().options.keyPickItem);
        KEYS.add(Minecraft.getInstance().options.keyDrop);

        for(KeyMapping key : KEYS){
            IS_PRESSED.put(key,false);
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.gui.screen() == null) {
                for(KeyMapping key : KEYS){
                    boolean pushed = key.isDown();
                    if(IS_PRESSED.getOrDefault(key,false) != pushed){
                        IS_PRESSED.put(key,pushed);
                        processInput(key,pushed);
                    }
                }
            }
        });
    }

    private static void processInput(KeyMapping key, boolean ispushed) {
        ClientPlayNetworking.send(new StringPayload(HEADER + ":" + key.getName() + ":" + ispushed));
    }

}
