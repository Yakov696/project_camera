package primitives;



import java.util.Objects;

public class Point3D extends Point2D {

    private Coordinate _z;

    /********** Constructors ***********/
    //CTOR geting Coordinates
    public Point3D(Coordinate _x, Coordinate _y, Coordinate _z) {
        super(_x, _y);
        setZ(_z);
    }

    //CTOR geting doubles
    public Point3D(double _x, double _y, double _z) {
        super(_x, _y);
        setZ(new Coordinate(_z));
    }

    // default CTOR
    public Point3D() {
        setZ(new Coordinate(0));
    }

    // copy CTOR
    public Point3D(Point3D p) {
        super(p);
        if (p != null) {
            setZ(p.getZ());
        }
    }

    public Point3D(Point2D p2, Coordinate z){
        super(p2);
        _z = z;
    }

    /************** Getters/Setters *******/
    public Coordinate getZ() {
        return new Coordinate(_z);
    }

    public void setZ(Coordinate _z) {
        this._z = new Coordinate(_z);
    }

    /*************** Admin *****************/
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Point3D)) return false;
        if (!super.equals(obj)) return false;
        Point3D point3D = (Point3D) obj;
        return Objects.equals(_z, point3D._z);
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)",getX().getCoordinate(), getY().getCoordinate(), getZ().getCoordinate());
    }

    // vector function
    public Vector vector(Point3D p) {

        if (p == null) {
            throw new IllegalArgumentException("The given point's value is \"null\"");
        }

        // X
        Coordinate new_x = getX();
        new_x = new_x.subtract(p.getX());

        // Y
        Coordinate new_y = getY();
        new_y = new_y.subtract(p.getY());

        // Z
        Coordinate new_z = getZ();
        new_z = new_z.subtract(p.getZ());

        return new Vector(new Point3D(new_x, new_y, new_z));
    }

    /*************************************************
     * FUNCTION
     * add
     * PARAMETERS
     * Vector – add vector to another
     * RETURN VALUE
     * A Vector: new Vector after add vector to another per coordinates
     *
     * MEANING
     * This function add vector to vector per coordinates.
     **************************************************/

    public Point3D add(Vector vec){
        return new Point3D(super.add((Point2D)vec.getHead()),_z.add(vec.getHead()._z));
    }

    /*************************************************
     * FUNCTION
     * distance
     * PARAMETERS
     * Point3D – calculate the length by this point
     * RETURN VALUE
     * A Double: the distance of point from (0,0,0)
     *
     * MEANING
     * This function calculate the length of point from (0,0,0)
     **************************************************/
    public double distance(Point3D p3) {
        Point3D tmp = this.subtract(new Vector(p3));
        return Math.sqrt(Math.pow(tmp.getX()._coord,2)+Math.pow(tmp.getY()._coord,2)+Math.pow(tmp._z._coord,2));
    }

    /*************************************************
     * FUNCTION
     * subtract
     * PARAMETERS
     * Point3D – multiple by this vector
     * RETURN VALUE
     * A Vector: new Vector after subtract with the point3d
     *
     * MEANING
     * This function subtract point from vector.
     **************************************************/
    public Point3D subtract(Vector other){
        return new Point3D(this.add(new Vector(other.scale(-1))));
    }

    public int compareTo(Point3D point3D) {
        if (this.equals(point3D))
            return 0;
        return 1;
    }
}