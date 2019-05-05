package geometries;

public abstract class RadialGeometry extends Geometry {

    //properties
    protected double _radius;


    // ************* Constructors ****************** //
    public RadialGeometry(){ _radius = 1; }
    public RadialGeometry(double r){ _radius = r; }
    public RadialGeometry(RadialGeometry r){ this._radius = r._radius; }

    // ************* Getters/Setters ****************** //
    public double getRadius() { return _radius; }
    public void setRadius(double _radius) { this._radius = _radius; }
}