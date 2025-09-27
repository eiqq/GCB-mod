package com.eiqui.gcbmod.particle.shape;

import com.eiqui.gcbmod.particle.Partic;
import com.eiqui.gcbmod.utils.Vector;

import java.util.UUID;

import static com.eiqui.gcbmod.particle.Parser.parseVector;
import static com.eiqui.gcbmod.utils.NumberConversions.floor;

public class SpiralLine extends Line implements Circular{
    protected int points = 360;
    protected double startDegree = 0d;
    protected double radius = 1d;
    protected boolean inverse = false;

    public SpiralLine(Partic particle) {
        super(particle);
    }

    @Override
    public void setRadius(double radius) {
        this.radius = radius;
    }
    @Override
    public void setAxis(Vector axis) {}

    @Override
    public void setRotate(Vector rotate) {}

    @Override
    public void setPoints(int points) {
        this.points = points;
    }
    public void setStartDegree(double startDegree) {
        this.startDegree = startDegree;
    }
    public void setInverse(boolean inverse) {
        this.inverse = inverse;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public Vector getAxis() {
        return null;
    }
    @Override
    public Vector getRotate() {
        return null;
    }
    @Override
    public int getPoints() {
        return points;
    }
    public double getStartDegree() {
        return startDegree;
    }
    public boolean isInverse() {
        return inverse;
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
        if(distance < Vector.getEpsilon()){
            return;
        }
        if (points <= 0) points = 1;
        if (period <= Vector.getEpsilon()) period = Vector.getEpsilon();

        Vector lineVector = v.clone();
        double density = (period/((double) points));
        int loop = floor(distance/density);
        if(loop <= 0){
            return;
        }
        if(!lineVector.isZero()){
            lineVector.normalize().multiply(density);
        }

        Vector tempAxis = v.clone().setY(0).rotateAroundY(Math.toRadians(90));
        if(tempAxis.isZero()){
            tempAxis = new Vector(1, 0, 0);
        }
        Vector rotator = v.clone().rotateAroundAxis(tempAxis,Math.toRadians(-90));
        if(!rotator.isZero()){
            rotator.normalize().multiply(radius);
            rotator.rotateAroundAxis(lineVector,Math.toRadians(startDegree));
        }

        double rotateRadian = Math.toRadians(360d/points);
        if(inverse){
            rotateRadian-=1d;
        }
        Vector[] result = new Vector[loop];

        for(int i = 0;i < loop;i++) {
            result[i] = start.clone().add(transform(rotator));
            rotator.rotateAroundAxis(lineVector, rotateRadian);
            start.add(lineVector);
        }

        print(result,positions[0]);
    }


    public static SpiralLine deserialize(String[] data,Partic particle) {
        //index 0 is header
        SpiralLine spiral = new SpiralLine(particle);
        spiral.setPeriod(Double.parseDouble(data[1]));
        spiral.setRadius(Double.parseDouble(data[2]));
        spiral.setPoints(Integer.parseInt(data[3]));
        spiral.setStartDegree(Double.parseDouble(data[4]));
        spiral.setInverse(Boolean.parseBoolean(data[5]));

        spiral.setExpand(Double.parseDouble(data[6])); // matrix generic
        spiral.setTime(Double.parseDouble(data[7]));
        spiral.setMatrix(parseVector(data[8]),parseVector(data[9]),parseVector(data[10]));
        if(!data[11].equals("0")){
            spiral.setTarget(UUID.fromString(data[11]));
        }

        for(int i = 12;i < data.length;i++) { //locations to draw
            spiral.positions.add(parseVector(data[i]));
        }
        return spiral;
    }
}
