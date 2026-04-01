package com.eiqui.gcbmod;

import com.eiqui.gcbmod.camera.GCBPerspective;
import com.eiqui.gcbmod.keyinput.KeyInput;
import com.eiqui.gcbmod.network.PacketS2C;
import com.eiqui.gcbmod.network.StringPayload;
import net.gensokyoreimagined.megClientMod.network.BulkDataHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Gcbmod implements ModInitializer {
	public static final String MOD_ID = "gcbmod";
	public static final String modVersion = FabricLoader.getInstance()
			.getModContainer(MOD_ID).orElseThrow()
			.getMetadata().getVersion().getFriendlyString();
	public static final String GCB_IDENTIFIER = "gcb";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("GCB MOD LOADED");
		PacketS2C.registerHandler();
		BulkDataHandler.register();
		KeyInput.Initialize();
		GCBPerspective.Initialize();

		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			ClientPlayNetworking.send(new StringPayload(modVersion));
			sendMods();
		});
	}

	public void sendMods(){
		// 모든 설치된 모드를 순회한다.
		for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
			// 모드의 ID와 버전을 출력한다.
			String modid = "MODS:"
					+mod.getMetadata().getId().replace(":","").replace(" ","");
			ClientPlayNetworking.send(new StringPayload(modid));
		}
	}

}