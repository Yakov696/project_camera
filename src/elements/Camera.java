package elements;

import com.sun.org.apache.xpath.internal.operations.Equals;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.Objects;

public class Camera{

    private Point3D _p0;
    private Vector _vUp;
    private Vector _vTo;
    private Vector _vRight;

    // ************* Constructors ****************** //

    //CTOR
    public Camera(Point3D _p0, Vector _vUp, Vector _vTo, Vector _vRight) {
        setP0(_p0);
        set_vUp(_vUp);
        set_vTo(_vTo);
        set_vRight(_vRight);
    }

    public Camera(Point3D _p0, Vector _vUp, Vector _vTo) {
        setP0(_p0);
        set_vUp(_vUp);
        set_vTo(_vTo);
        set_vRight(_vUp.crossProduct(_vTo));
    }

    // default CTOR
    public Camera() {
        setP0(new Point3D(0,0,10));
        set_vUp(new Vector(new Point3D(1, 0, 0)));
        set_vTo(new Vector(new Point3D(0, 0, -1)));
        set_vRight(_vUp.crossProduct(_vTo));
    }

    // copy CTOR
    public Camera(Camera c){
        if(c != null){
            setP0(c.getP0());
            set_vUp(c.get_vUp());
            set_vTo(c.get_vTo());
            set_vRight(c.get_vRight());
        }
    }


    // ************* Getters/Setters ****************** //

    // get functions
    public Point3D getP0() {
        return new Point3D(_p0);
    }

    public Vector get_vUp() {
        return new Vector(_vUp);
    }

    public Vector get_vTo() {
        return new Vector(_vTo);
    }

    public Vector get_vRight() {
        return new Vector(_vRight);
    }

    // set functions
    public void setP0(Point3D _p0) {
        this._p0 = new Point3D(_p0);
    }

    public void set_vUp(Vector _vUp) {
        this._vUp = new Vector(_vUp);
    }

    public void set_vTo(Vector _vTo) {
        this._vTo = new Vector(_vTo);
    }

    public void set_vRight(Vector _vRight) {
        this._vRight = new Vector(_vRight);
    }

    // ************* Administration  **************** //

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Camera)) return false;
        Camera camera = (Camera) obj;
        return Objects.equals(getP0(), camera.getP0()) &&
                Objects.equals(get_vUp(), camera.get_vUp()) &&
                Objects.equals(get_vTo(), camera.get_vTo()) &&
                Objects.equals(get_vRight(), camera.get_vRight());
    }

    @Override
    public String toString()
    {
        return "Vto: " + _vTo + "\n" + "Vup: " + _vUp + "\n" + "Vright:" + _vRight + ".";
    }

    // ************* Operations **************** //

    public Ray constructRayThroughPixel (int Nx, int Ny, double x, double y,
                                         double screenDist, double screenWidth,
                                         double screenHeight){
        // Calculating the image center
        Vector vToward = new Vector(_vTo);
        Vector vRight = new Vector(_vRight);
        Vector vUP = new Vector(_vUp);

        vToward = vToward.normalize();
        vRight = vRight.normalize();
        vUP = vUP.normalize();

        vToward = vToward.scale(screenDist);

        Point3D Pc = getP0().add(vToward);

        // Calculating x-y ratios
        double Rx = screenWidth  / Nx;
        double Ry = screenHeight / Ny;

        // Calculating P - the intersection point
        vRight = vRight.scale(((x - (Nx / 2.0)) * Rx + (Rx / 2.0)));
        vUP = vUP.scale(-((y - (Ny / 2.0)) * Ry + (Ry / 2.0)));

        Point3D P = (Pc.add(vRight)).add(vUP);

        // returning the constructed ray between P0 and the intersection point
        return new Ray(P, new Vector(P.vector(getP0())));



        /*
        // image center
        Vector _dV = get_vTo().scale(screenDist);
        Point3D _Pc = getP0().add(_dV);

        // width & height of a pixel
        double _Rx = screenWidth/Nx;
        double _Ry = screenHeight/Ny;

        // direction of the vectors
        //_Vr.scale((x - (Nx + 1)/2.0)*_Rx);
        Vector _Vr = get_vRight().scale((y - (Ny + 1)/2.0)*_Ry);

        //_Vu.scale((y - (Ny + 1)/2.0)*_Ry);
        Vector _Vu = get_vUp().scale((x - (Nx + 1)/2.0)*_Rx);

        // creating the point P
        _Vr = _Vr.subtract(_Vu);
        _Pc = _Pc.add(_Vr);

        // diraction
        Vector direction = _Pc.vector(getP0()).normalize();
        return new Ray(_Pc, direction);*/
    }

}