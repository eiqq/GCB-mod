package com.eiqui.gcbmod.network;

import com.eiqui.gcbmod.particle.Parser;
import com.eiqui.gcbmod.playerControl.Velocity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import static com.eiqui.gcbmod.Gcbmod.modVersion;

public class Packet {

    public static void receive(String data) {
        String someData = new String(data);
        String[] datas = someData.split(":");
        String header = datas[0];
        switch(header) {
            case "PARTICLE":
                Parser.Parse(datas);
                break;
            case "VELOCITY":
                Velocity.receivePacket(datas[1]);
                break;
            default:
                ClientPlayNetworking.send(new StringPayload(modVersion));
        }
    }


}
