package com.eiqui.gcbmod.particle.shape;

import com.eiqui.gcbmod.particle.Partic;
import com.eiqui.gcbmod.utils.Vector;

import java.util.UUID;

import static com.eiqui.gcbmod.particle.Parser.parseVector;
import static com.eiqui.gcbmod.utils.NumberConversions.round;

public class Line extends ShapeImpl implements Periodable{
    protected double period = 1d;

    @Override
    public double getPeriod() {
        return period;
    }
    @Override
    public void setPeriod(double period) {
        this.period = period;
    }
    public double getDensity() {
        return period;
    }
    public void setDensity(double period) {
        this.period = period;
    }

    public Line(Partic particle) {
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

        Vector[] result = new Vector[loop+1];

        v = transform(v);
        for(int i = 0;i <= loop;i++) {
            result[i] = start.clone();
            start.add(v);
        }
        print(result,positions[0]);
    }

    public static Line deserialize(String[] data,Partic particle) {
        //index 0 is header
        Line line = new Line(particle);
        line.setPeriod(Double.parseDouble(data[1]));

        line.setExpand(Double.parseDouble(data[2])); // matrix generic
        line.setTime(Double.parseDouble(data[3]));
        line.setMatrix(parseVector(data[4]),parseVector(data[5]),parseVector(data[6]));
        if(!data[7].equals("0")){
            line.setTarget(UUID.fromString(data[7]));
        }

        for(int i = 8;i < data.length;i++) { //locations to draw
            line.positions.add(parseVector(data[i]));
        }
        return line;
    }
}
