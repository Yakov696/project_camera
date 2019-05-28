package elements;

import primitives.Point3D;
import primitives.Vector;

import java.awt.*;

public class DirectionalLight extends Light implements LightSource {
    private Vector _direction;

    /************* Constructors ******************/
    public DirectionalLight(Color color, Vector direction){
        super(color);
        this._direction = new Vector(direction);
    }


    /************* Getters/Setters ******************/
    public Vector getDirection(){ return new Vector(_direction); }
    public void setDirection(Vector direction){ this._direction = new Vector(direction); }

    /************* Operations ******************/
    @Override
    public Color getIntensity(Point3D point){ return new Color(this.getColor().getRGB()); }
    @Override
    public Vector getL(Point3D point){ return new Vector(_direction.normalize()); }
}
