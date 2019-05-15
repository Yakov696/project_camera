package scene;
import elements.AmbientLight;
import elements.Camera;
import elements.LightSource;
import geometries.Geometry;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import elements.LightSource;             part 3

public class Scene {
    private Color _background; // the background color
    private AmbientLight _ambientLight; // the ambient light
    private List<Geometry> _geometries = new ArrayList<Geometry>(); // the geometries in the scene
    private Camera _camera; //the camera that take the pic
    private double _screenDistance; // the distance of the screen from the camera
    private List<LightSource> _lights = new ArrayList<LightSource>();
    private String _sceneName = "scene"; // scene name

    /************* Constructors ******************/
    //default CTOR
    public Scene(){
        _background = new Color(0,0,0);
        _ambientLight = new AmbientLight();
        this.setCamera(new Camera());
        _screenDistance = 100;
    }
    // copy CTOR
    public Scene (Scene scene){
        _background = scene._background;
        _ambientLight = scene._ambientLight;
        _geometries = scene._geometries;
        //_lights = scene._lights;  part 3
        _camera = scene._camera;
        _screenDistance = scene._screenDistance;
    }
    //CTOR
    public Scene(AmbientLight aLight, Color background, Camera camera, double screenDistance){
        _background = background;
        _ambientLight = new AmbientLight(aLight);
        _camera = new Camera(camera);
        _screenDistance = screenDistance;
    }

    /************* Getters/Setters ******************/
    public Color getBackground(){ return _background; }
    public AmbientLight getAmbientLight() { return _ambientLight; }
    public Camera getCamera() { return _camera; }
    public List<Geometry> get_geometries() { return _geometries; }
    public String getSceneName() { return _sceneName; }
    public double getScreenDistance() { return _screenDistance; }


    public void setBackground(Color background) { this._background = background; }
    public void setAmbientLight(AmbientLight ambientLight) { this._ambientLight = new AmbientLight(ambientLight); }
    public void setCamera(Camera camera) { this._camera = new Camera(camera); }
    public void set_geometries (List<Geometry> geometries) { this._geometries = geometries; }
    public void setSceneName(String sceneNAme) { this._sceneName = sceneNAme; }
    public void setScreenDistance(double screenDistance) { this._screenDistance = screenDistance; }


    /************* Operations ****************/

    /*************************************************
     * FUNCTION
     * addGeometry
     * PARAMETERS
     * Geometry
     * RETURN VALUE
     * no return value
     *
     * MEANING
     * adding geometry to the scene.
     **************************************************/
    public void addGeometry(Geometry geometry) { _geometries.add(geometry); }

    /*************************************************
     * FUNCTION
     * getGeometriesIterator
     * PARAMETERS
     * no parameters
     * RETURN VALUE
     * geometries iterator
     *
     * MEANING
     * return iterator that help as to go over the geometries.
     **************************************************/
    public Iterator<Geometry> getGeometriesIterator() { return _geometries.iterator(); }
    public void addLight(LightSource light){
        _lights.add(light);
    }
    public Iterator<LightSource> getLightsIterator(){
        return _lights.iterator();
    }
}









/*package scene;

import elements.*;
import geometries.Geometry;
import geometries.Plane;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Scene {

    private String  _sceneName;
    private Color _background;
    private AmbientLight _ambientLight;
    private ArrayList<Geometry> _geometries;
    private Camera _camera;
    private double _screenDistance;

    //private ArrayList<LightSource> _lights;

    final double DEFAULT_DISTANCE = 50;
    // ************* Constructors ****************** //

    //CTOR
    public Scene(String _sceneName, Color _background, AmbientLight _ambientLight,
                 ArrayList<Geometry> _geometries, Camera _camera, double _screenDistance, ArrayList<LightSource> _lights) {
        set_sceneName(_sceneName != null ? _sceneName : "main scene");
        set_background(_background != null ? _background : Color.white);
        set_ambientLight(_ambientLight != null ? _ambientLight : new AmbientLight());
        set_geometries(_geometries != null ? _geometries : new ArrayList<Geometry>());
        set_camera(_camera != null ? _camera : new Camera());
        set_screenDistance(_screenDistance);
        set_lights(_lights != null ? _lights : new ArrayList<LightSource>());
    }


    // dafault CTOR
    public Scene() {
        set_sceneName("main scene");
        set_background(Color.black);
        set_ambientLight(new AmbientLight());
        set_geometries(new ArrayList<Geometry>());
        set_camera(new Camera());
        set_screenDistance(DEFAULT_DISTANCE);
        //set_lights(new ArrayList<LightSource>());
    }

    // copy CTOR
    public Scene(Scene s){
        if(s != null){
            set_sceneName(s.get_sceneName());
            set_background(s.get_background());
            set_ambientLight(s.get_ambientLight());
            set_geometries(s.get_geometries());
            set_camera(s.get_camera());
            set_screenDistance(s.get_screenDistance());
            set_lights(s.get_lights());
        }
    }

    // ************* Getters/Setters ****************** //

    public String get_sceneName() {
        return new String(_sceneName);
    }

    public void set_sceneName(String _sceneName) {
        if(_sceneName != null){
            this._sceneName = new String(_sceneName);
        }
    }

    public Color get_background() {
        return new Color(_background.getRGB());
    }

    public void set_background(Color _background) {
        if(_background != null){
            this._background = new Color(_background.getRGB());
        }
    }

    public AmbientLight get_ambientLight() {
        return new AmbientLight(_ambientLight);
    }

    public void set_ambientLight(AmbientLight _ambientLight) {
        if(_ambientLight != null) {
            this._ambientLight = new AmbientLight(_ambientLight);
        }
    }

    public ArrayList<Geometry> get_geometries() {
        ArrayList<Geometry> arr = new ArrayList<Geometry>();
        for(Geometry g: _geometries){
            arr.add(g.getClone());
        }
        return arr;
    }

    public void set_geometries(ArrayList<Geometry> _geometries) {
        if(_geometries != null){
            this._geometries = new ArrayList<Geometry>();
            for(Geometry g: _geometries){
                if(g != null){
                    this._geometries.add(g.getClone());
                }
            }
        }
    }

    public Camera get_camera() {
        return new Camera(_camera);
    }

    public void set_camera(Camera _camera) {
        if(_camera != null){
            this._camera = new Camera(_camera);
        }
    }

    public double get_screenDistance() {
        return _screenDistance;
    }

    public void set_screenDistance(double _screenDistance) {
        this._screenDistance = _screenDistance;
    }

    public ArrayList<LightSource> get_lights() {
        ArrayList<LightSource> arr = new ArrayList<LightSource>();
        for(LightSource l: _lights){
            arr.add(l.getClone());
        }
        return arr;
    }

    public void set_lights(ArrayList<LightSource> _lights) {
        if(_lights != null){
            this._lights = new ArrayList<LightSource>();
            for(LightSource l: _lights){
                if(l != null){
                    this._lights.add(l.getClone());
                }
            }
        }
    }

    // ************* Administration  **************** //


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Scene)) return false;
        Scene scene = (Scene) obj;
        return Double.compare(scene.get_screenDistance(), get_screenDistance()) == 0 &&
                Objects.equals(get_sceneName(), scene.get_sceneName()) &&
                Objects.equals(get_background(), scene.get_background()) &&
                Objects.equals(get_ambientLight(), scene.get_ambientLight()) &&
                Objects.equals(get_geometries(), scene.get_geometries()) &&
                Objects.equals(get_camera(), scene.get_camera()) &&
                Objects.equals(get_lights(),scene.get_lights());
    }

    @Override
    public String toString() {
        String s = "SceneName: " + get_sceneName() + '\n' +
                "Background: ( r = " + get_background().getRed() + ", g = " + get_background().getGreen() +
                ", b = " + get_background().getBlue() + " )\n" +
                "AmbientLight: " + get_ambientLight() + '\n' +
                "Camera: " + get_camera() + '\n' +
                "ScreenDistance: " + get_screenDistance() +
                "Geometries:\n";

        if(get_geometries().isEmpty()){
            s += "  empty";
        }
        else{
            for(Geometry g: get_geometries()){
                s += "  " + g + '\n';
            }
        }

        return s;
    }

    // ************* Operations **************** //

    //adding geometries to the scene
    public void addGeometry(Geometry g){
        if(g != null){
            ArrayList<Geometry> arr = get_geometries();
            if(arr == null){
                arr = new ArrayList<Geometry>();
            }
            arr.add(g.getClone());
            set_geometries(arr);
        }
    }

    //adding lights to the scene
    public void addLight(LightSource l){
        if(l != null){
            ArrayList<LightSource> arr = get_lights();
            if(arr == null){
                arr = new ArrayList<LightSource>();
            }
            arr.add(l.getClone());
            set_lights(arr);
        }
    }

    public Iterator<Geometry> getGeometriesIterator(){
        return _geometries.iterator();
    }

    public Iterator<LightSource> getLightsIterator(){
        return _lights.iterator();
    }
}*/