package com.eiqui.gcbmod.playerControl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class Velocity {

    public static void receivePacket(String data){
        try {
            String[] v = data.split(",");
            double x = Double.parseDouble(v[0]);
            double y = Double.parseDouble(v[1]);
            double z = Double.parseDouble(v[2]);
            push(x,y,z);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void push(double x,double y,double z) {
        if(MinecraftClient.getInstance().player != null){
            MinecraftClient.getInstance().player.setVelocity(new Vec3d(x,y,z));
        }
    }

}
