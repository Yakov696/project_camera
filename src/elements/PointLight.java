package elements;

import primitives.Point3D;
import primitives.Vector;

import java.awt.*;

import static primitives.Util.multColor;

public class PointLight extends Light implements LightSource {
    Point3D _position;
    double _Kc, _Kl, _Kq;
    /************* Constructors ******************/

    // CTOR
    public PointLight(Color _color, Point3D _position, double _Ko, double _Kl, double _Kq) {
        super(_color);
        set_position(_position);
        set_Kc(_Ko);
        set_Kl(_Kl);
        set_Kq(_Kq);
    }

    // copy CTOR
    public PointLight(PointLight p){
        super(p.getColor());
        if(p != null){
            set_position(p.get_position());
            set_Kc(p.get_Kc());
            set_Kl(p.get_Kl());
            set_Kq(p.get_Kq());
        }
    }

    /************* Getters/Setters ******************/

    public Point3D get_position() {
        return new Point3D(_position);
    }

    public void set_position(Point3D _position) {
        if(_position != null){
            this._position = new Point3D(_position);
        }
    }

    public double get_Kc() {
        return _Kc;
    }

    public void set_Kc(double Kc) {
        if(Kc <=1 && Kc >= 0){
            this._Kc = Kc;
        }
        else if(Kc > 1){
            this._Kc = 1;
        }
        else if(Kc < 0){
            this._Kc = 0;
        }
    }

    public double get_Kl() {
        return _Kl;
    }

    public void set_Kl(double Kl) {
        if(Kl <=1 && Kl >= 0){
            this._Kl = Kl;
        }
        else if(Kl > 1){
            this._Kl = 1;
        }
        else if(Kl < 0){
            this._Kl = 0;
        }
    }

    public double get_Kq() {
        return _Kq;
    }

    public void set_Kq(double Kq) {
        if(Kq <=1 && Kq >= 0){
            this._Kq = Kq;
        }
        else if(Kq > 1){
            this._Kq = 1;
        }
        else if(Kq < 0){
            this._Kq = 0;
        }
    }

    /************* Operations ****************/
    @Override
    public Color getIntensity(Point3D p) {
        double d = get_position().distance(p);

        double mekadem = 1.0/(get_Kc() + get_Kl()*d + get_Kq()*d*d);

        mekadem = mekadem <= 1 ? mekadem : 1;

        return multColor(this.getColor(), mekadem);
    }

    @Override
    public Vector getL(Point3D p) {
        return p.vector(get_position()).normalize();
    }
}
