package com.eiqui.gcbmod.particle.shape;

import com.eiqui.gcbmod.particle.Partic;
import com.eiqui.gcbmod.utils.Vector;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class ShapeImpl implements Shape {
    private static final long TICK_INTERVAL_MS = 20;
    private static final double TICK_INTERVAL_SEC = TICK_INTERVAL_MS / 1000.0d;
    private static final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "GCB-Particle-Scheduler");
                t.setDaemon(true);
                return t;
            });

    protected Partic particle;
    protected Vector[] matrix = {new Vector(1,0,0),new Vector(0,1,0),new Vector(0,0,1)};
    protected double time = 0;
    protected double expand = 0;
    protected UUID target;
    protected ArrayList<Vector> positions = new ArrayList<>();

    public ShapeImpl(Partic particle) {
        this.particle = particle;
    }

    public void setMatrix(Vector x, Vector y, Vector z) {
        this.matrix[0] = x.clone();
        this.matrix[1] = y.clone();
        this.matrix[2] = z.clone();
    }
    public void setTime(double time) {
        this.time = time;
    }
    public void setExpand(double expand) {
        this.expand = expand;
    }
    public void setTarget(UUID target) {
        this.target = target;
    }

    public Vector[] getMatrix() {
        return new Vector[]{matrix[0].clone(),matrix[1].clone(),matrix[2].clone()};
    }
    public double getTime() {
        return time;
    }
    public double getExpand() {return expand;
    }

    public ArrayList<Vector> getPositions() {
        return new ArrayList<>(positions);
    }

    public Vector transform(Vector coord) {
        Vector x = this.matrix[0].clone().multiply(coord.getX());
        Vector y = this.matrix[1].clone().multiply(coord.getY());
        Vector z = this.matrix[2].clone().multiply(coord.getZ());
        return x.add(y.add(z));
    }

    protected void print(Vector[] result,Vector center){
        int size = result.length;
        if(time <= 0 || size <= 1){
            if(target != null){
                ClientWorld cw = MinecraftClient.getInstance().world;
                Entity realTarget = cw.getEntity(target);
                if(realTarget != null){
                    Vector targetL = new Vector(realTarget.getX(),realTarget.getY(),realTarget.getZ());
                    for(Vector vector : result) {
                        Vector expandFactor = vector.clone().subtract(center).multiply(expand);
                        particle.spawnParticle(vector.clone().add(targetL),expandFactor);
                    }
                }
            }else{
                for(Vector vector : result) {
                    Vector expandFactor = vector.clone().subtract(center).multiply(expand);
                    particle.spawnParticle(vector,expandFactor);
                }
            }
            return;
        }

        final double loopPerTick = size/(time/TICK_INTERVAL_SEC);
        final ScheduledFuture<?>[] futureHolder = new ScheduledFuture<?>[1];
        futureHolder[0] = scheduler.scheduleAtFixedRate(new Runnable() {
            int i = 0;
            double count = loopPerTick;
            @Override
            public void run() {
                if(target != null){
                    ClientWorld cw = MinecraftClient.getInstance().world;
                    Entity realTarget = cw.getEntity(target);
                    if(realTarget != null){
                        Vector targetL = new Vector(realTarget.getX(),realTarget.getY(),realTarget.getZ());
                        for(; i < count; i++) {
                            if (i >= size) {
                                futureHolder[0].cancel(false);
                                return;
                            }
                            Vector vector = result[i];
                            Vector expandFactor = vector.clone().subtract(center).multiply(expand);
                            particle.spawnParticle(vector.clone().add(targetL),expandFactor);
                        }
                    }
                }else{
                    for(; i < count; i++) {
                        if (i >= size) {
                            futureHolder[0].cancel(false);
                            return;
                        }
                        Vector vector = result[i];
                        Vector expandFactor = vector.clone().subtract(center).multiply(expand);
                        particle.spawnParticle(vector,expandFactor);
                    }
                }
                count += loopPerTick;
            }
        }, 0, TICK_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

}
