package elements;

import primitives.Point3D;
import java.awt.*;
import java.util.Objects;

import static primitives.Util.multColor;

public class AmbientLight extends Light {

    private double _Ka;

    /************* Constructors ******************/

    // CTOR
    public AmbientLight(Color _color, double ka) {
        super(_color);
        setKa(ka);
    }

    // default CTOR
    public AmbientLight() {
        super(Color.black);
        setKa(1);
    }

    // copy CTOR
    public AmbientLight(AmbientLight a)
    {
        super(a);
        if(a != null){
            setKa(a.getKa());
        }
    }

    /************* Getters/Setters ******************/

    public double getKa() { return _Ka; }

    public void setKa(double ka) {
        if(ka <=1 && ka >= 0){
            this._Ka = ka;
        }
        else if(ka > 1){
            this._Ka = 1;
        }
        else if(ka < 0){
            this._Ka = 0;
        }
    }

    /************* Administration  ****************/

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AmbientLight)) return false;
        AmbientLight that = (AmbientLight) obj;
        return Double.compare(that.getKa(), getKa()) == 0 &&
                Objects.equals(getColor(), that.getColor());
    }

    @Override
    public String toString() {
        return "{ Color:( r = " + getColor().getRed() + ", g = " + getColor().getGreen() +
                ", b = " + getColor().getBlue() + " ), Ka = " + getKa() + " }";
    }

    /************* Operations ****************/

    /*************************************************
     * FUNCTION
     * getIntensity
     * PARAMETERS
     * no parameters
     * RETURN VALUE
     * light intensity
     *
     * MEANING
     * return the intensity of the ambient light.
     **************************************************/
    @Override
    public Color getIntensity(Point3D p){
        return multColor(this.getColor(), this._Ka);
    }
}