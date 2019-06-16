package tests;

import elements.AmbientLight;
import elements.Camera;
import elements.PointLight;
import elements.SpotLight;
import geometries.Plane;
import geometries.Quadrangle;
import geometries.Sphere;
import geometries.Triangle;
import junit.framework.TestCase;
import primitives.Material;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import renderer.ImageWriter;
import renderer.Render;
import scene.Scene;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VectorTest extends TestCase {
    public void test0() {
        System.out.println("Test01: Point3D compareTo");
        Point3D point3D = new Point3D(2.0, -7.5, 9.25);
        Point3D instance = new Point3D(2.0, -7.5, 9.25);
        int expResult = 0;
        int result = instance.compareTo(point3D);
        assertEquals(expResult, result);
    }

    public void test1() {
        System.out.println("Test02: Point3D toString");
        Point3D instance = new Point3D(1.123, 2.569, 3.999);
        String expResult = "(1.12, 2.57, 4.00)";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    public void test2() {
        System.out.println("Test03: Point3D add");
        Vector vector = new Vector(1.25, -2.0, 3.0);
        Point3D instance = new Point3D(4.75, -5.0, 6.0);
        Point3D result = instance.add(vector);
        assertTrue(result.compareTo(new Point3D(6.0, -7.0, 9.0)) == 0);
    }

    public void test04() {
        System.out.println("Test04: Point3D subtract");
        Vector vector = new Vector(1.0, 2.0, 3.0);
        Point3D instance = new Point3D(4.0, 5.0, 6.0);
        Point3D result = instance.subtract(vector);
        assertTrue(result.compareTo(new Point3D(3.0, 3.0, 3.0)) == 0);
    }

    public void test05() {
        System.out.println("Test05: Point3D distance");
        Point3D point = new Point3D(-20.5, 55, 9.25);
        Point3D instance = new Point3D(75, -10, -100);
        double expResult = 159.0;
        double result = instance.distance(point);
        assertEquals("Worng distance", expResult, result, 0.01);
    }

    /************************************** Vector tests *************************************************************/

    public void test06() {
        System.out.println("Test06: Vector Add test");

        Vector v1 = new Vector(1.0, 1.0, 1.0);
        Vector v2 = new Vector(-1.0, -1.0, -1.0);

        Vector v3 = v1.add(v2);
        assertTrue(v3.compareTo(new Vector(0.0, 0.0, 0.0)) == 0);

        v3 = v2.add(v1);
        assertTrue(v3.compareTo(v3) == 0);
    }

    public void test07() {
        System.out.println("Test07: Vector Substruct test");

        Vector v1 = new Vector(1.0, 1.0, 1.0);
        Vector v2 = new Vector(-1.0, -1.0, -1.0);

        Vector v3 = v1.subtract(v2);
        assertTrue(v3.compareTo(new Vector(2.0, 2.0, 2.0)) == 0);

        v3 = v2.subtract(v1);
        assertTrue(v3.compareTo(new Vector(-2.0, -2.0, -2.0)) == 0);
    }

    public void test08() {
        System.out.println("Test08: Vector Scaling test");

        Vector v1 = new Vector(1.0, 1.0, 1.0);

        Vector v2 = v1.scale(1);
        assertTrue(v2.compareTo(v1) == 0);

        v2 = v1.scale(2);
        assertTrue(v2.compareTo(new Vector(2.0, 2.0, 2.0)) == 0);

        v2 = v1.scale(-2);
        assertTrue(v2.compareTo(new Vector(-2.0, -2.0, -2.0)) == 0);
    }

    public void test09() {
        System.out.println("Test09: Vector Dot product test");


        Vector v1 = new Vector(3.5, -5, 10);
        Vector v2 = new Vector(2.5, 7, 0.5);

        assertTrue(Double.compare(v1.dotProduct(v2), (8.75 + -35 + 5)) == 0);

    }

    public void test10() {
        System.out.println("Test10: Vector Length test");

        Vector v = new Vector(3.5, -5, 10);
        assertTrue(v.length() == Math.sqrt(12.25 + 25 + 100));
    }

    public void test11() {
        System.out.printf("Test11: Vector Normalize test -> ");

        Vector v = new Vector(100, -60.781, 0.0001);
        System.out.printf("Length = %f  ", v.length());
        Vector v1 = v.normalize();
        System.out.printf("Length = %f\n", v1.length());

        assertEquals("Incorrect length after normalize! ", 1, v1.length(), 1e-10);

        v = new Vector(0, 0, 0);

        try {
            v1 = v.normalize();
            fail("Didn't throw divide by zero exception!");
        } catch (ArithmeticException e) {
            assertTrue(true);
        }

    }

    public void test12() {
        System.out.println("Test12: Vector Cross product test");

        Vector v1 = new Vector(3.5, -5.0, 10.0);
        Vector v2 = new Vector(2.5, 7, 0.5);
        Vector v3 = v1.crossProduct(v2);

        assertEquals("", 0, v3.dotProduct(v2), 1e-10);
        assertEquals("", 0, v3.dotProduct(v1), 1e-10);

        Vector v4 = v2.crossProduct(v1);
        Vector v5 = v3.add(v4);
        assertEquals("", 0, v5.length(), 1e-10);
    }

    public void test13() {
        final int WIDTH = 3;
        final int HEIGHT = 3;
        Ray[][] rays = new Ray[HEIGHT][WIDTH];
        Camera camera = new Camera(new Point3D(0.0, 0.0, 0.0),
                new Vector(0.0, 1.0, 0.0),
                new Vector(0.0, 0.0, -1.0));
// plane orthogonal to the view plane
        Plane plane = new Plane(new Vector(0.0, 0.0, -1.0), new Point3D(0.0, 0.0, -3.0));
// 45 degrees to the view plane
        Plane plane2 = new Plane(new Vector(0.0, 0.25, -1.0), new Point3D(0.0, 0.0, -3.0));
        List<Point3D> intersectionPointsPlane = new ArrayList<Point3D>();
        List<Point3D> intersectionPointsPlane2 = new ArrayList<Point3D>();
        System.out.println("Camera:\n" + camera);
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                rays[i][j] = camera.constructRayThroughPixel(
                        WIDTH, HEIGHT, j, i, 1, 3 * WIDTH, 3 * HEIGHT);
                List<Point3D> rayIntersectionPoints = plane.FindIntersections(rays[i][j]);
                List<Point3D> rayIntersectionPoints2 = plane2.FindIntersections(rays[i][j]);
                for (Point3D iPoint : rayIntersectionPoints)
                    intersectionPointsPlane.add(iPoint);
                for (Point3D iPoint : rayIntersectionPoints2)
                    intersectionPointsPlane2.add(iPoint);
            }
        }
        assertTrue(intersectionPointsPlane.size() == 9);
        assertTrue(intersectionPointsPlane2.size() == 9);
        for (Point3D iPoint : intersectionPointsPlane)
            System.out.println(iPoint);
        System.out.println("---");
        for (Point3D iPoint : intersectionPointsPlane2)
            System.out.println(iPoint);
    }

    public void test14() {

        ImageWriter imageWriter = new ImageWriter("Image writer test", 500, 500, 1, 1);
        Random rand = new Random();
        for (int i = 0; i < imageWriter.getHeight(); i++) {
            for (int j = 0; j < imageWriter.getWidth(); j++) {

                if (i % 25 == 0 || j % 25 == 0 || i == j || i == 499 || j == 499 || i == 500 - j)
                    imageWriter.writePixel(j, i, 0, 0, 0);  // Black
                else if (i >= 200 && i <= 300 && j >= 200 && j <= 300)
                    imageWriter.writePixel(j, i, 255, 0, 0); // Red
                else if (i >= 150 && i <= 350 && j >= 150 && j <= 350)
                    imageWriter.writePixel(j, i, 0, 255, 0);  // Green
                else if (i >= 100 && i <= 400 && j >= 100 && j <= 400)
                    imageWriter.writePixel(j, i, 0, 0, 255); // Blue
                else if (i >= 50 && i <= 450 && j >= 50 && j <= 450)
                    imageWriter.writePixel(j, i, 255, 255, 0); // Yellow
                else
                    imageWriter.writePixel(j, i, rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)); // Random
            }
        }

        imageWriter.writeToimage();

    }

    public void test15() {
        Scene scene = new Scene();
        scene.setScreenDistance(50);

        Sphere sphere = new Sphere(50, new Point3D(0.0, 0.0, -50));
        Triangle triangle1 = new Triangle(new Point3D(150, 0, -50),
                new Point3D(0, 150, -50),
                new Point3D(150, 150, -50));

        Triangle triangle2 = new Triangle(new Point3D(150, 0, -50),
                new Point3D(0, -150, -50),
                new Point3D(150, -150, -50));

        Triangle triangle3 = new Triangle(new Point3D(-150, 0, -50),
                new Point3D(0, 150, -50),
                new Point3D(-150, 150, -50));

        Triangle triangle4 = new Triangle(new Point3D(-150, 0, -50),
                new Point3D(0, -150, -50),
                new Point3D(-150, -150, -50));

        sphere.setEmmission(new Color(0, 100, 0));
        triangle1.setEmmission(new Color(255, 0, 0));
        triangle2.setEmmission(new Color(0, 255, 0));
        triangle3.setEmmission(new Color(0, 0, 160));
        triangle4.setEmmission(new Color(160, 0, 0));

        scene.addGeometry(sphere);
        scene.addGeometry(triangle1);
        scene.addGeometry(triangle2);
        scene.addGeometry(triangle3);
        scene.addGeometry(triangle4);


        scene.addLight(new PointLight(new Color(100, 100, 100), new Point3D(0, 0, 0), 0.00001, 0.001, 0.0005));

        ImageWriter imageWriter = new ImageWriter("Emmission test", 500, 500, 500, 500);

        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.printGrid(50);
        render.writeToImage();
    }

    public void test16() {

        Scene scene = new Scene();
        Sphere sphere = new Sphere(500, new Point3D(0.0, 0.0, -1000));
        sphere.getMaterial().setN(20);
        sphere.setEmmission(new Color(0, 0, 100));

        scene.addGeometry(sphere);

        Triangle triangle1 = new Triangle(new Point3D(3500, 3500, -2000),
                new Point3D(-3500, -3500, -1000),
                new Point3D(3500, -3500, -2000));

        Triangle triangle2 = new Triangle(new Point3D(3500, 3500, -2000),
                new Point3D(-3500, 3500, -1000),
                new Point3D(-3500, -3500, -1000));

        scene.addGeometry(triangle1);
        scene.addGeometry(triangle2);

        scene.addLight(new SpotLight(new Color(255, 100, 100), new Point3D(200, 200, -100),
                new Vector(-2, -2, -3), 0, 0.000001, 0.0000005));


        ImageWriter imageWriter = new ImageWriter("Shadow test", 500, 500, 500, 500);

        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();

    }

    public void test17() {
        Scene scene = new Scene();
        scene.setScreenDistance(200);

        Sphere sphere = new Sphere(500, new Point3D(0.0, 0.0, -1000));
        sphere.getMaterial().setN(20);
        sphere.setEmmission(new Color(0, 0, 100));
        sphere.getMaterial().setKt(0.5);
        scene.addGeometry(sphere);

        Sphere sphere2 = new Sphere(250, new Point3D(0.0, 0.0, -1000));
        sphere2.getMaterial().setN(20);
        sphere2.setEmmission(new Color(100, 20, 20));
        sphere2.getMaterial().setKt(0);
        scene.addGeometry(sphere2);

        scene.addLight(new SpotLight(new Color(255, 100, 100), new Point3D(-200, -200, -150),
                new Vector(2, 2, -3), 0.1, 0.00001, 0.000005));

        ImageWriter imageWriter = new ImageWriter("Recursive Test 1", 500, 500, 500, 500);

        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();

    }

    public void test18() {
        Scene scene = new Scene();
        scene.setScreenDistance(200);

        Sphere sphere = new Sphere(300, new Point3D(-550, -500, -1000));
        sphere.getMaterial().setN(20);
        sphere.setEmmission(new Color(0, 0, 100));
        sphere.getMaterial().setKt(0.5);
        scene.addGeometry(sphere);

        Sphere sphere2 = new Sphere(150, new Point3D(-550, -500, -1000));
        sphere2.getMaterial().setN(20);
        sphere2.setEmmission(new Color(100, 20, 20));
        sphere2.getMaterial().setKt(0);
        scene.addGeometry(sphere2);

        Triangle triangle = new Triangle(new Point3D(1500, -1500, -1500),
                new Point3D(-1500, 1500, -1500),
                new Point3D(200, 200, -375));

        Triangle triangle2 = new Triangle(new Point3D(1500, -1500, -1500),
                new Point3D(-1500, 1500, -1500),
                new Point3D(-1500, -1500, -1500));

        triangle.setEmmission(new Color(20, 20, 20));
        triangle2.setEmmission(new Color(20, 20, 20));
        triangle.getMaterial().setKr(1);
        triangle2.getMaterial().setKr(0.5);
        scene.addGeometry(triangle);
        scene.addGeometry(triangle2);

//        scene.addLight(new SpotLight(new Color(255, 100, 100),  new Point3D(200, 200, -150),
//                new Vector(-2, -2, -3), 0, 0.00001, 0.000005));
        scene.addLight(new SpotLight(new Color(255, 100, 100), new Point3D(200, 100, -50),
                new Vector(-2, -2, -3), 0, 0.000001, 0.0000005));
        ImageWriter imageWriter = new ImageWriter("Recursive Test 2", 500, 500, 500, 500);

        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }

    public void test19() {
        Scene scene = new Scene();
        scene.setScreenDistance(100);

        Sphere sphere = new Sphere(300, new Point3D(0, 0, -1000));
        sphere.getMaterial().setN(20);
        sphere.setEmmission(new Color(0, 0, 100));
        sphere.getMaterial().setKt(0.5);
        scene.addGeometry(sphere);

        Sphere sphere2 = new Sphere(150, new Point3D(0, 0, -1000));
        sphere2.getMaterial().setN(20);
        sphere2.setEmmission(new Color(100, 20, 20));
        sphere2.getMaterial().setKt(0);
        scene.addGeometry(sphere2);

        Triangle triangle = new Triangle(new Point3D(2000, -1000, -1500),
                new Point3D(-1000, 2000, -1500),
                new Point3D(700, 700, -375));

        Triangle triangle2 = new Triangle(new Point3D(2000, -1000, -1500),
                new Point3D(-1000, 2000, -1500),
                new Point3D(-1000, -1000, -1500));

        triangle.setEmmission(new Color(20, 20, 20));
        triangle2.setEmmission(new Color(20, 20, 20));
        triangle.getMaterial().setKr(1);
        triangle2.getMaterial().setKr(0.5);
        scene.addGeometry(triangle);
        scene.addGeometry(triangle2);

        scene.addLight(new SpotLight(new Color(255, 100, 100), new Point3D(200, 200, -150),
                new Vector(-2, -2, -3), 0, 0.00001, 0.000005));

        ImageWriter imageWriter = new ImageWriter("Recursive Test 3", 500, 500, 500, 500);

        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }

    public void testSphereAndSpot() {
        Scene scene = new Scene();
        scene.getCamera().setP0(new Point3D(0, 0, 0));
        scene.setScreenDistance(300);

        Sphere sphere = new Sphere(80, new Point3D(0, 0, -150));
        sphere.setEmmission(new Color(0, 0, 100));
        scene.addGeometry(sphere);

        Quadrangle quadrangle = new Quadrangle(
                new Point3D(10, 20, -150),new Point3D(0, 0, -150),
                new Point3D(50, 0, -80),
                new Point3D(10, 20, -80));
        quadrangle.setMaterial(15, 0.1, 0.4, 0.2,1);
        quadrangle.setEmmission(new Color(133, 133, 133));
        scene.addGeometry(quadrangle);

        Vector V = new Vector(new Point3D(0, 0, -1)).normalize();
        scene.addLight(new SpotLight(new Color(220, 230, 60), new Point3D(0, 0, 100),
                V, 0, 0.0001, 0.00005));

        ImageWriter imageWriter = new ImageWriter("SphereAndSpot", 500, 500, 500, 500);

        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }


    public void testPart3_01() {
        Scene scene = new Scene();
        scene.getCamera().setP0(new Point3D(0, 0, 0));
        scene.setScreenDistance(150);

        Material material = new Material(20, 0.55, 0.1, 0.6,0.4);
        Point3D pSphere = new Point3D(-50, -100, -150);
        Sphere sphere = new Sphere(50, pSphere);
        sphere.setEmmission(new Color(10, 100, 20));
        sphere.setMaterial(material);
        scene.addGeometry(sphere);

        Point3D pSphere1 = new Point3D(-30, 0, -250);
        Sphere sphere1 = new Sphere(70,pSphere1);
        sphere1.setEmmission(new Color(110, 20, 10));
        sphere1.setMaterial(material);
        scene.addGeometry(sphere1);

        Point3D pSphere2 = new Point3D(-10, 150, -350);
        Sphere sphere2 = new Sphere(90,pSphere2 );
        sphere2.setEmmission(new Color(20, 20, 100));
        sphere2.setMaterial(material);
        scene.addGeometry(sphere2);


        Plane plane = new Plane(new Vector(1,0,0), new Point3D(-100, 0 , 0));
        plane.setMaterial(15, 0.1, 0.4, 0.2,1);
        plane.setEmmission(new Color(133, 133, 133));
        scene.addGeometry(plane);

        scene.addLight(new PointLight(new Color(130, 100, 130), new Point3D(150, 150, -50), 0, 0.00001, 0.000005));
        scene.addLight(new PointLight(new Color(110, 130, 30), new Point3D(150, 150, -50), 0, 0.00001, 0.000005));
//        scene.addLight(new PointLight(new Color(90, 30, 130), new Point3D(150, -150, -50), 0, 0.00001, 0.000005));


        ImageWriter imageWriter = new ImageWriter("testPart3_01", 500, 500, 500, 500);

        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }

    public void testPart3_02() {
        Scene scene = new Scene();
        scene.getCamera().setP0(new Point3D(0, 0, 0));
        scene.setScreenDistance(150);

        Material material = new Material(20, 0.35, 0.1, 0.6,0.4);

        Point3D pSphere = new Point3D(-50, -100, -150);
        Sphere sphere = new Sphere(50, pSphere);
        sphere.setEmmission(new Color(10, 100, 20));
        sphere.setMaterial(material);
        scene.addGeometry(sphere);

        Point3D pSphere1 = new Point3D(-30, 0, -250);
        Sphere sphere1 = new Sphere(70,pSphere1);
        sphere1.setEmmission(new Color(110, 20, 10));
        sphere1.setMaterial(material);
        scene.addGeometry(sphere1);

        Point3D pSphere2 = new Point3D(-10, 150, -350);
        Sphere sphere2 = new Sphere(90,pSphere2 );
        sphere2.setEmmission(new Color(20, 20, 100));
        sphere2.setMaterial(material);
        scene.addGeometry(sphere2);

        Plane plane = new Plane(new Vector(1,0,0), new Point3D(-100, 0 , 0));
        plane.setMaterial(15, 0.1, 0.4, 0.2,1);
        plane.setEmmission(new Color(133, 133, 133));
        scene.addGeometry(plane);

        scene.addLight(new SpotLight(new Color(130, 100, 130), new Point3D(150, 150, -50), //right light
                pSphere.vector(new Point3D(300, 0, -250)), 0, 0.00001, 0.000005));
        scene.addLight(new SpotLight(new Color(110, 130, 30), new Point3D(150, 150, -50), //right light
                pSphere1.vector(new Point3D(300, 0, -250)), 0, 0.00001, 0.00005));
        scene.addLight(new SpotLight(new Color(90, 30, 130), new Point3D(150, 150, -50), //right light
                pSphere2.vector(new Point3D(300, 0, -250)), 0, 0.00001, 0.00005));

        ImageWriter imageWriter = new ImageWriter("testPart3_02", 500, 500, 500, 500);

        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }

    public void testPart3_03() {
        Scene scene = new Scene();
        scene.getCamera().setP0(new Point3D(0, 0, 0));
        scene.setScreenDistance(200);

        Sphere sphere = new Sphere(80, new Point3D(0, 0, -250));
        sphere.setEmmission(new Color(0, 0, 100));
        scene.addGeometry(sphere);

        Triangle triangle = new Triangle(new Point3D(0, 0, -450), //green triangle
                new Point3D(-2000, 0, -500),
                new Point3D(0, -4000, -500));
        triangle.setEmmission(new Color(0, 50, 0));
        scene.addGeometry(triangle);

        Triangle triangle1 = new Triangle(new Point3D(100, 100, -100), //red triangle
                new Point3D(90, 200, -90),
                new Point3D(-50, 100, -100));
        triangle1.setEmmission(new Color(80, 0, 0));
        scene.addGeometry(triangle1);

        Triangle triangle2 = new Triangle(new Point3D(-2000, -2000, -2000), //gray triangle
                new Point3D(-2000, 500, -2000),
                new Point3D(1500, 800, -800));
        triangle2.setEmmission(new Color(33, 33, 33));
        scene.addGeometry(triangle2);

        scene.addLight(new SpotLight(new Color(100, 80, 0), new Point3D(150, 150, -50), //right light
                new Point3D(0, 0, -100).vector(new Point3D(50, 0, 0)), 0, 0.000001, 0.0000005));

        Vector V = new Vector(new Point3D(-0.2, -0.6, -1)).normalize();
        scene.addLight(new SpotLight(new Color(220, 230, 60), new Point3D(0, 0, -350), //light behind the sphere
                V, 0, 0.00001, 0.00005));

        ImageWriter imageWriter = new ImageWriter("testPart3_03", 500, 500, 500, 500);

        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }

    public void testQuadrangle(){

        // regular table

        Scene scene = new Scene();
        scene.setScreenDistance(300);
        Camera cam = scene.getCamera();
        cam.setP0(new Point3D(0,0,0));
        scene.setCamera(cam);

        Plane plane = new Plane(new Vector(new Point3D(0,1,0)), new Point3D(0,-300,0));

        double tableLegRadius = 5;

        double tableThickness = 12;

        Point3D a = new Point3D(-125,50,-300);
        Point3D b = new Point3D(125,50,-300);
        Point3D c = new Point3D(125,50,-800);
        Point3D d = new Point3D(-125,50,-800);

        // A
        Vector u = a.vector(b).normalize();
        Vector v = a.vector(d).normalize();
        u = u.add(v);
        u = u.normalize();
        u = u.scale(tableLegRadius +25);
        Point3D A = new Point3D(a);
        A = A.add(u);


        // B
        u = b.vector(a).normalize();
        v = b.vector(c).normalize();
        u = u.add(v);
        u = u.normalize();
        u = u.scale(tableLegRadius +25);
        Point3D B = new Point3D(b);
        B = B.add(u);


        // C
        u = c.vector(d).normalize();
        v = c.vector(b).normalize();
        u = u.add(v);
        u = u.normalize();
        u = u.scale(tableLegRadius +25);
        Point3D C = new Point3D(c);
        C = C.add(u);


        // D
        u = d.vector(c).normalize();
        v = d.vector(a).normalize();
        u = u.add(v);
        u = u.normalize();
        u = u.scale(tableLegRadius +25);
        Point3D D = new Point3D(d);
        D = D.add(u);

        Point3D A1 = new Point3D(A.getX().getCoordinate(),A.getY().getCoordinate()+tableThickness,A.getZ().getCoordinate());
        Point3D B1 = new Point3D(B.getX().getCoordinate(),B.getY().getCoordinate()+tableThickness,B.getZ().getCoordinate());
        Point3D C1 = new Point3D(C.getX().getCoordinate(),C.getY().getCoordinate()+tableThickness,C.getZ().getCoordinate());
        Point3D D1 = new Point3D(D.getX().getCoordinate(),D.getY().getCoordinate()+tableThickness,D.getZ().getCoordinate());



        Quadrangle q1 = new Quadrangle(A,B,C,D);
        Quadrangle q2 = new Quadrangle(A1,B1,C1,D1);
        Quadrangle q3 = new Quadrangle(A,B,B1,A1);
        Quadrangle q4 = new Quadrangle(B,C,C1,B1);
        Quadrangle q5 = new Quadrangle(C,D,D1,C1);
        Quadrangle q6 = new Quadrangle(D,A,A1,D1);
        Sphere sphere = new Sphere(100, new Point3D(600,800,-750));
        sphere.setEmmission(new Color(169, 164, 166));


        plane.setEmmission(new Color(0,60,110));
        q1.setEmmission(new Color(3, 120, 0));
        q2.setEmmission(new Color(3, 120, 0));
        q3.setEmmission(new Color(3, 120, 0));
        q4.setEmmission(new Color(3, 120, 0));
        q5.setEmmission(new Color(3, 120, 0));
        q6.setEmmission(new Color(3, 120, 0));

        scene.addGeometry(plane);
        scene.addGeometry(sphere);
        scene.addGeometry(q1);
        scene.addGeometry(q2);
        scene.addGeometry(q3);
        scene.addGeometry(q4);
        scene.addGeometry(q5);
        scene.addGeometry(q6);

        scene.addLight(new SpotLight(new Color(200, 200, 255),  new Point3D(300, 650, -50),new Vector(new Point3D(-2, -2, -3)),
                0, 0.00001, 0.000005));
        scene.addLight(new SpotLight(new Color(200, 200, 80),  new Point3D(0,0,0),new Vector(new Point3D(600, 800,-750)),
                0, 0.00001, 0.00005));


        ImageWriter imageWriter = new ImageWriter("Test Quadrangle", 1000, 1000, 500, 500);

        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }

    public void testRecursive(){
        Scene scene = new Scene();
        scene.getCamera().set_vTo(new Vector(0.35,0,-1));
        scene.getCamera().setP0(new Point3D(-500,0,0));
        scene.setScreenDistance(120);
        scene.setBackground(new Color(255, 246, 200));

        Sphere sphere = new Sphere(300, new Point3D(-250, -250, -1000));
        sphere.getMaterial().setN(20);
        sphere.setEmmission(new Color(0, 0, 100));
        sphere.getMaterial().setKt(0.5);
        scene.addGeometry(sphere);

        Sphere sphere2 = new Sphere(150, new Point3D(-250, -250, -1000));
        sphere2.getMaterial().setN(20);
        sphere2.setEmmission(new Color(100, 20, 20));
        sphere2.getMaterial().setKt(0);
        scene.addGeometry(sphere2);

        Sphere sphere3 = new Sphere(300, new Point3D(250, 250, -1000));
        sphere3.getMaterial().setN(20);
        sphere3.setEmmission(new Color(0, 0, 100));
        sphere3.getMaterial().setKt(0.5);
        scene.addGeometry(sphere3);

        Sphere sphere4 = new Sphere(150, new Point3D(250, 250, -1000));
        sphere4.getMaterial().setN(20);
        sphere4.setEmmission(new Color(100, 20, 20));
        sphere4.getMaterial().setKt(0);
        scene.addGeometry(sphere4);

        Triangle triangle = new Triangle(new Point3D(  1800, 0, -1500),
                new Point3D( -1000,  1500, -1500),
                new Point3D( -1000, -1500, -1500));
        triangle.setEmmission(new Color(28, 52, 255));
//        triangle.getMaterial().setKr(0.5);
        triangle.getMaterial().setKd(1);
        scene.addGeometry(triangle);

        Triangle triangle2 = new Triangle(new Point3D(  1000, 1500, -1505),
                new Point3D( -1800,  0, -1505),
                new Point3D( 1000, -1500, -1505));
        triangle2.setEmmission(new Color(28, 52, 255));
//        triangle2.getMaterial().setKr(0.5);
        triangle2.getMaterial().setKd(1);
        scene.addGeometry(triangle2);

        Plane plane = new Plane(new Vector(0,1,0), new Point3D( 1000,  -1550, -1500));
        plane.setEmmission(new Color(60, 60, 60));
        plane.getMaterial().setKr(1);
        scene.addGeometry(plane);

        scene.addLight(new SpotLight(new Color(100, 100, 50),  new Point3D(200, 200, 0),
                new Vector(-2, -2, -3), 0, 0.000001, 0.0000005));

        ImageWriter imageWriter = new ImageWriter("MagenDavid", 500, 500, 500, 500);

        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }
}