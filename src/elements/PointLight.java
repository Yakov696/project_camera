package elements;

import primitives.Point3D;
import primitives.Vector;

import java.awt.*;

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

    public void set_Kc(double _Kc) {
        if(_Kc <=1 && _Kc >= 0){
            this._Kc = _Kc;
        }
        else if(_Kc > 1){
            this._Kc = 1;
        }
        else if(_Kc < 0){
            this._Kc = 0;
        }
    }

    public double get_Kl() {
        return _Kl;
    }

    public void set_Kl(double _Kl) {
        if(_Kl <=1 && _Kl >= 0){
            this._Kl = _Kl;
        }
        else if(_Kl > 1){
            this._Kl = 1;
        }
        else if(_Kl < 0){
            this._Kl = 0;
        }
    }

    public double get_Kq() {
        return _Kq;
    }

    public void set_Kq(double _Kq) {
        if(_Kq <=1 && _Kq >= 0){
            this._Kq = _Kq;
        }
        else if(_Kq > 1){
            this._Kq = 1;
        }
        else if(_Kq < 0){
            this._Kq = 0;
        }
    }

    /************* Operations ****************/
    @Override
    public Color getIntensity(Point3D p) {
        double d = get_position().distance(p);

        double mekadem = 1.0/(get_Kc() + get_Kl()*d + get_Kq()*d*d);

        mekadem = mekadem <= 1 ? mekadem : 1;

        return new Color((int)(getColor().getRed()*mekadem), (int)(getColor().getGreen()*mekadem), (int)(getColor().getBlue()*mekadem));
    }

    @Override
    public Vector getL(Point3D p) {
        return p.vector(get_position()).normalize();
    }
}
