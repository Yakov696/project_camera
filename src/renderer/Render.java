package renderer;

import elements.*;
import primitives.*;
import primitives.Vector;
import scene.Scene;
import geometries.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Render {

    private Scene _scene;
    private ImageWriter _imageWriter;

    // ************* Constructors ****************** //

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

    // ************* Getters/Setters ****************** //

    public Scene getScene() {
        return new Scene(_scene);
    }

    public void setScene(Scene _scene) {
        if (_scene != null) {
            this._scene = new Scene(_scene);
        }
    }

    public ImageWriter getImageWriter() {
        return _imageWriter;
    }

    public void setImageWriter(ImageWriter _imageWriter) {
        if (_imageWriter != null) {
            this._imageWriter = _imageWriter;
        }
    }

    // ************* Operations **************** //

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

    // getting a ray and returning all the intersection points
    // of the ray with all the geometries in the scene
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

    // geting the closest point to the camera
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
    
    // calculating the color at the current point - recursive
    private Color calcColor(Geometry geometry, Point3D point, Ray inRay) {
        
        // getting the normal through the point
        Vector N = geometry.getNormal(point);

        // checking if the point was on the geometry
        if (N == null) {
            return getScene().getBackground();
        }

        // calculating the ambient and the emission light
        Color ambientLight = getScene().getAmbientLight().getIntensity(point);
        Color emissionLight = geometry.getEmmission();

        return addColor(ambientLight, emissionLight);
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

    private Color addColor(Color a, Color b){
        int R, G, B;
        R = Integer.min(a.getRed() + b.getRed(),255);
        G = Integer.min(a.getGreen() + b.getGreen(),255);
        B = Integer.min(a.getBlue() + b.getBlue(),255);
        return new Color(R,G,B);
    }
}