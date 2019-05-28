package geometries;

import primitives.*;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Plane extends Geometry  implements FlatGeometry {

    private Point3D _Q; // Point in the plan
    private Vector _normal; // Plane normal

    /********** Constructors ***********/
    public Plane( Vector N, Point3D _Q) {
        setQ(_Q);
        setNormal(N);
    }

    // Copy constructor
    public Plane(Plane p) {
        if (p != null) {
            setQ(p.getQ());
            setNormal(p.getNormal());
        }
    }

    /************** Getters/Setters *******/
    public Point3D getQ() {
        return new Point3D(_Q);
    }

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

    /*************** Admin *****************/
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

    /************** Operations ***************/
    /*************************************************
     * FUNCTION
     * is_on_the_plane
     * PARAMETERS
     * Point3D – point to check
     * RETURN VALUE
     * if the point is on the plane -True
     * else - False
     *
     * MEANING
     * Check if the point is on the plane or not.
     **************************************************/
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

    /*************************************************
     * FUNCTION
     * getNormal
     * PARAMETERS
     * no parameters
     * RETURN VALUE
     * The triangle normal
     *
     * MEANING
     * return a vector that is the normal of the plane.
     **************************************************/
    @Override
    public Vector getNormal(Point3D p) {
        if (p != null) {
            return this._normal;
        }
        return null;
    }

    @Override
    public Plane getClone() {
        return new Plane(this);
    }

    /*************************************************
     * FUNCTION
     * FindIntersections
     * PARAMETERS
     * Ray - to find the intersections
     * RETURN VALUE
     * intersections point list
     *
     * MEANING
     * we find all intersection point between the ray and
     * all the elements in the scene.
     **************************************************/
    @Override
    public List<Point3D> FindIntersections(Ray r) {

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