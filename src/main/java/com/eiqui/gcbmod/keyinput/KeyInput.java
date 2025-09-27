package com.eiqui.gcbmod.keyinput;

import com.eiqui.gcbmod.network.StringPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.*;

public class KeyInput {
    public static final String HEADER = "KEYINPUT";
    private static List<KeyBinding> KEYS = new ArrayList<>();
    private static Map<KeyBinding, Boolean> IS_PRESSED = new HashMap<>();

    public static void Initialize() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            run();
        });
    }

    public static void run(){
        //KEYS.add(InputUtil.fromTranslationKey("key.keyboard.0"));

        // ZXCV 키 추가
        KeyBinding z = new KeyBinding("key.keyboard.z",
                InputUtil.fromTranslationKey("key.keyboard.z").getCode(), "category.GCB");
        KeyBindingHelper.registerKeyBinding(z);
        KEYS.add(z);

        KeyBinding x = new KeyBinding("key.keyboard.x",
                InputUtil.fromTranslationKey("key.keyboard.x").getCode(), "category.GCB");
        KeyBindingHelper.registerKeyBinding(x);
        KEYS.add(x);

        KeyBinding c = new KeyBinding("key.keyboard.c",
                InputUtil.fromTranslationKey("key.keyboard.c").getCode(), "category.GCB");
        KeyBindingHelper.registerKeyBinding(c);
        KEYS.add(c);

        KeyBinding v = new KeyBinding("key.keyboard.v",
                InputUtil.fromTranslationKey("key.keyboard.v").getCode(), "category.GCB");
        KeyBindingHelper.registerKeyBinding(v);
        KEYS.add(v);

        KeyBinding k0 = new KeyBinding("key.keyboard.0",
                InputUtil.fromTranslationKey("key.keyboard.0").getCode(), "category.GCB");
        KeyBindingHelper.registerKeyBinding(k0);
        KEYS.add(k0);


        //KEYS.add(new KeyBinding("key.keyboard.left.control", InputUtil.fromTranslationKey("key.keyboard.left.control").getCode(), "gcb.custom"));

        // 숫자 1~9 키 추가
        Collections.addAll(KEYS, MinecraftClient.getInstance().options.hotbarKeys);

        // 키 바인딩 등록
        KEYS.add(MinecraftClient.getInstance().options.attackKey);
        KEYS.add(MinecraftClient.getInstance().options.useKey);
        KEYS.add(MinecraftClient.getInstance().options.pickItemKey);

        KEYS.add(MinecraftClient.getInstance().options.dropKey);

        for(KeyBinding key : KEYS){
            IS_PRESSED.put(key,false);
        }

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.currentScreen == null) {
                for(KeyBinding key : KEYS){
                    boolean pushed = key.isPressed();
                    if(IS_PRESSED.getOrDefault(key,false) != pushed){
                        IS_PRESSED.put(key,pushed);
                        processInput(key,pushed);
                    }
                }

            }
        });
    }

    private static void processInput(KeyBinding key, boolean ispushed) {
        ClientPlayNetworking.send(new StringPayload(HEADER + ":" + key.getTranslationKey() + ":" + ispushed));
    }

}
