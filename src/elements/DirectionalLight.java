package elements;

import primitives.Point3D;
import primitives.Vector;

import java.awt.*;

public class DirectionalLight extends Light implements LightSource {
    private Vector _direction;

    /************* Constructors ******************/
    public DirectionalLight(Color color, Vector direction){
        super(color);
        this._direction = direction;
    }


    /************* Getters/Setters ******************/
    public Color getIntensity(Point3D point){
        return this.getColor();
    }
    public Vector getDirection(){ return _direction; }
    public void setDirection(Vector direction){ this._direction = direction; }
    public Vector getL(Point3D point){ return _direction; }
}
