package geometries;

import primitives.*;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Plane extends Geometry  implements FlatGeometry {

    private Point3D _Q;
    private Vector _normal;

    //CTOR
    public Plane( Vector N, Point3D _Q) {
        setQ(_Q);
        setNormal(N);
    }

    // copy CTOR
    public Plane(Plane p) {
        if (p != null) {
            setQ(p.getQ());
            setNormal(p.getNormal());
        }
    }

    // get functions
    public Point3D getQ() {
        return new Point3D(_Q);
    }

    // set functions
    public void setShininess(int n) {}

    public Vector getNormal() {
        return this._normal;
    }

    public void setQ(Point3D _Q) {
        this._Q = new Point3D(_Q);
    }

    public void setNormal(Vector N) {
        this._normal = new Vector(N).normalize();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Plane)) return false;
        Plane plane = (Plane) obj;
        return Objects.equals(getQ(), plane.getQ()) &&
                Objects.equals(getNormal(), plane.getNormal());
    }

    @Override
    public String toString() {
        return "{ Q:" + getQ() + ", N:" + getNormal() + " }";
    }

    // checking if a point is on the plan
    public boolean is_on_the_plane(Point3D p) {

        if (p == null) return false;

        //double a = getNormal().getHead().getX().getCoordinate();
        //double b = getNormal().getHead().getY().getCoordinate();
        //double c = getNormal().getHead().getZ().getCoordinate();

        //double x = p.getX().getCoordinate();
        //double y = p.getY().getCoordinate();
        //double z = p.getZ().getCoordinate();


        //return Tools.round(Tools.round(a*x,6) + Tools.round(b*y,6) + Tools.round(c*z,6) + Tools.round(get_D(),6),6) == 0.0;

        Vector PQ = p.vector(getQ());
        return getNormal().dotProduct(PQ) == 0;
    }

    @Override
    public Vector getNormal(Point3D p) {
        if (is_on_the_plane(p)) {
            return this._normal;
        }
        return null;
    }

    @Override
    public List<Point3D> FindIntersections(Ray r) {

        // creating a empty List
        //List<Point3D> arr = new List<Point3D>();

        // Q0 - P0
        //Vector Q0_P0 = getQ().vector(r.getPOO());

        // N*(Q0 - P0)
        //double mone = getNormal().dotProduct(Q0_P0);

        // N*v
        //double macane = getNormal().dotProduct(r.getDirection());

        // t
        //double t = mone/macane;

        //if(t <= 0){
        //    return arr;
        //}

        // v
        //Vector v = r.getDirection();
        //v.scale(t);

        // p
        //Point3D p = r.getPOO();
        //p.add(v.getHead());

        //arr.add(p);

        //return arr;

        //????????????????????///

        // creating a empty List
        List<Point3D> arr = new ArrayList<Point3D>();

        // P0 - Q0
        Vector P0_Q0 = r.getPOO().vector(getQ());

        // N*(P0 - Q0)
        double mone = getNormal().dotProduct(P0_Q0);

        // N*v
        double macane = getNormal().dotProduct(r.getDirection());

        // t
        double t = -mone / macane;

        if (t <= 0) {
            return arr;
        }

        // v
        Vector v = r.getDirection();
        v = v.scale(t);

        // p
        Point3D p = r.getPOO();
        p = p.add(v);

        arr.add(p);

        return arr;
    }
}