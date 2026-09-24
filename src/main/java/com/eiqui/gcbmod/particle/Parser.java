package com.eiqui.gcbmod.particle;

import com.eiqui.gcbmod.particle.shape.*;
import com.eiqui.gcbmod.utils.Vector;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Parser {
    public static Vector parseVector(String input) {
        String[] datas = input.split(",");
        return new Vector(Double.parseDouble(datas[0]), Double.parseDouble(datas[1]), Double.parseDouble(datas[2]));
    }

    public static void Parse(String[] packet){
        String[] particData = Arrays.copyOfRange(packet, 1, 7);
        String[] shapeData = Arrays.copyOfRange(packet, 7, packet.length);
        Partic partic = Partic.deserialize(particData);
        Shape shape = null;
        switch (shapeData[0].toLowerCase()) {
            case "circle":
                shape = Circle.deserialize(shapeData,partic);
                break;
            case "line":
                shape = Line.deserialize(shapeData,partic);
                break;
            case "trail":
                shape = Trail.deserialize(shapeData,partic);
                break;
            case "sphere":
                shape = Sphere.deserialize(shapeData,partic);
                break;
            case "spiral":
                shape = Spiral.deserialize(shapeData,partic);
                break;
            case "spiralline":
                shape = SpiralLine.deserialize(shapeData,partic);
                break;
        }
        if(shape != null){
            ArrayList<Vector> points = shape.getPositions();
            Vector[] vec = new Vector[points.size()];
            for(int i = 0; i < points.size(); i++){
                vec[i] = points.get(i).clone();
            }
            shape.draw(vec);
        }
    }

    public static BlockState parseBlockState(String name){
        if (name == null || name.isEmpty()) return Blocks.AIR.defaultBlockState();
        name = name.toLowerCase(Locale.ROOT).trim();
        String full = name.contains(":") ? name : "minecraft:" + name;
        Identifier id = Identifier.tryParse(full);
        if (id == null) return Blocks.AIR.defaultBlockState(); // 파싱 실패 시 안전값
        var block = BuiltInRegistries.BLOCK.getValue(id);
        return block.defaultBlockState();
    }
}
