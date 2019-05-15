package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import java.util.List;

public class Quadrangle extends Geometry {
    Triangle _t1;
    Triangle _t2;

    public Quadrangle (Point3D p1, Point3D p2, Point3D p3, Point3D p4){
        _t1 = new Triangle(p1,p2,p4);
        _t2 = new Triangle(p2,p3,p4);
    }

    public Quadrangle (Quadrangle q){
        _t1 = q._t1;
        _t2 = q._t2;
    }

    @Override
    public List<Point3D> FindIntersections(Ray ray) {
        List<Point3D> myList = _t1.FindIntersections(ray);
        myList.addAll(_t2.FindIntersections(ray));
        return myList;
    }

    @Override
    public Vector getNormal(Point3D point) {
        return _t1.getNormal();
    }
}
