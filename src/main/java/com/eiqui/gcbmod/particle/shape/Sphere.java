package com.eiqui.gcbmod.particle.shape;

import com.eiqui.gcbmod.particle.Partic;
import com.eiqui.gcbmod.utils.Vector;

import java.util.UUID;

import static com.eiqui.gcbmod.particle.Parser.parseVector;


public class Sphere extends ShapeImpl implements Circular{
    private Vector axis = new Vector(0,1,0);
    private int points = 360;

    public Sphere(Partic particle) {
        super(particle);
    }

    public void setRadius(double radius) {
        if(this.axis.isZero()){
            this.axis = new Vector(0,radius,0);
        }else{
            this.axis.normalize().multiply(radius);
        }
    }
    public void setAxis(Vector axis) {
        this.axis = axis.clone();
        if(!axis.isZero()){
            axis.normalize();
        }
    }
    @Override
    public void setRotate(Vector rotate) {
        setAxis(rotate);
    }
    public void setPoints(int points) {
        this.points = points;
    }

    public double getRadius() {
        return this.axis.length();
    }
    public Vector getAxis() {
        return axis.clone();
    }
    @Override
    public Vector getRotate() {
        return getAxis();
    }
    public int getPoints() {
        return points;
    }


    @Override
    public void draw(Vector... positions) {
        if(positions.length < 1){
            return;
        }

        final Vector center = positions[0];
        int points = this.points; // "밀도"로 해석: 총 샘플 개수
        if (points <= 0) return;
           // 중심
        final double r = this.axis.length();        // 반지름: 기존 원 코드와 동일하게 rotate의 길이
        final Vector pole = this.axis.clone().normalize(); // 구의 '북극' 축

        // ---- 구 표면 샘플 생성: 피보나치(골든 앵글) ----
        final Vector[] result = fibonacciSphere(center, r, pole, points);
        print(result,center);
    }


    /** 피보나치 격자로 구 표면을 거의 등면적으로 샘플링. pole을 북극축으로 사용. */
    private Vector[] fibonacciSphere(Vector center, double r, Vector pole, int N) {
        N = Math.max(1, N);
        Vector ey = pole.clone().normalize();                 // 북극축
        // ey와 거의 평행한 임시축을 피해서 안정적 기저 구성
        Vector tmp = (Math.abs(ey.getY()) > 0.9) ? new Vector(1, 0, 0) : new Vector(0, 1, 0);
        Vector ez = ey.clone().crossProduct(tmp).normalize(); // 경도축1
        Vector ex = ez.clone().crossProduct(ey).normalize();  // 경도축2

        double ga = Math.PI * (3 - Math.sqrt(5)); // golden angle ≈ 2.399963229...
        Vector[] out = new Vector[N];

        for (int i = 0; i < N; i++) {
            // 등면적 분포: y = 1 - 2*(i+0.5)/N
            double y = 1.0 - 2.0 * (i + 0.5) / N;     // [-1, 1]
            double radius = Math.sqrt(Math.max(0, 1 - y * y));
            double phi = ga * i;

            double x = Math.cos(phi) * radius;
            double z = Math.sin(phi) * radius;

            // 로컬(x,y,z) -> 월드: x*ex + y*ey + z*ez
            Vector world = ex.clone().multiply(x)
                    .add(ey.clone().multiply(y))
                    .add(ez.clone().multiply(z))
                    .multiply(r);
            out[i] = transform(world).add(center);
        }
        return out;
    }

    public static Sphere deserialize(String[] data,Partic particle) {
        //index 0 is header
        Sphere sphere = new Sphere(particle);
        sphere.setAxis(parseVector(data[1]));
        sphere.setPoints(Integer.parseInt(data[2]));

        sphere.setExpand(Double.parseDouble(data[3])); // matrix generic
        sphere.setTime(Double.parseDouble(data[4]));
        sphere.setMatrix(parseVector(data[5]),parseVector(data[6]),parseVector(data[7]));
        if(!data[8].equals("0")){
            sphere.setTarget(UUID.fromString(data[8]));
        }

        for(int i = 9;i < data.length;i++) { //locations to draw
            sphere.positions.add(parseVector(data[i]));
        }
        return sphere;
    }
}
