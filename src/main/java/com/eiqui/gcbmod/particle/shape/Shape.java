package com.eiqui.gcbmod.particle.shape;

import com.eiqui.gcbmod.utils.Vector;

import java.util.ArrayList;

public interface Shape {
    void setMatrix(Vector x, Vector y, Vector z);
    void setTime(double timeInSeconds);

    Vector[] getMatrix();
    double getTime();
    double getExpand();
    ArrayList<Vector> getPositions();

    void setExpand(double expand);

    void draw(Vector... positions);

}
