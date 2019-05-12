package geometries;

import primitives.*;
import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Sphere extends RadialGeometry {

    private Point3D _center; // The center of the sphere

    /********** Constructors ***********/
    public Sphere(double _radius, Point3D _center) {
        super(_radius);
        this._center = new Point3D(_center);
    }

    // Copy constructor
    public Sphere(Sphere s){
        super(s);
        if(s != null){
            this._center = s.getCenter();
        }
    }

    /************** Getters/Setters *******/
    public Point3D getCenter() { return new Point3D(_center); }

    /*************** Admin *****************/
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Sphere)) return false;
        Sphere sphere = (Sphere) obj;
        return Objects.equals(getCenter(), sphere.getCenter()) &&
                getRadius() == sphere.getRadius();
    }

    @Override
    public String toString() {
        return "{ M:" + getCenter() + ", R:" + getRadius() + " }";
    }

    /************** Operations ***************/
    // checking if a point is on the sphere
    private boolean is_on_the_sphere(Point3D p){
        return Util.usubtract(getCenter().distance(p), getRadius()) == 0;
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
     * return a vector that is the normal of the sphere.
     **************************************************/
    @Override
    public Vector getNormal(Point3D p) {
        if(p == null) return null;
        return p.vector(getCenter()).normalize();
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

        List<Point3D> arr = new ArrayList<Point3D>();

        // u
        Vector _u = getCenter().vector(r.getPOO());

        // Tm
        double _Tm = r.getDirection().dotProduct(_u) ;

        //d

        double _d = Math.sqrt(Math.pow(_u.length(),2) - Math.pow(_Tm,2));

        if(_d > getRadius()){
            return arr;
        }

        // Th
        double _Th = Math.sqrt(Math.pow(getRadius(),2) - Math.pow(_d,2));

        // t1, t2
        double _t1 = _Tm - _Th;
        double _t2 = _Tm + _Th;

        // p1, p2

        Point3D _p1 = r.getPOO();
        Vector v1 = r.getDirection();
        v1 = v1.scale(_t1);
        _p1 = _p1.add(v1);

        Point3D _p2 = r.getPOO();
        Vector v2 = r.getDirection();
        v2 = v2.scale(_t2);
        _p2 = _p2.add(v2);

        // inserting the points to the arrList

        //if there is only one intersection point
        if(_t1 == _t2 && _t1 >= 0){
            arr.add(_p1);
            return arr;
        }

        if(_t1 >= 0){
            arr.add(_p1);
        }

        if(_t2 >= 0){
            arr.add(_p2);
        }

        return arr;
    }

}