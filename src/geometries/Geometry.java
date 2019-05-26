package geometries;

import primitives.*;
import java.util.List;
import java.awt.*;

public abstract class Geometry {

    //properties
    private Material _material = new Material();
    private Color _emmission = new Color(0, 0, 0);

    /************** Getters/Setters *******/
    public abstract List<Point3D> FindIntersections (Ray ray);
    public abstract Vector getNormal(Point3D point);
    public Material getMaterial(){ return _material; }
    public Color getEmmission(){ return _emmission; }
    public void setMaterial(Material material){ this._material = new Material(material); }
    public void setMaterial (double a, double b,double c,double d,double e) {
        _material.setN(a);
        _material.setKt(b);
        _material.setKr(c);
        _material.setKd(d);
        _material.setKs(e);
    }
    public void setEmmission(Color emmission){ this._emmission = emmission; }

}