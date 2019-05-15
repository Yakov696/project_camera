package elements;

import primitives.Point3D;
import primitives.Vector;

import java.awt.*;

public class SpotLight extends PointLight {

    private Vector _direction;
    /************* Constructor ******************/

    public SpotLight(Color color, Point3D position, Vector direction, double kc, double kl, double kq){
        super(color, position, kc, kl, kq);
        this._direction = direction;
    }

    /************* Getters/Setters ******************/

    public Color getIntensity(Point3D point){
        double d = point.distance(_position);
        double dl = this.getL(point).dotProduct(_direction);
        int r = (int)(dl * this._color.getRed()/(_Kc*_Kl*d*_Kq*d*d));
        int g = (int)(dl * this._color.getGreen()/(_Kc*_Kl*d*_Kq*d*d));
        int b = (int)(dl * this._color.getBlue()/(_Kc*_Kl*d*_Kq*d*d));
        return new Color(r,g,b);
    }
}
