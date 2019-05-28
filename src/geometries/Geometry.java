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
    public void setMaterial (double n, double kt,double kr,double kd,double ks) {
        _material.setN(n);
        _material.setKt(kt);
        _material.setKr(kr);
        _material.setKd(kd);
        _material.setKs(ks);
    }
    public void setEmmission(Color emmission){ this._emmission = emmission; }
    public abstract Geometry getClone ();

}