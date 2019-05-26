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
import static primitives.Util.uscale;

public class Render {

    final int RECURSION_LEVEL = 2;
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
     * getting the closest point to the camera.
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
	   // calculating the color at the current point
    private Color calcColor(Geometry geometry, Point3D point, Ray inRay){
        return calcColor(geometry, point, inRay, 0);
    }

    // calculating the color at the current point - recursive
    private Color calcColor(Geometry geometry, Point3D point, Ray inRay, int level) {

        if (level == RECURSION_LEVEL) {
            return Color.BLACK;
        }

        // getting the normal through the point
        Vector N = geometry.getNormal(point);

        // checking if the point was on the geometry
        if (N == null) {
            return getScene().getBackground();
        }

        // calculating the ambient and the emission light
        Color ambientLight = getScene().getAmbientLight().getIntensity(point);
        Color emissionLight = geometry.getEmmission();

        Color l0 = addColor(ambientLight, emissionLight);

        // running over all the lights
        Iterator<LightSource> lights = _scene.getLightsIterator();
        while (lights.hasNext()) {

            // checking if we have reached the white color
            if (l0.getRGB() == Color.WHITE.getRGB()) {
                return l0;
            }

            // promoting the iterator
            LightSource l = lights.next();

            if (!occluded(l, point, geometry)) {
                Vector L = l.getL(point);
                Vector V = point.vector(getScene().getCamera().getP0()).normalize();

                if (L.dotProduct(N) * V.dotProduct(N) >= 0) {
                    Color lightIntensity = l.getIntensity(point);

                    // calculating the diffuse light
                    Color diffuseLight = calcDiffusiveComp(geometry.getMaterial().getKd(), N, L, lightIntensity);

                    V = V.scale(-1);

                    // calculating the specular light
                    Color specularLight = calcSpecularComp(geometry.getMaterial().getKs(), V, N, L,
                            geometry.getMaterial().getN(), lightIntensity);

                    // l0 += diffuseLight + specularLight
                    l0 = addColor(l0, diffuseLight, specularLight);
                }
            }

        }


        // Recursive call for a reflected ray
        Ray reflectedRay = constructReflectedRay(geometry.getNormal(point), point, inRay);

        // getting the closest intersection point to the reflected ray
        Map.Entry<Geometry,Point3D> reflectedEntry = null;
        Map<Geometry,Point3D> reflected_closestPoint = getClosestPoint(getSceneRayIntersections(reflectedRay),reflectedRay.getPOO());

        Color reflectedColor = Color.BLACK;

        for(Map.Entry<Geometry,Point3D> zug : reflected_closestPoint.entrySet()){
            reflectedEntry = zug;
        }

        if(reflectedEntry != null){
            reflectedColor = calcColor(reflectedEntry.getKey(), reflectedEntry.getValue(), reflectedRay,level+1);
        }

        double kr = geometry.getMaterial().getKr();
        Color reflectedLight = multColor(reflectedColor,kr);

        // Recursive call for a refracted ray
        Ray refractedRay = constructRefractedRay(point, inRay);

        // getting the closest intersection point to the refracted ray
        Map.Entry<Geometry,Point3D> refractedEntry = null;
        Map<Geometry,Point3D> refracted_closestPoint = getClosestPoint(getSceneRayIntersections(refractedRay),refractedRay.getPOO());

        Color refractedColor = Color.black;


        for(Map.Entry<Geometry,Point3D> zug : refracted_closestPoint.entrySet()){
            refractedEntry = zug;
        }
        if(refractedEntry != null) {
            refractedColor = calcColor(refractedEntry.getKey(), refractedEntry.getValue(), refractedRay, level + 1);
        }

        double kt = geometry.getMaterial().getKt();
        Color refractedLight = multColor(refractedColor,kt);

        l0 = addColor(l0, reflectedLight,refractedLight);

        return l0;
    }


    private boolean occluded(LightSource light, Point3D point, Geometry geometry){
        Vector lightDirection = light.getL(point).normalize();
        lightDirection = lightDirection.scale(-1);
        Point3D geometryPoint = new Point3D(point);
        Vector epsVector = new Vector(geometry.getNormal(point));
        epsVector = epsVector.scale(2);
        geometryPoint = geometryPoint.add(epsVector);
        Ray lightRay = new Ray(geometryPoint, lightDirection);
        Map<Geometry, List<Point3D>> intersectionPoints = getSceneRayIntersections(lightRay);
        // Flat geometry cannot self intersect
        if (geometry instanceof FlatGeometry){
            intersectionPoints.remove(geometry);
        }
        return !intersectionPoints.isEmpty();
    }

    private Color calcSpecularComp(double ks, Vector v, Vector normal, Vector d, double shininess, Color lightIntensity){

        double dn = normal.dotProduct(d)*2;
        if(dn < 0){
            normal = normal.scale(-1);
            dn = uscale(dn, -1);
        }
        Vector R = new Vector(d);
        normal = normal.scale(dn);
        R = R.subtract(normal).normalize();
        double res = Math.pow(v.dotProduct(R),shininess);
        double mekadem = uscale(ks,res);

        int r = Integer.min((int)(mekadem*lightIntensity.getRed()),255);
        int g = Integer.min((int)(mekadem*lightIntensity.getGreen()),255);
        int b = Integer.min((int)(mekadem*lightIntensity.getBlue()),255);

        return new Color(Integer.max(r,0),Integer.max(g,0),Integer.max(b,0));

    }

    private Color calcDiffusiveComp(double kd, Vector normal, Vector l, Color lightIntensity){
        double KdNL = kd*(normal.dotProduct(l));
        int r = Integer.min((int)(KdNL*lightIntensity.getRed()), 255);
        int g = Integer.min((int)(KdNL*lightIntensity.getGreen()), 255);
        int b = Integer.min((int)(KdNL*lightIntensity.getBlue()), 255);
        return new Color(Integer.max(r,0),Integer.max(g,0),Integer.max(b,0));
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

    // mult  color
    private Color multColor(Color c, double mekadem){

        int r = (int)(mekadem*c.getRed());
        int g = (int)(mekadem*c.getGreen());
        int b = (int)(mekadem*c.getBlue());

        r = (r > 0) ? r : 0;
        g = (g > 0) ? g : 0;
        b = (b > 0) ? b : 0;

        return new Color(r <= 255 ? r : 255, g <= 255 ? g : 255, b <= 255 ? b : 255);
    }

    // construct Reflected Ray
    private Ray constructReflectedRay(Vector N, Point3D p, Ray ray){
        N = N.normalize();
        Vector D = ray.getDirection().normalize();

        double D_dot_N = D.dotProduct(N);

        if(D_dot_N < 0){
            N = N.scale(-1);
            D_dot_N = uscale(-1,D_dot_N);
        }

        // creating thr R vector
        Vector R = new Vector(D);
        N = N.scale(uscale(2,D_dot_N));
        R = R.subtract(N);
        return new Ray(p, R.normalize());
    }

    // construct Refracted Ray
    private Ray constructRefractedRay(Point3D p, Ray ray){

        // epsilon
        Point3D rayPoint = new Point3D(p);
        Vector epsVector = ray.getDirection().normalize();
        epsVector.scale(2);
        rayPoint = rayPoint.add(epsVector);

        return new Ray(rayPoint, ray.getDirection().normalize());
    }
}
 


