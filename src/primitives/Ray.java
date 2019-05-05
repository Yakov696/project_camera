package primitives;

import java.util.Objects;

public class Ray {

    private Point3D _POO;
    private Vector _direction;

    // CTOR
    public Ray(Point3D _POO, Vector _direction) {
        setPOO(_POO);
        setDirection(_direction);
    }

    //default CTOR
    public Ray() {
        setPOO(new Point3D());
        setDirection(new Vector());
    }

    // copy CTOR
    public Ray(Ray r){
        if(r != null){
            setPOO(r.getPOO());
            setDirection(r.getDirection());
        }
    }

    // get functions
    public Point3D getPOO() {
        return new Point3D(_POO);
    }

    public Vector getDirection() {
        return new Vector(_direction).normalize();
    }

    // set function
    public void setPOO(Point3D _POO) {
        this._POO = new Point3D(_POO);
    }

    public void setDirection(Vector _direction) {
        this._direction = new Vector(_direction);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Ray)) return false;
        Ray ray = (Ray) obj;
        return Objects.equals(_POO, ray._POO) &&
                Objects.equals(_direction, ray._direction);
    }

    @Override
    public String toString() {
        return "{ P:" + getPOO() + ", V:" + getDirection() + " }";
    }


}