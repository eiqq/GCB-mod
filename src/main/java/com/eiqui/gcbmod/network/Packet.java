package com.eiqui.gcbmod.network;

import com.eiqui.gcbmod.particle.Parser;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

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
            default:
                ClientPlayNetworking.send(new StringPayload(modVersion));
        }
    }


}
