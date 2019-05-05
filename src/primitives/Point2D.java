package primitives;

import java.util.Objects;

public class Point2D {

    private Coordinate _x;
    private Coordinate _y;

    //CTOR geting Coordinates
    public Point2D(Coordinate _x, Coordinate _y) {
        setX(_x);
        setY(_y);
    }

    //CTOR geting doubles
    public Point2D(double _x, double _y){
        setX(new Coordinate(_x));
        setY(new Coordinate(_y));
    }

    //default CTOR
    public Point2D() {
        setX(new Coordinate(0));
        setY(new Coordinate(0));
    }

    //copy CTOR
    public Point2D(Point2D p){
        if(p != null){
            setX(p.getX());
            setY(p.getY());
        }
    }

    //get functions
    public Coordinate getX() {
        return new Coordinate(_x);
    }

    public Coordinate getY() {
        return new Coordinate(_y);
    }

    //set functions
    public void setX(Coordinate _x) {
        this._x = new Coordinate(_x);
    }

    public void setY(Coordinate _y) {
        this._y = new Coordinate(_y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point2D)) return false;
        Point2D point2D = (Point2D) o;
        return Objects.equals(_x, point2D._x) &&
                Objects.equals(_y, point2D._y);
    }

    @Override
    public String toString() {
        return "(" + getX() + "," + getY() + ")";
    }
    public int compareTo(Point2D point2D) {
        if (this.equals(point2D))
            return 0;
        return 1;
    }

    public Point2D add(Point2D other) { return new Point2D(_x.add(other._x),_y.add(other._y)); }

}