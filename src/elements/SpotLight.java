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

    public SpotLight(SpotLight s) {
        super(s);
        if (s != null)
            this._direction = s._direction;
    }

    /************* Getters/Setters ******************/


    public Vector get_direction() {
        return new Vector(_direction).normalize();
    }

    public void set_direction(Vector _direction) {
        if(_direction != null){
            this._direction = new Vector(_direction);
        }
    }

    /************* Operations ****************/
    @Override
    public Color getIntensity(Point3D p) {
        double d = get_position().distance(p);
        double mekadem = (get_direction().dotProduct(getL(p)))/(get_Kc() + get_Kl()*d + get_Kq()*d*d);

        mekadem = mekadem <=1 ? mekadem : 1;
        mekadem = mekadem >=0 ? mekadem : 0;

        return new Color((int)(getColor().getRed()*mekadem), (int)(getColor().getGreen()*mekadem), (int)(getColor().getBlue()*mekadem));
    }

    @Override
    public Vector getL(Point3D p) {
        return new Vector(_direction).normalize();
    }
}
