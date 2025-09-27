package com.eiqui.gcbmod.particle;

import com.eiqui.gcbmod.utils.Vector;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

import static com.eiqui.gcbmod.particle.Parser.parseBlockState;
import static com.eiqui.gcbmod.particle.Parser.parseVector;

public class Partic {
    private final Random random = Random.createThreadSafe();

    public Vector offset = new Vector(0,0,0);
    public int count = 0;
    public double speed = 0;
    public String data;
    public boolean force = false;
    private ParticleEffect particleEffect;

    public Partic() {}

    public Partic(String particle, Vector offset, int count, double speed, boolean force, String data){
        this.offset = offset;
        this.count = count;
        this.speed = speed;
        this.data = data;
        this.force = force;

        ParticleType<?> particleType =
                Registries.PARTICLE_TYPE.get(Identifier.of("minecraft",particle.toLowerCase()));
        if(particleType == null) {
            return;
        }
        String[] datas = data.split(",");
        if(particleType.equals(ParticleTypes.DUST_COLOR_TRANSITION)) {
            particleEffect = new DustColorTransitionParticleEffect(
                    Integer.parseInt(datas[0]),
                   Integer.parseInt(datas[1]),
                    Float.parseFloat(datas[2]));
        }else if(particleType.equals(ParticleTypes.TINTED_LEAVES)) {
            particleEffect = TintedParticleEffect.create(ParticleTypes.TINTED_LEAVES,Integer.parseInt(datas[0]));
        }else if(particleType.equals(ParticleTypes.ENTITY_EFFECT)) {
            particleEffect = TintedParticleEffect.create(ParticleTypes.ENTITY_EFFECT,Integer.parseInt(datas[0]));
        }else if(particleType.equals(ParticleTypes.SCULK_CHARGE)) {
            particleEffect = new SculkChargeParticleEffect(Float.parseFloat(datas[0]));
        }else if(particleType.equals(ParticleTypes.SHRIEK)) {
            particleEffect = new ShriekParticleEffect(Integer.parseInt(datas[0]));
        }else if(particleType.equals(ParticleTypes.BLOCK) ||
                particleType.equals(ParticleTypes.BLOCK_CRUMBLE) ||
                particleType.equals(ParticleTypes.BLOCK_MARKER) ||
                particleType.equals(ParticleTypes.DUST_PILLAR) ||
                particleType.equals(ParticleTypes.FALLING_DUST)) {
            particleEffect =
                    new BlockStateParticleEffect((ParticleType<BlockStateParticleEffect>) particleType,
                            parseBlockState(datas[0]));
        }else{
            particleEffect = (SimpleParticleType) particleType;
        }
    }


    public void spawnParticle(Vector loc, Vector expension) {
        Vector t = offset.clone().add(expension);
        if (count == 0) {
            double x = speed * t.getX();
            double y = speed * t.getY();
            double z = speed * t.getZ();
            MinecraftClient.getInstance().execute(() -> {
                ClientWorld cw = MinecraftClient.getInstance().world;
                if(cw != null) {
                    cw.addParticleClient(particleEffect, force, force,
                            loc.getX(),loc.getY(),loc.getZ(),
                            x,y,z);
                }
            });
        } else {
            for(int i = 0; i < count; ++i) {
                double g = this.random.nextGaussian() * t.getX();
                double h = this.random.nextGaussian() * t.getY();
                double j = this.random.nextGaussian() * t.getZ();
                double k = this.random.nextGaussian() * speed;
                double l = this.random.nextGaussian() * speed;
                double m = this.random.nextGaussian() * speed;
                MinecraftClient.getInstance().execute(() -> {
                    ClientWorld cw = MinecraftClient.getInstance().world;
                    if(cw != null){
                        cw.addParticleClient(particleEffect, force, force,
                                loc.getX() + g, loc.getY() + h, loc.getZ() + j,
                                k, l, m);
                    }
                });
            }
        }
    }

    public static Partic deserialize(String[] data) {
        Partic partic = new Partic(
                data[0],
                parseVector(data[1]),
                Integer.parseInt(data[2]),
                Double.parseDouble(data[3]),
                Boolean.parseBoolean(data[4]),
                String.join(",",java.util.Arrays.copyOfRange(data, 5, data.length))
        );
        return partic;
    }

}
