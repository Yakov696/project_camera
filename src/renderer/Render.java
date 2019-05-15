package renderer;

import elements.*;
import primitives.*;
import primitives.Vector;
import scene.Scene;
import geometries.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Math.pow;

public class Render {

    private Scene _scene; // the scene that we wont to render
    private ImageWriter _imageWriter; // make the scene to image

    /************* Constructors ******************/

    //CTOR
    public Render(ImageWriter _imageWriter, Scene _scene) {
        // checking if the given parameters are not null
        setScene(_scene != null ? _scene : new Scene());
        setImageWriter(_imageWriter != null ? _imageWriter : new ImageWriter("Default Image", 500, 500, 500, 500));
    }

    //default CTOR
    public Render() {
        setScene(new Scene());
        setImageWriter(new ImageWriter("Default Image", 500, 500, 500, 500));
    }

    // copy CTOR
    public Render(Render r) {
        if (r != null) {
            setScene(r.getScene());
            setImageWriter(r.getImageWriter());
        }
    }

    /************* Getters/Setters ******************/

    public Scene getScene() { return new Scene(_scene); }

    public void setScene(Scene _scene) {
        if (_scene != null) {
            this._scene = new Scene(_scene);
        }
    }

    public ImageWriter getImageWriter() { return _imageWriter; }

    public void setImageWriter(ImageWriter _imageWriter) {
        if (_imageWriter != null) {
            this._imageWriter = _imageWriter;
        }
    }

    /************* Operations ****************/

    /*************************************************
     * FUNCTION
     * renderImage
     * PARAMETERS
     * no parameters
     * RETURN VALUE
     * no return value
     *
     * MEANING
     * make a image from all rey intersections that we find and
     * calculate the closest ona to calc the color according this element.
     **************************************************/
    public void renderImage() {

        // running on all the pixels on the view plane
        for (int i = 0; i < getImageWriter().getWidth(); i++) {

            for (int j = 0; j < getImageWriter().getHeight(); j++) {

                // creating a ray to the current pixel
                Ray r = getScene().getCamera().constructRayThroughPixel
                        (getImageWriter().getWidth(), getImageWriter().getHeight(), i, j,
                                getScene().getScreenDistance(), getImageWriter().getNx(),
                                getImageWriter().getNy());

                // getting all the intersection points through the current pixel
                Map<Geometry, List<Point3D>> intersectionPoints = getSceneRayIntersections(r);

                // if there is not an intersection point through the current pixel
                if (intersectionPoints.isEmpty()) {
                    _imageWriter.writePixel(j, i, getScene().getBackground());
                }
                // otherwise
                else {
                    // getting the closest point which the ray interacts
                    Map<Geometry, Point3D> closestPoint = getClosestPoint(intersectionPoints, getScene().getCamera().getP0());
                    Map.Entry<Geometry, Point3D> entry = null;
                    for (Map.Entry<Geometry, Point3D> zug : closestPoint.entrySet()) {
                        entry = zug;
                    }

                    _imageWriter.writePixel(j, i, calcColor(entry.getKey(), entry.getValue(), r));
                }


            }
        }

    }

    /*************************************************
     * FUNCTION
     * getSceneRayIntersections
     * PARAMETERS
     * ray
     * RETURN VALUE
     * map with all the intersection points
     *
     * MEANING
     * getting a ray and returning all the intersection points
     * of the ray with all the geometries in the scene.
     **************************************************/
    private Map<Geometry, List<Point3D>> getSceneRayIntersections(Ray ray) {

        Iterator<Geometry> geometries = _scene.getGeometriesIterator();
        Map<Geometry, List<Point3D>> intersectionPoints = new HashMap<Geometry, List<Point3D>>();

        while (geometries.hasNext()) {
            Geometry geometry = geometries.next();
            List<Point3D> geometryIntersectionPoints = geometry.FindIntersections(ray);
            //add geometryIntersectionPoints to intersectionPoints
            if (!geometryIntersectionPoints.isEmpty()) {
                intersectionPoints.put(geometry, geometryIntersectionPoints);
            }
        }
        return intersectionPoints;
    }

    /*************************************************
     * FUNCTION
     * getClosestPoint
     * PARAMETERS
     * Map<Geometry, List<Point3D>>
     * Point3D
     * RETURN VALUE
     * map with the closest intersection point
     *
     * MEANING
     * geting the closest point to the camera.
     **************************************************/
    private Map<Geometry, Point3D> getClosestPoint(Map<Geometry, List<Point3D>> intersectionPoints, Point3D P0) {

        double distance = Double.MAX_VALUE;
        Map<Geometry, Point3D> minDistancePoint = new HashMap<Geometry, Point3D>();

        for (Map.Entry<Geometry, List<Point3D>> entry : intersectionPoints.entrySet()) {
            for (Point3D point : entry.getValue()) {
                if (P0.distance(point) < distance) {
                    minDistancePoint.clear();
                    minDistancePoint.put(entry.getKey(), new Point3D(point));
                    distance = P0.distance(point);
                }
            }
        }
        return minDistancePoint;
    }

    /*************************************************
     * FUNCTION
     * calcColor
     * PARAMETERS
     * geometry
     * Point3D
     * Ray
     * RETURN VALUE
     * the pixel color
     *
     * MEANING
     * calculating the color at the current point.
     **************************************************/
    private Color calcColor(Geometry geometry, Point3D point, Ray inRay) {

        // calculating the ambient and the emission light
        Color KamIam = getScene().getAmbientLight().getIntensity(point);
        Color Ie = geometry.getEmmission();

        Color L0 = addColor(KamIam,Ie);
        Iterator<LightSource> lights = _scene.getLightsIterator();
        while (lights.hasNext()){
            if(L0.getRGB()==Color.WHITE.getRGB())
                return L0;
            LightSource l = lights.next();
            if (!occluded(l,point,geometry)) {
                Color c1 = calcDiffusiveComp(geometry.getMaterial().getKd(),geometry.getNormal(point),l.getL(point),l.getIntensity(point));
                Color c2 = calcSpecularComp(geometry.getMaterial().getKs(),point.vector(_scene.getCamera().getP0()),geometry.getNormal(point),l.getL(point),geometry.getMaterial().getN(),l.getIntensity(point));
                L0 = addColor(L0,c1,c2);
            }
        }

        return L0;
    }

    private boolean occluded(LightSource light, Point3D point, Geometry geometry){
        Vector lightDirection = light.getL(point);
        lightDirection.scale(-1);
        Point3D geometryPoint = new Point3D(point);
        Vector epsVector = new Vector(geometry.getNormal(point));
        epsVector.scale(2);
        geometryPoint.add(epsVector);
        Ray lightRay = new Ray(geometryPoint, lightDirection);
        Map<Geometry, List<Point3D>> intersectionPoints = getSceneRayIntersections(lightRay);
        // Flat geometry cannot self intersect
        if (geometry instanceof FlatGeometry){
            intersectionPoints.remove(geometry);
        }
        return !intersectionPoints.isEmpty();
    }

    private Color calcSpecularComp(double ks, Vector v, Vector normal, Vector d, double shininess, Color lightIntensity){
        Vector R = d.subtract(normal.scale(2*d.dotProduct(normal)));
        double K = ks*pow(v.dotProduct(R),shininess);
        return new Color((int)(K*lightIntensity.getRed()),(int)(K*lightIntensity.getGreen()),(int)(K*lightIntensity.getBlue()));
    }

    private Color calcDiffusiveComp(double kd, Vector normal, Vector l, Color lightIntensity){
        double KdNL = kd*(normal.dotProduct(l));
        return new Color((int)(KdNL*lightIntensity.getRed()),(int)(KdNL*lightIntensity.getGreen()),(int)(KdNL*lightIntensity.getBlue()));
    }

    // printing lines on the grid
    public void printGrid(int interval) {

        if (interval > 0) {
            for (int i = 0; i < _imageWriter.getHeight(); i++) {
                for (int j = 0; j < _imageWriter.getWidth(); j++) {

                    if (i % interval == 0 || j % interval == 0)
                        _imageWriter.writePixel(j, i, 255, 255, 255);

                }
            }
        }
    }
    
    public void writeToImage() {
        _imageWriter.writeToimage();
    }

    /*************************************************
     * FUNCTION
     * addColor
     * PARAMETERS
     * 2 colors
     * RETURN VALUE
     * sum of this tow colors
     *
     * MEANING
     * calculating the color according the colors that the function received.
     **************************************************/
    private Color addColor(Color... a){
        int R = 0, G = 0, B = 0;
        for (Color c: a) {
            R += c.getRed();
            G += c.getGreen();
            B += c.getBlue();
        }
        R = Integer.min(R, 255);
        G = Integer.min(G, 255);
        B = Integer.min(B, 255);
        return new Color(R,G,B);
    }
}