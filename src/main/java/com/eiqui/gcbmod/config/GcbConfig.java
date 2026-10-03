package com.eiqui.gcbmod.config;

import com.eiqui.gcbmod.Gcbmod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/** config/gcbmod.json */
public final class GcbConfig {
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("gcbmod.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /** 리소스를 불러올 때 스프라이트별 GPU 텍스처 없이 RAM 의 이미지를 아틀라스에 바로 쓴다(불러오는 순간 VRAM 약 절반) */
    public boolean directAtlasUpload = true;

    private static GcbConfig instance;

    public static GcbConfig get() {
        if (instance == null) {
            instance = new GcbConfig();
            try {
                if (Files.exists(PATH)) {
                    try (Reader r = Files.newBufferedReader(PATH)) {
                        GcbConfig read = GSON.fromJson(r, GcbConfig.class);
                        if (read != null) instance = read;
                    }
                }
            } catch (Exception e) {
                Gcbmod.LOGGER.warn("config/gcbmod.json 읽기 실패, 기본값 사용", e);
            }
            save();
        }
        return instance;
    }

    public static void save() {
        try (Writer w = Files.newBufferedWriter(PATH)) {
            GSON.toJson(instance, w);
        } catch (Exception e) {
            Gcbmod.LOGGER.warn("config/gcbmod.json 저장 실패", e);
        }
    }

    /** 값이 바뀌면 저장하고 리소스를 다시 불러온다 */
    public static void setDirectAtlasUpload(boolean value) {
        if (get().directAtlasUpload == value) return;
        instance.directAtlasUpload = value;
        save();
        Minecraft.getInstance().reloadResourcePacks();
    }

    public static OptionInstance<Boolean> directAtlasUploadOption() {
        return OptionInstance.createBoolean("gcbmod.options.directAtlasUpload",
                OptionInstance.cachedConstantTooltip(Component.translatable("gcbmod.options.directAtlasUpload.tooltip")),
                get().directAtlasUpload, GcbConfig::setDirectAtlasUpload);
    }
}
