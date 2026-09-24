package com.eiqui.gcbmod.particle;

import com.eiqui.gcbmod.utils.Vector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;

import static com.eiqui.gcbmod.particle.Parser.parseBlockState;
import static com.eiqui.gcbmod.particle.Parser.parseVector;

public class Partic {
    private final RandomSource random = RandomSource.createThreadSafe();

    public Vector offset = new Vector(0,0,0);
    public int count = 0;
    public double speed = 0;
    public String data;
    public boolean force = false;
    private ParticleOptions particleEffect;

    public Partic() {}

    public Partic(String particle, Vector offset, int count, double speed, boolean force, String data){
        this.offset = offset;
        this.count = count;
        this.speed = speed;
        this.data = data;
        this.force = force;

        ParticleType<?> particleType =
                BuiltInRegistries.PARTICLE_TYPE.getValue(Identifier.fromNamespaceAndPath("minecraft",particle.toLowerCase()));
        if(particleType == null) {
            return;
        }
        String[] datas = data.split(",");
        if(particleType.equals(ParticleTypes.DUST_COLOR_TRANSITION)) {
            particleEffect = new DustColorTransitionOptions(
                    Integer.parseInt(datas[0]),
                   Integer.parseInt(datas[1]),
                    Float.parseFloat(datas[2]));
        }else if(particleType.equals(ParticleTypes.TINTED_LEAVES)) {
            particleEffect = ColorParticleOption.create(ParticleTypes.TINTED_LEAVES,Integer.parseInt(datas[0]));
        }else if(particleType.equals(ParticleTypes.ENTITY_EFFECT)) {
            particleEffect = ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT,Integer.parseInt(datas[0]));
        }else if(particleType.equals(ParticleTypes.SCULK_CHARGE)) {
            particleEffect = new SculkChargeParticleOptions(Float.parseFloat(datas[0]));
        }else if(particleType.equals(ParticleTypes.SHRIEK)) {
            particleEffect = new ShriekParticleOption(Integer.parseInt(datas[0]));
        }else if(particleType.equals(ParticleTypes.BLOCK) ||
                particleType.equals(ParticleTypes.BLOCK_CRUMBLE) ||
                particleType.equals(ParticleTypes.BLOCK_MARKER) ||
                particleType.equals(ParticleTypes.DUST_PILLAR) ||
                particleType.equals(ParticleTypes.FALLING_DUST)) {
            particleEffect =
                    new BlockParticleOption((ParticleType<BlockParticleOption>) particleType,
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
            Minecraft.getInstance().execute(() -> {
                ClientLevel cw = Minecraft.getInstance().level;
                if(cw != null) {
                    cw.addParticle(particleEffect, force, force,
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
                Minecraft.getInstance().execute(() -> {
                    ClientLevel cw = Minecraft.getInstance().level;
                    if(cw != null){
                        cw.addParticle(particleEffect, force, force,
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
