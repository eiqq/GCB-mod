package com.eiqui.gcbmod.compat;

import com.eiqui.gcbmod.config.GcbOptionsScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return GcbOptionsScreen::new;
    }
}
