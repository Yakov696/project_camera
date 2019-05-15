package elements;

import primitives.Point3D;
import primitives.Vector;

import java.awt.*;

public class PointLight extends Light implements LightSource {
    Point3D _position;
    double _Kc, _Kl, _Kq;
    /************* Constructors ******************/

    public PointLight(Color color, Point3D position, double kc, double kl, double kq){
        super(color);
        this._position = position;
        this._Kc = kc;
        this._Kl = kl;
        this._Kq = kq;
    }
    /************ Getters/Setters ******************/

    public Color getIntensity(Point3D point){
        double d = point.distance(_position);
        int r = (int)(this._color.getRed()/(_Kc*_Kl*d*_Kq*d*d));
        int g = (int)(this._color.getGreen()/(_Kc*_Kl*d*_Kq*d*d));
        int b = (int)(this._color.getBlue()/(_Kc*_Kl*d*_Kq*d*d));
        return new Color(r,g,b);
    }
    public Vector getL(Point3D point){
        return new Vector(point.subtract(new Vector(_position)));
    }
}
