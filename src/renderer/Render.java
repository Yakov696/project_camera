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
import static primitives.Util.*;

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
            LightSource lightSource = lights.next();

            double soft = occluded_ex(lightSource, point, geometry);
            Vector L = lightSource.getL(point);
            Vector V = point.vector(getScene().getCamera().getP0()).normalize();

            if (L.dotProduct(N) * V.dotProduct(N) > 0) {
                Color lightIntensity = lightSource.getIntensity(point);

                // calculating the diffuse light
                Color diffuseLight = calcDiffusiveComp(geometry.getMaterial().getKd(), N, L, lightIntensity);

                V = V.scale(-1);

                // calculating the specular light
                Color specularLight = calcSpecularComp(geometry.getMaterial().getKs(), V, N, L,
                        geometry.getMaterial().getN(), lightIntensity);

                // l0 += diffuseLight + specularLight
                l0 = addColor(l0, diffuseLight, specularLight);
            }
            l0 = multColor(l0, soft);

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

        Vector N = new Vector(geometry.getNormal(point));
        Vector epsVector = N.scale(N.dotProduct(lightDirection) > 0 ? 1.0 : -1.0);
        epsVector = epsVector.scale(2);
        Point3D geometryPoint = new Point3D(point).add(epsVector);
        Ray lightRay = new Ray(geometryPoint, lightDirection);
        Map<Geometry, List<Point3D>> intersections = getSceneRayIntersections(lightRay);
//        // Flat geometry cannot self intersect
//        if (geometry instanceof FlatGeometry){
//            intersections.remove(geometry);
//        }
        return !intersections.isEmpty();
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
        double KdNL = kd*Math.abs(normal.dotProduct(l));
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

    // construct Reflected Ray
    private Ray constructReflectedRay(Vector N, Point3D p, Ray ray){
        Vector D = ray.getDirection().normalize();
        N = N.scale(-2 * D.dotProduct(N.normalize()));
        D = D.add(N);
        // creating the R vector
        Vector R = new Vector(D).normalize();
        Point3D point3D = p.add(N);
        return new Ray(point3D, R);
    }

    // construct Refracted Ray
    private Ray constructRefractedRay(Point3D p, Ray ray){

        // epsilon
        Point3D rayPoint = new Point3D(p);
        Vector epsVector = ray.getDirection().normalize();
        epsVector = epsVector.scale(2);
        rayPoint = rayPoint.add(epsVector);

        return new Ray(rayPoint, ray.getDirection().normalize());
    }

    private float occluded_ex(LightSource light, Point3D point, Geometry geometry){
        float k = 0;
        Vector lightDirection = light.getL(point).normalize();
        lightDirection = lightDirection.scale(-1);
        Point3D geometryPoint = new Point3D(point);
        Vector epsVector = new Vector(geometry.getNormal(point));
        epsVector = epsVector.scale(2);
        geometryPoint = geometryPoint.add(epsVector);
        Ray[] rays = new Ray[20];
        rays[0] = new Ray(geometryPoint, lightDirection);
        rays[1] = new Ray(geometryPoint, lightDirection.add(new Vector(0,0,0.03)));
        rays[2] = new Ray(geometryPoint, lightDirection.subtract(new Vector(0,0,0.03)));
        rays[3] = new Ray(geometryPoint, lightDirection.add(new Vector(0,0,0.06)));
        rays[4] = new Ray(geometryPoint, lightDirection.subtract(new Vector(0,0,0.06)));
        rays[5] = new Ray(geometryPoint, lightDirection.add(new Vector(0,0,0.09)));
        rays[6] = new Ray(geometryPoint, lightDirection.subtract(new Vector(0,0,0.09)));
        rays[7] = new Ray(geometryPoint, lightDirection.add(new Vector(0,0,0.12)));
        rays[8] = new Ray(geometryPoint, lightDirection.subtract(new Vector(0,0,0.12)));
        rays[9] = new Ray(geometryPoint, lightDirection.add(new Vector(0,0,0.15)));
        rays[10] = new Ray(geometryPoint, lightDirection.subtract(new Vector(0,0,0.15)));
        rays[11] = new Ray(geometryPoint, lightDirection.add(new Vector(0,0,0.18)));
        rays[12] = new Ray(geometryPoint, lightDirection.subtract(new Vector(0,0,0.18)));
        rays[13] = new Ray(geometryPoint, lightDirection.add(new Vector(0,0,0.21)));
        rays[14] = new Ray(geometryPoint, lightDirection.subtract(new Vector(0,0,0.21)));
        rays[15] = new Ray(geometryPoint, lightDirection.add(new Vector(0,0,0.24)));
        rays[16] = new Ray(geometryPoint, lightDirection.subtract(new Vector(0,0,0.24)));
        rays[17] = new Ray(geometryPoint, lightDirection.add(new Vector(0,0,0.25)));
        rays[18] = new Ray(geometryPoint, lightDirection.subtract(new Vector(0,0,0.25)));
        rays[19] = new Ray(geometryPoint, lightDirection.add(new Vector(0,0,0.28)));
        for (int i = 0; i < 20; i++){
            Map<Geometry, List<Point3D>> intersectionPoints = getSceneRayIntersections(rays[i]);
            // Flat geometry cannot self intersect
            if (geometry instanceof FlatGeometry){
                intersectionPoints.remove(geometry);
            }
            if(intersectionPoints.isEmpty()) {
                k += 0.05;
            }
        }

        return k;
    }
}
 


