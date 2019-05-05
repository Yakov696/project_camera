package primitives;

import java.math.*;
import java.text.DecimalFormat;
import java.util.Objects;

import java.lang.Math;

public class Vector {

    private Point3D _head;

    //CTOR
    public Vector(Point3D _head) {
        setHead(_head);
    }

    //default CTOR
    public Vector() {
        setHead(new Point3D());
    }

    // copy CTOR
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

    // Get function
    public Point3D getHead() {
        return new Point3D(this._head);
    }

    // set function
    public void setHead(Point3D _head) {
        this._head = new Point3D(_head);
    }

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

    // add function
    public Vector add(Vector v){
        if(v != null){

            Point3D new_head = getHead();
            new_head = new_head.add(v);
            return new Vector(new_head);
        }
        throw new NullPointerException();
    }

    // substract function
    public Vector subtract(Vector v){
        if(v != null){
            Vector tmp = v.scale(-1.0);
            return this.add(tmp);
        }
        throw new NullPointerException();
    }

    // Scalar multiplication function
    public Vector scale(double alpha){
        double x = getHead().getX().getCoordinate();
        x = Util.uscale(x, alpha);

        double y = getHead().getY().getCoordinate();
        y = Util.uscale(y, alpha);

        double z = getHead().getZ().getCoordinate();
        z = Util.uscale(z, alpha);

        return new Vector(new Point3D(x, y, z));
    }

    // Dot product function
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

    // Cross product function
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

    // lenght function
    public double length(){

        double x = getHead().getX().getCoordinate();
        x = Util.uscale(x,x);

        double y = getHead().getY().getCoordinate();
        y = Util.uscale(y,y);

        double z = getHead().getZ().getCoordinate();
        z = Util.uscale(z,z);


        return Math.sqrt(Util.uadd(Util.uadd(x,y),z));
    }

    // normalization function
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

    public int compareTo(Vector vector){
        if(this.equals(vector))
            return 0;
        return 1;
    }
}