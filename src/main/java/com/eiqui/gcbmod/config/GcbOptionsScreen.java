package com.eiqui.gcbmod.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

/** ModMenu 설정 화면 */
public class GcbOptionsScreen extends OptionsSubScreen {
    public GcbOptionsScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options, Component.translatable("gcbmod.options.title"));
    }

    @Override
    protected void addOptions() {
        list.addBig(GcbConfig.directAtlasUploadOption());
    }
}
