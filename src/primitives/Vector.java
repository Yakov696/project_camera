package primitives;

import java.math.*;
import java.text.DecimalFormat;
import java.util.Objects;

import java.lang.Math;

public class Vector {

    private Point3D _head; //Head of the Vector

    /********** Constructors ***********/
    public Vector(Point3D _head) {
        setHead(_head);
    }

    public Vector() {
        setHead(new Point3D());
    }

    public Vector(Vector v){
        if(v != null){
            setHead(v.getHead());
        }
        else{
            setHead(new Point3D());
        }
    }

    public Vector(double x, double y, double z) {
        _head = new Point3D(x, y, z);
    }

    /************** Getters/Setters *******/
    public Point3D getHead() {
        return new Point3D(this._head);
    }

    public void setHead(Point3D _head) {
        this._head = new Point3D(_head);
    }

    /*************** Admin *****************/
    @Override
    public String toString() {
        return getHead().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector)) return false;
        Vector vector = (Vector) obj;
        return Objects.equals(_head, vector._head);
    }

    public int compareTo(Vector vector){
        if(this.equals(vector))
            return 0;
        return 1;
    }

    /************** Operations ***************/
    /*************************************************
     * FUNCTION
     * add
     * PARAMETERS
     * Vector – add vector per coordinate
     * RETURN VALUE
     * A Vector: new vector after add coordinate.
     *
     * MEANING
     * This functions add a vector to other vector,
     * with adding per coordinate
     **************************************************/
    public Vector add(Vector v){
        if(v != null){

            Point3D new_head = getHead();
            new_head = new_head.add(v);
            return new Vector(new_head);
        }
        throw new NullPointerException();
    }

    /*************************************************
     * FUNCTION
     * Subtract
     * PARAMETERS
     * Vector – subtract vector per coordinate
     * RETURN VALUE
     * A Vector: new vector after subtract coordinate.
     *
     * MEANING
     * This functions add a vector to other vector,
     * with adding per coordinate after mult vector by (-1)
     **************************************************/
    public Vector subtract(Vector v){
        if(v != null){
            Vector tmp = v.scale(-1.0);
            return this.add(tmp);
        }
        throw new NullPointerException();
    }

    /*************************************************
     * FUNCTION
     * scale
     * PARAMETERS
     * double – num to multiple a vector
     * RETURN VALUE
     * A Vector: new vector after multiple coordinate.
     *
     * MEANING
     * This functions multiple a vector by double
     **************************************************/
    public Vector scale(double alpha){
        double x = getHead().getX().getCoordinate();
        x = Util.uscale(x, alpha);

        double y = getHead().getY().getCoordinate();
        y = Util.uscale(y, alpha);

        double z = getHead().getZ().getCoordinate();
        z = Util.uscale(z, alpha);

        return new Vector(new Point3D(x, y, z));
    }

    /*************************************************
     * FUNCTION
     * dotProduct
     * PARAMETERS
     * Vector – multiple by this vector
     * RETURN VALUE
     * A double: skalar product => x1*x2 + y1*y2 + z1*z2
     *
     * MEANING
     * This functions multiple two vector and return a double,
     * if the result is 0 so the vectors are orthogonal to each other
     **************************************************/
    public double dotProduct(Vector v){

        if(v == null){
            throw new IllegalArgumentException("The given vector's value is \"null\"");
        }

        double alpha1 = getHead().getX().getCoordinate();
        alpha1 = Util.uscale(alpha1, v.getHead().getX().getCoordinate());

        double alpha2 = getHead().getY().getCoordinate();
        alpha2 = Util.uscale(alpha2, v.getHead().getY().getCoordinate());

        double alpha3 = getHead().getZ().getCoordinate();
        alpha3 = Util.uscale(alpha3, v.getHead().getZ().getCoordinate());

        double dot_product = Util.uadd(alpha1,alpha2);
        dot_product = Util.uadd(dot_product,alpha3);
        return dot_product;
    }

    /*************************************************
     * FUNCTION
     * crossProduct
     * PARAMETERS
     * Vector – multiple by this vector
     * RETURN VALUE
     * A Vector: cross product =>
     * x = y1*z2 - z1*y2
     * y = z1*x2 - x1*z2
     * z = x1*y2 - y1*x2
     *
     * MEANING
     * This functions multiple two vector and return a vector,
     * this vector called: a normal to those vectors
     **************************************************/
    public Vector crossProduct(Vector v){

        if(v == null){
            throw new IllegalArgumentException("The given vector's value is \"null\"");
        }

        double u1 = getHead().getX().getCoordinate();
        double u2 = getHead().getY().getCoordinate();
        double u3 = getHead().getZ().getCoordinate();

        double v1 = v.getHead().getX().getCoordinate();
        double v2 = v.getHead().getY().getCoordinate();
        double v3 = v.getHead().getZ().getCoordinate();

        double x = Util.usubtract(Util.uscale(u2,v3),Util.uscale(u3,v2));
        double y = Util.usubtract(Util.uscale(u3,v1),Util.uscale(u1,v3));
        double z = Util.usubtract(Util.uscale(u1,v2),Util.uscale(u2,v1));

        return new Vector(new Point3D(x, y, z));
    }

    /*************************************************
     * FUNCTION
     * length
     *
     * RETURN VALUE
     * A double: length of the vector from (0,0,0)
     *
     * MEANING
     * This functions compute a length of vector from (0,0,0)
     **************************************************/
    public double length(){

        double x = getHead().getX().getCoordinate();
        x = Util.uscale(x,x);

        double y = getHead().getY().getCoordinate();
        y = Util.uscale(y,y);

        double z = getHead().getZ().getCoordinate();
        z = Util.uscale(z,z);


        return Math.sqrt(Util.uadd(Util.uadd(x,y),z));
    }

    /*************************************************
     * FUNCTION
     * normalize
     *
     * RETURN VALUE
     * Vector – new vector with length 1
     *
     * MEANING
     * This functions normalize a vector to length 1
     **************************************************/
    public Vector normalize(){
        if(length() == 0){
            throw new ArithmeticException();
        }

        if(length() == 0 || length() == 1){
            return new Vector(this);
        }

        double x = Util.uscale(getHead().getX().getCoordinate(), 1/length());
        double y = Util.uscale(getHead().getY().getCoordinate(), 1/length());
        double z = Util.uscale(getHead().getZ().getCoordinate(), 1/length());

        return new Vector(new Point3D(x, y, z));
    }

}