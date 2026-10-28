package com.eiqui.gcbmod.network;

import com.eiqui.gcbmod.Gcbmod;
import com.eiqui.gcbmod.camera.GCBPerspective;
import com.eiqui.gcbmod.mixin.AbstractClientPlayerAccessor;
import com.eiqui.gcbmod.particle.Parser;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;

import static com.eiqui.gcbmod.Gcbmod.LOGGER;
import static com.eiqui.gcbmod.Gcbmod.modVersion;

public class Packet {

    public static void receive(String data) {
        String someData = new String(data);
        String[] datas = someData.split(":");
        String header = datas[0];
        switch(header) {
            case "PARTICLE":
                try{
                    Parser.Parse(datas);
                }catch (Exception e){
                    LOGGER.warn("Invalid particle packet: {} ({})",data,e.toString());
                }
                break;
            case "SKIN":
                // 서버가 내 PlayerInfo 를 새 스킨으로 다시 보냈다. 캐시된 옛 PlayerInfo 를 비우면 리스폰 없이 새로 읽는다
                if (Minecraft.getInstance().player != null) {
                    ((AbstractClientPlayerAccessor) Minecraft.getInstance().player).gcb$setPlayerInfo(null);
                }
                break;
            case "PERSPECTIVE":
                try{
                    GCBPerspective.set(datas[1]);
                }catch (Exception e){
                    LOGGER.warn("Invalid perspective packet: {} ({})",data,e.toString());
                }
                break;
            default:
                ClientPlayNetworking.send(new StringPayload(modVersion));
                Gcbmod.sendFeatures();
        }
    }


}
