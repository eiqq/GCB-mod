package com.eiqui.gcbmod.particle.shape;

import com.eiqui.gcbmod.particle.Partic;
import com.eiqui.gcbmod.utils.Vector;

import java.util.UUID;

import static com.eiqui.gcbmod.particle.Parser.parseVector;


public class Circle extends ShapeImpl implements Circular{
    protected Vector axis = new Vector(0,1,0);
    protected Vector rotate = new Vector(1,0,0);
    protected int points = 360;
    private double angle = 360.0d;

    public Circle(Partic particle) {
        super(particle);
    }

    public void setRadius(double radius) {
        if(this.rotate.isZero()){
            this.rotate = new Vector(radius,0,0);
        }else{
            this.rotate.normalize().multiply(radius);
        }
    }
    public void setAxis(Vector axis) {
        this.axis = axis.clone();
        if(!axis.isZero()){
            axis.normalize();
        }
    }
    public void setRotate(Vector rotate) {
        if(rotate.isZero()){
            this.rotate = new Vector(Vector.getEpsilon(),0,0);
        }else{
            this.rotate = rotate.clone();
        }
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getRadius() {
        return this.rotate.length();
    }
    public Vector getAxis() {
        return axis.clone();
    }
    public Vector getRotate() {
        return rotate.clone();
    }
    public int getPoints() {
        return points;
    }
    public double getAngle() {
        return angle;
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

        double rotateRadian = Math.toRadians(this.angle/(double)points);
        Vector startVector = rotate.clone();
        Vector[] result = new Vector[points];

        for(int i = 0;i < points;i++) {
            result[i] = transform(startVector).add(center);
            startVector.rotateAroundAxis(axis, rotateRadian);
        }

        print(result,center);
    }


    public static Circle deserialize(String[] data,Partic particle) {
        //index 0 is header
        Circle circle = new Circle(particle);
        circle.setAxis(parseVector(data[1]));
        circle.setRotate(parseVector(data[2]));
        circle.setPoints(Integer.parseInt(data[3]));
        circle.setAngle(Double.parseDouble(data[4]));

        circle.setExpand(Double.parseDouble(data[5])); // matrix generic
        circle.setTime(Double.parseDouble(data[6]));
        circle.setMatrix(parseVector(data[7]),parseVector(data[8]),parseVector(data[9]));
        if(!data[10].equals("0")){
            circle.setTarget(UUID.fromString(data[10]));
        }

        for(int i = 11;i < data.length;i++) { //locations to draw
            circle.positions.add(parseVector(data[i]));
        }
        return circle;
    }

}
