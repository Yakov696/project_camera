package geometries;

import primitives.Material;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Triangle extends Plane implements FlatGeometry {

    private Point3D _p1; // The 3 triangle points
    private Point3D _p2;
    private Point3D _p3;

    /********** Constructors ***********/
    public Triangle(Point3D _p1, Point3D _p2, Point3D _p3) {
        super( _p1.vector(_p3).crossProduct(_p1.vector(_p2)), _p1);
        this._p1 = new Point3D(_p1);
        this._p2 = new Point3D(_p2);
        this._p3 = new Point3D(_p3);
    }

    // Copy constructor
    public Triangle(Triangle t) {
        super(t);
        if (t != null) {
            this._p1 = new Point3D(t.getP1());
            this._p2 = new Point3D(t.getP2());
            this._p3 = new Point3D(t.getP3());
        }
    }

    /************** Getters/Setters *******/
    public Point3D getP1() {
        return new Point3D(_p1);
    }

    public Point3D getP2() {
        return new Point3D(_p2);
    }

    public Point3D getP3() {
        return new Point3D(_p3);
    }

    public void setShininess(int n) {    }

    public Plane getPlane() {
        Vector u = getP1().vector(getP2());
        Vector v = getP1().vector(getP3());
        return new Plane( u.crossProduct(v), getP1());
    }

    public double getArea() {
        Vector u = getP1().vector(getP2());
        Vector v = getP1().vector(getP3());
        double area = (u.crossProduct(v).length()) / 2;
        return area;
    }

    /*************** Admin *****************/
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Triangle)) return false;
        Triangle triangle = (Triangle) obj;
        return Objects.equals(getP1(), triangle.getP1()) &&
                Objects.equals(getP2(), triangle.getP2()) &&
                Objects.equals(getP3(), triangle.getP3());
    }

    @Override
    public String toString() {
        return "{ P1:" + getP1() + ", P2:" + getP2() + ", P3:" + getP3() + " }";
    }

    /************** Operations ***************/

    /*************************************************
     * FUNCTION
     * is_on_the_triangle
     * PARAMETERS
     * Point3D – point to check
     * RETURN VALUE
     * if the point is on the triangle -True
     * else - False
     *
     * MEANING
     * Check if the point is on the triangle or not.
     **************************************************/
    public boolean is_on_the_triangle(Point3D p) {
        if (!getPlane().is_on_the_plane(p)) return false;

        // creating three new triangles
        //Triangle t1 = new Triangle(p, getP1(), getP2());
        //Triangle t2 = new Triangle(p, getP1(), getP3());
        //Triangle t3 = new Triangle(p, getP2(), getP3());

        //return getArea() == t1.getArea() + t2.getArea() + t3.getArea();

        Point3D p0 = new Point3D(0, 0, 0);

        Vector v1 = getP1().vector(p0);
        Vector v2 = getP2().vector(p0);
        Vector v3 = getP3().vector(p0);

        Vector N1 = v1.crossProduct(v2);
        Vector N2 = v2.crossProduct(v3);
        Vector N3 = v3.crossProduct(v1);

        Vector P_P0 = p.vector(p0);

        boolean allAboveZero = (P_P0.dotProduct(N1) >= 0) &&
                (P_P0.dotProduct(N2) >= 0) &&
                (P_P0.dotProduct(N3) >= 0);

        boolean allBehindZero = (P_P0.dotProduct(N1) <= 0) &&
                (P_P0.dotProduct(N2) <= 0) &&
                (P_P0.dotProduct(N3) <= 0);

        return (allAboveZero || allBehindZero);
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
     * return a vector that is the normal of the triangle.
     **************************************************/
    @Override
    public Vector getNormal(Point3D p) {
        return super.getNormal(p);
//        if (is_on_the_triangle(p)) {
//            return super.getNormal(p);
//        }
//        return null;
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

        // craeting an empty arrayList
        List<Point3D> arr = new ArrayList<Point3D>();

        // getting the intersection Points of the plane of the triangle
        List<Point3D> intersectionPointsPlane = getPlane().FindIntersections(r);

        // checking if the points is on the triangle
        for (Point3D p : intersectionPointsPlane) {
            if (is_on_the_triangle(p)) {
                arr.add(p);
            }
        }

        return arr;
    }

    @Override
    public Triangle getClone() {
        return new Triangle(this);
    }

}