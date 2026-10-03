package com.eiqui.gcbmod.compat;

import com.eiqui.gcbmod.config.GcbConfig;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.option.OptionFlag;
import net.caffeinemc.mods.sodium.api.config.option.OptionImpact;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/** Sodium 영상 설정에 GCB 페이지 추가(엔트리포인트 sodium:config_api_user) */
public class SodiumCompat implements ConfigEntryPoint {
    @Override
    public void registerConfigLate(ConfigBuilder builder) {
        builder.registerOwnModOptions()
                .setName("GCB")
                .addPage(builder.createOptionPage()
                        .setName(Component.translatable("gcbmod.options.title"))
                        .addOptionGroup(builder.createOptionGroup()
                                .addOption(builder.createBooleanOption(Identifier.fromNamespaceAndPath("gcbmod", "direct_atlas_upload"))
                                        .setName(Component.translatable("gcbmod.options.directAtlasUpload"))
                                        .setTooltip(Component.translatable("gcbmod.options.directAtlasUpload.tooltip"))
                                        .setImpact(OptionImpact.LOW)
                                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                                        .setDefaultValue(true)
                                        .setBinding(v -> GcbConfig.get().directAtlasUpload = v, () -> GcbConfig.get().directAtlasUpload)
                                        .setStorageHandler(GcbConfig::save))));
    }
}
