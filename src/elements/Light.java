package elements;

import primitives.Point3D;

import java.awt.*;

public abstract class Light {

    private Color _color;

    /************* Constructors ******************/

    // CTOR
    public Light(Color _color) {
        setColor(_color);
    }

    // copy CTOR
    public Light(Light l){
        if(l != null){
            setColor(l.getColor());
        }
    }

    /************* Getters/Setters ******************/

    public Color getColor() {
        return new Color(_color.getRGB());
    }

    public void setColor(Color _color) {
        if(_color != null){
            this._color = new Color(_color.getRGB());
        }
    }

    /************* Operations ****************/

    public abstract Color getIntensity(Point3D p);
}