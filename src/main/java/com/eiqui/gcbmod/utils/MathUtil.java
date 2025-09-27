package com.eiqui.gcbmod.utils;

import java.security.SecureRandom;

public class MathUtil {

    // 라디안을 도로 변환하는 상수
    private static final double TO_DEGREES = 180.0d / Math.PI;
    // 도를 라디안으로 변환하는 상수
    private static final double TO_RADIANS = Math.PI / 180.0d;

    public static Vector getMaxRotateVector(Vector current, Vector target, double maxAngleDegrees) {
        Vector currentDirection = current.clone().normalize();
        Vector targetDirection = target.clone().normalize();
        float angleBetween = currentDirection.angle(targetDirection);
        double maxAngleRadians = maxAngleDegrees * TO_RADIANS;
        if (angleBetween <= maxAngleRadians) {
            return target.clone();
        }
        Vector cross = currentDirection.clone().crossProduct(targetDirection);
        if(cross.dot(new Vector(0,1,0)) > 1){
            currentDirection.rotateAroundAxis(cross,-maxAngleRadians);
        }else{
            currentDirection.rotateAroundAxis(cross,maxAngleRadians);
        }
        return currentDirection.multiply(target.length());
    }

    public static float getYaw(Vector vector) {
        if (((Double) vector.getX()).equals((double) 0) && ((Double) vector.getZ()).equals((double) 0)){
            return 0;
        }
        double y = (Math.atan2(vector.getZ(), vector.getX()) * TO_DEGREES) - 90d;
        if (y < 0d){
            y += 360d;
        }
        else if (y >= 360d){
            y -= 360d;
        }
        return (float)y;
    }

    public static float getPitch(Vector vector) {
        double xy = Math.sqrt(vector.getX() * vector.getX() + vector.getZ() * vector.getZ());
        return (float) -(Math.atan(vector.getY() / xy) * TO_DEGREES);
    }

    public static Vector getVector(double yaw, double pitch) {
        double y = Math.sin(pitch * TO_RADIANS);
        double div = Math.max(Vector.getEpsilon(), Math.cos(pitch * TO_RADIANS));
        double x = Math.cos(yaw * TO_RADIANS);
        double z = Math.sin(yaw * TO_RADIANS);
        x *= div;
        z *= div;
        return new Vector(x,-y,z).rotateAroundY(-Math.PI/2.0);
    }

    public static void NaNToZero(Vector v) {
        Double x = v.getX();
        Double y = v.getY();
        Double z = v.getZ();
        if(x.isNaN()){v.setX(0.0d);}
        if(y.isNaN()){v.setY(0.0d);}
        if(z.isNaN()){v.setZ(0.0d);}
    }

    public static Vector setInfiniteTo(Vector v,double d) {
        Double x = v.getX();
        Double y = v.getY();
        Double z = v.getZ();
        if(x.isInfinite()){v.setX(d);}
        if(y.isInfinite()){v.setY(d);}
        if(z.isInfinite()){v.setZ(d);}
        return v;
    }

    public static String getFormattedNumber(double value) {
        if (value == (long) value) {
            // 값이 정수와 같으면 정수로 출력
            return String.valueOf((long)value);
        } else {
            // 값에 소수 부분이 있으면 소수로 출력
            return String.valueOf(value);
        }
    }

    public static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static int getRandomInteger(int i,int n){
        if(i == n){
            return i;
        }
        if (i > n) {
            int temp = i;
            i = n;
            n = temp;
        }
        return SECURE_RANDOM.nextInt(n - i + 1) + i;
    }

    public static double getRandomNumber(double i,double n){
        if(i == n){
            return i;
        }
        if (i > n) {
            double temp = i;
            i = n;
            n = temp;
        }
        return i + (n - i) * SECURE_RANDOM.nextDouble();
    }
}
