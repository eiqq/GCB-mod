package com.eiqui.gcbmod.particle.shape;

import com.eiqui.gcbmod.particle.Partic;
import com.eiqui.gcbmod.utils.Vector;

import static com.eiqui.gcbmod.utils.NumberConversions.round;

public class Trail extends Line{

    public Trail(Partic particle) {
        super(particle);
    }

    @Override
    public void draw(Vector... positions) {
        if(positions.length < 2){
            return;
        }
        Vector start = positions[0].clone();
        Vector end = positions[1].clone();

        Vector v = end.clone().subtract(start);
        double distance = v.length();
        double density = Math.max(this.period,Vector.getEpsilon());
        int loop = round(distance/density);
        if(loop <= 0){
            return;
        }
        v.multiply(1d/loop);

        Vector[] result = new Vector[loop];
        v = transform(v);
        for(int i = 0;i < loop;i++) {
            result[i] = start.clone();
            start.add(v);
        }

        print(result,positions[0]);
    }
}
