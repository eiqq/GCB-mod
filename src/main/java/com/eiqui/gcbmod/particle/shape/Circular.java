package com.eiqui.gcbmod.particle.shape;


import com.eiqui.gcbmod.utils.Vector;

public interface Circular {

    void setRadius(double radius);
    void setAxis(Vector axis);
    void setRotate(Vector rotate);
    void setPoints(int points);

    double getRadius();
    Vector getAxis();
    Vector getRotate();
    int getPoints();

}
