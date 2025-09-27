package com.eiqui.gcbmod.particle.shape;

import com.eiqui.gcbmod.particle.Partic;
import com.eiqui.gcbmod.utils.Vector;

import java.util.UUID;

import static com.eiqui.gcbmod.particle.Parser.parseVector;
import static com.eiqui.gcbmod.utils.NumberConversions.round;
import static com.eiqui.gcbmod.utils.NumberConversions.square;

public class Spiral extends Circle implements Periodable{
    private double period = 1d;
    private double increment = 0d;
    private boolean isConverge = true;

    public Spiral(Partic particle) {
        super(particle);
    }

    public boolean isConverge() {
        return isConverge;
    }
    public void setConverge(boolean converge) {
        isConverge = converge;
    }
    public double getPeriod() {
        return period;
    }
    public void setPeriod(double period) {
        this.period = period;
    }
    public double getIncrement() {
        return increment;
    }
    public void setIncrement(double increment) {
        this.increment = increment;
    }

    @Override
    public void draw(Vector... positions) {
        if(positions.length < 1){
            return;
        }
        Vector center = positions[0];
        int points = this.points;
        if(points <= 0){
            return;
        }

        double rotateRadian = Math.toRadians(360d/(double)points);
        Vector spinVector = rotate.clone();
        double radius = spinVector.length();
        double adder = period/points;
        double inc = increment/square(points);
        double incCount = 0;
        int loop = round(radius*points/period);
        Vector[] result = new Vector[loop];
        int N = 0;
        if(isConverge){
            for(int i = 0;i < loop;i++) {
                result[i] = transform(spinVector).add(center);
                spinVector.rotateAroundAxis(axis, rotateRadian);
                radius -= adder;
                radius -= incCount;
                incCount += inc;
                N++;
                if(radius < 0){
                    break;
                }
                if(!spinVector.isZero()){
                    spinVector.normalize().multiply(radius);
                }
            }
        }else{
            radius = Vector.getEpsilon();
            double finalRadius = spinVector.length()+Vector.getEpsilon();
            for(int i = 0;i < loop;i++) {
                if(!spinVector.isZero()){
                    spinVector.normalize().multiply(radius);
                }
                if(radius > finalRadius){
                    break;
                }
                result[i] = transform(spinVector).add(center);
                spinVector.rotateAroundAxis(axis, rotateRadian);
                radius += adder;
                radius += incCount;
                incCount += inc;
                N++;
            }
        }
        Vector[] result2 = new Vector[N];
        System.arraycopy(result, 0, result2, 0, N);
        print(result2,center);
    }

    public static Spiral deserialize(String[] data,Partic particle) {
        //index 0 is header
        Spiral spiral = new Spiral(particle);
        spiral.setAxis(parseVector(data[1]));
        spiral.setRotate(parseVector(data[2]));
        spiral.setPoints(Integer.parseInt(data[3]));
        spiral.setPeriod(Double.parseDouble(data[4]));
        spiral.setIncrement(Double.parseDouble(data[5]));
        spiral.setConverge(Boolean.parseBoolean(data[6]));

        spiral.setExpand(Double.parseDouble(data[7])); // matrix generic
        spiral.setTime(Double.parseDouble(data[8]));
        spiral.setMatrix(parseVector(data[9]),parseVector(data[10]),parseVector(data[11]));
        if(!data[12].equals("0")){
            spiral.setTarget(UUID.fromString(data[12]));
        }

        for(int i = 13;i < data.length;i++) { //locations to draw
            spiral.positions.add(parseVector(data[i]));
        }
        return spiral;
    }
}
