package tests;

import elements.Camera;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import junit.framework.TestCase;
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
        assertTrue(result.compareTo(new Point3D(6.0, -7.0, 9.0)) == 0 );
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

    public void test06(){
        System.out.println("Test06: Vector Add test");

        Vector v1 = new Vector(1.0, 1.0, 1.0);
        Vector v2 = new Vector(-1.0, -1.0, -1.0);

        Vector v3 = v1.add(v2);
        assertTrue(v3.compareTo(new Vector(0.0,0.0,0.0)) == 0);

        v3 = v2.add(v1);
        assertTrue(v3.compareTo(v3) == 0);
    }

    public void test07(){
        System.out.println("Test07: Vector Substruct test");

        Vector v1 = new Vector(1.0, 1.0, 1.0);
        Vector v2 = new Vector(-1.0, -1.0, -1.0);

        Vector v3 = v1.subtract(v2);
        assertTrue(v3.compareTo(new Vector(2.0,2.0,2.0)) == 0);

        v3 = v2.subtract(v1);
        assertTrue(v3.compareTo(new Vector(-2.0,-2.0,-2.0)) == 0);
    }

    public void test08(){
        System.out.println("Test08: Vector Scaling test");

        Vector v1 = new Vector(1.0, 1.0, 1.0);

        Vector v2 = v1.scale(1);
        assertTrue(v2.compareTo(v1) == 0);

        v2 = v1.scale(2);
        assertTrue(v2.compareTo(new Vector(2.0,2.0,2.0)) == 0);

        v2 = v1.scale(-2);
        assertTrue(v2.compareTo(new Vector(-2.0,-2.0,-2.0)) == 0);
    }

    public void test09(){
        System.out.println("Test09: Vector Dot product test");


        Vector v1 = new Vector(3.5,-5,10);
        Vector v2 = new Vector(2.5,7,0.5);

        assertTrue(Double.compare(v1.dotProduct(v2), (8.75 + -35 + 5)) == 0);

    }

    public void test10() {
        System.out.println("Test10: Vector Length test");

        Vector v = new Vector(3.5,-5,10);
        assertTrue(v.length() ==  Math.sqrt(12.25 + 25 + 100));
    }

    public void test11(){
        System.out.printf("Test11: Vector Normalize test -> ");

        Vector v = new Vector(100,-60.781,0.0001);
        System.out.printf("Length = %f  ", v.length());
        Vector v1 = v.normalize();
        System.out.printf("Length = %f\n", v1.length());

        assertEquals("Incorrect length after normalize! ", 1, v1.length(), 1e-10);

        v = new Vector(0,0,0);

        try {
            v1 = v.normalize();
            fail("Didn't throw divide by zero exception!");
        } catch (ArithmeticException e) {
            assertTrue(true);
        }

    }

    public void test12(){
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

    public void test13(){
        final int WIDTH = 3;
        final int HEIGHT = 3;
        Ray[][] rays = new Ray [HEIGHT][WIDTH];
        Camera camera = new Camera(new Point3D(0.0 ,0.0 ,0.0),
                new Vector (0.0, 1.0, 0.0),
                new Vector (0.0, 0.0, -1.0));
// plane orthogonal to the view plane
        Plane plane = new Plane(new Vector(0.0, 0.0, -1.0), new Point3D(0.0, 0.0, -3.0));
// 45 degrees to the view plane
        Plane plane2 = new Plane(new Vector(0.0, 0.25, -1.0), new Point3D(0.0, 0.0, -3.0));
        List<Point3D> intersectionPointsPlane = new ArrayList<Point3D>();
        List<Point3D> intersectionPointsPlane2 = new ArrayList<Point3D>();
        System.out.println("Camera:\n" + camera);
        for (int i = 0; i < HEIGHT; i++){
            for (int j = 0; j < WIDTH; j++){
                rays[i][j] = camera.constructRayThroughPixel(
                        WIDTH, HEIGHT, j, i, 1, 3 * WIDTH, 3 * HEIGHT);
                List<Point3D> rayIntersectionPoints = plane. FindIntersections(rays[i][j]);
                List<Point3D> rayIntersectionPoints2 = plane2.FindIntersections(rays[i][j]);
                for (Point3D iPoint: rayIntersectionPoints)
                    intersectionPointsPlane.add(iPoint);
                for (Point3D iPoint: rayIntersectionPoints2)
                    intersectionPointsPlane2.add(iPoint);
            }
        }
        assertTrue(intersectionPointsPlane. size() == 9);
        assertTrue(intersectionPointsPlane2.size() == 9);
        for (Point3D iPoint: intersectionPointsPlane)
            System.out.println(iPoint);
        System.out.println("---");
        for (Point3D iPoint: intersectionPointsPlane2)
            System.out.println(iPoint);
    }

    public void test14(){

        ImageWriter imageWriter = new ImageWriter("Image writer test", 500, 500, 1, 1);
        Random rand = new Random();
        for (int i = 0; i < imageWriter.getHeight(); i++){
            for (int j = 0; j < imageWriter.getWidth(); j++)
            {

                if (i % 25 == 0 || j % 25 == 0 || i == j || i == 499 || j == 499 || i == 500 - j)
                    imageWriter.writePixel(j, i, 0, 0, 0);  // Black
                else
                if(i >= 200 && i <= 300 && j >= 200 && j <= 300)
                    imageWriter.writePixel(j, i, 255, 0, 0); // Red
                else
                if(i >= 150 && i <= 350 && j >= 150 && j <= 350)
                    imageWriter.writePixel(j, i, 0, 255, 0);  // Green
                else
                if(i >= 100 && i <= 400 && j >= 100 && j <= 400)
                    imageWriter.writePixel(j, i, 0, 0, 255); // Blue
                else
                if(i >= 50 && i <= 450 && j >= 50 && j <= 450)
                    imageWriter.writePixel(j, i, 255, 255, 0); // Yellow
                else
                    imageWriter.writePixel(j, i, rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)); // Random
            }
        }

        imageWriter.writeToimage();

    }

    public void test15(){
        Scene scene = new Scene();
        scene.setScreenDistance(40);

        Sphere sphere = new Sphere(50, new Point3D(0.0, 0.0, -50));
        Triangle triangle1 = new Triangle(new Point3D( 150, 0, -50),
                new Point3D(  0, 150, -50),
                new Point3D( 150, 150, -50));

        Triangle triangle2 = new Triangle(new Point3D( 150, 0, -50),
                new Point3D(  0, -150, -50),
                new Point3D( 150,-150, -50));

        Triangle triangle3 = new Triangle(new Point3D(-150, 0, -50),
                new Point3D(  0, 150, -50),
                new Point3D(-150, 150, -50));

        Triangle triangle4 = new Triangle(new Point3D(-150, 0, -50),
                new Point3D(  0,  -150, -50),
                new Point3D(-150, -150, -50));

        sphere.setEmmission(new Color (0, 1, 255));
        triangle1.setEmmission(new Color (255, 0, 0));
        triangle2.setEmmission(new Color (255, 255, 0));
        triangle3.setEmmission(new Color (0, 255, 0));
        triangle4.setEmmission(new Color(255, 67, 166));

        scene.addGeometry(sphere);
        scene.addGeometry(triangle1);
        scene.addGeometry(triangle2);
        scene.addGeometry(triangle3);
        scene.addGeometry(triangle4);

        ImageWriter imageWriter = new ImageWriter("Emmission test", 500, 500, 500, 500);

        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.printGrid(50);
        render.writeToImage();
    }

    public void test16(){
    }

    public void test17(){
    }

    public void test18(){
    }

    public void test19(){
    }


    //DAVID COHEN
    public void test20() {

        final int WIDTH  = 11;
        final int HEIGHT = 11;

        Point3D[][] screen = new Point3D [HEIGHT][WIDTH];

        Camera camera = new Camera(new Point3D(0.0 ,0.0 ,0.0),
                new Vector (0.0, 1.0, 0.0),
                new Vector (0.0, 0.0, -1.0));

        System.out.println("Test13: Camera test:\n" + camera);

        for (int i = 0; i < HEIGHT; i++)
        {
            for (int j = 0; j < WIDTH; j++)
            {

                Ray ray = camera.constructRayThroughPixel(WIDTH, HEIGHT, j, i, 1, 3 * WIDTH, 3 * HEIGHT);

                screen[i][j] = ray.getPOO();

                System.out.print(screen[i][j]);
                System.out.println(ray.getDirection());

                // Checking z-coordinate
                assertTrue(Double.compare(screen[i][j].getZ().getCoordinate(), -1.0) == 0);

                // Checking all options
                double x = screen[i][j].getX().getCoordinate();
                double y = screen[i][j].getY().getCoordinate();
                double[] values = {0.0,3.0,6.0,9.0,12.0,15.0};

                int k = 0;
                for(; k < values.length; k++)
                    if(values[k] == Math.abs(x))
                        break;
                if(k == values.length)
                    fail("Wrong x coordinate");

                k = 0;
                for(; k < values.length; k++)
                    if(values[k] == Math.abs(y))
                        break;
                if(k == values.length)
                    fail("Wrong Y coordinate");
            }
            System.out.println("---");
        }

    }
    /************************************** Triangle test ******************************************************/
    public void test21() {
        final int WIDTH  = 3;
        final int HEIGHT = 3;

        System.out.println("Test14: Triangle intersection test");

        Ray[][] rays = new Ray [HEIGHT][WIDTH];

        Camera camera = new Camera(new Point3D(0.0 ,0.0 ,0.0),
                new Vector (0.0, 1.0, 0.0),
                new Vector (0.0, 0.0, -1.0));

        Triangle triangle1 = new Triangle(new Point3D( 0,  1, -2),
                new Point3D( 1, -1, -2),
                new Point3D(-1, -1, -2));

        Triangle triangle2 = new Triangle(new Point3D( 0,  10, -2),
                new Point3D( 1, -1, -2),
                new Point3D(-1, -1, -2));

        List<Point3D> intersectionPointsTriangle1 = new ArrayList<Point3D>();
        List<Point3D> intersectionPointsTriangle2 = new ArrayList<Point3D>();

        System.out.println("Camera:\n" + camera);

        for (int i = 0; i < HEIGHT; i++){
            for (int j = 0; j < WIDTH; j++){

                rays[i][j] = camera.constructRayThroughPixel(
                        WIDTH, HEIGHT, j, i, 1, 3 * WIDTH, 3 * HEIGHT);

                List<Point3D> rayIntersectionPoints1  = triangle1.FindIntersections(rays[i][j]);
                List<Point3D> rayIntersectionPoints2  = triangle2.FindIntersections(rays[i][j]);

                for (Point3D iPoint: rayIntersectionPoints1)
                    intersectionPointsTriangle1.add(iPoint);

                for (Point3D iPoint: rayIntersectionPoints2)
                    intersectionPointsTriangle2.add(iPoint);
            }
        }

        assertTrue(intersectionPointsTriangle1. size() == 1);
        assertTrue(intersectionPointsTriangle2.size() == 2);
        System.out.println("Intersection Points:");
        for (Point3D iPoint: intersectionPointsTriangle1){
            System.out.println(iPoint);
            assertEquals("(0.00, 0.00, -2.00)", iPoint.toString());
        }
        System.out.println("--");
        for(int i = 0; i < intersectionPointsTriangle2.size(); i++)
        {
            Point3D iPoint = intersectionPointsTriangle2.get(i);
            System.out.println(iPoint);
            switch(i)
            {
                case 0:
                    assertEquals("(0.00, 6.00, -2.00)", iPoint.toString());
                    break;
                case 1:
                    assertEquals("(0.00, 0.00, -2.00)", iPoint.toString());
                    break;
            }
        }
    }
    public void test22() {
        System.out.print("Test15: Triangle getNormal test: ");
        Triangle triangle = new Triangle(new Point3D( 0,  1, -2),
                new Point3D( 1, -1, -2),
                new Point3D(-1, -1, -2));
        Vector normal = triangle.getNormal(new Point3D());
        System.out.println(normal);
        assertEquals("Bad normal! ","(0.00, 0.00, 1.00)", normal.toString());
    }
    /************************************** Sphere test ******************************************************/
    public void test23() {

        final int WIDTH = 3;
        final int HEIGHT = 3;

        System.out.println("Test16: Sphere intersection test");

        Ray[][] rays = new Ray[HEIGHT][WIDTH];

        Camera camera = new Camera(new Point3D(0.0, 0.0, 0.0),
                new Vector(0.0, 1.0, 0.0),
                new Vector(0.0, 0.0, -1.0));

        Sphere sphere1 = new Sphere(1, new Point3D(0.0, 0.0, -3.0));
        Sphere sphere2 = new Sphere(10, new Point3D(0.0, 0.0, -3.0));

        // Only the center ray intersect the sphere in two locations
        List<Point3D> intersectionPointsSphere1 = new ArrayList<Point3D>();

        // The sphere encapsulates the view plane - all rays intersect with the sphere once
        List<Point3D> intersectionPointsSphere2 = new ArrayList<Point3D>();

        System.out.println("Camera:\n" + camera);

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {

                rays[i][j] = camera.constructRayThroughPixel(
                        WIDTH, HEIGHT, j, i, 1, 3 * WIDTH, 3 * HEIGHT);

                List<Point3D> rayIntersectionPoints1 = sphere1.FindIntersections(rays[i][j]);
                List<Point3D> rayIntersectionPoints2 = sphere2.FindIntersections(rays[i][j]);

                for (Point3D iPoint : rayIntersectionPoints1)
                    intersectionPointsSphere1.add(iPoint);

                for (Point3D iPoint : rayIntersectionPoints2)
                    intersectionPointsSphere2.add(iPoint);

            }
        }

        assertTrue(intersectionPointsSphere1.size() == 2);
        assertTrue(intersectionPointsSphere2.size() == 9);

        System.out.println("Intersection Points:");
        for (Point3D iPoint : intersectionPointsSphere1) {
            assertTrue(iPoint.compareTo(new Point3D(0.0, 0.0, -2.0)) == 0 ||
                    iPoint.compareTo(new Point3D(0.0, 0.0, -4.0)) == 0);
            System.out.println(iPoint);
        }
    }
    public void test24() {
        System.out.print("Test17: Sphere getNormal test: ");
        Sphere sphere = new Sphere(10, new Point3D(0.0, 0.0, -3.0));
        Vector normal = sphere.getNormal(new Point3D(5.0,5.0,-3.0));
        System.out.println(normal);
        assertEquals("(0.71, 0.71, 0.00)", normal.toString());
    }
    /************************************ Plane test *****************************************************/
    public void test25() {

        final int WIDTH  = 3;
        final int HEIGHT = 3;

        System.out.println("Test18: Plane intersection test");

        Ray[][] rays = new Ray [HEIGHT][WIDTH];

        Camera camera = new Camera(new Point3D(0.0 ,0.0 ,0.0),
                new Vector (0.0, 1.0, 0.0),
                new Vector (0.0, 0.0, -1.0));

        // plane orthogonal to the view plane
        Plane plane1  = new Plane(new Vector(0.0, 0.0, -1.0), new Point3D(0.0, 0.0, -3.0));

        // 45 degrees to the view plane
        Plane plane2 = new Plane(new Vector(0.0, 0.25, -1.0), new Point3D(0.0, 0.0, -3.0));

        List<Point3D> intersectionPointsPlane1 = new ArrayList<Point3D>();
        List<Point3D> intersectionPointsPlane2 = new ArrayList<Point3D>();

        System.out.println("Camera:\n" + camera);

        for (int i = 0; i < HEIGHT; i++){
            for (int j = 0; j < WIDTH; j++){

                rays[i][j] = camera.constructRayThroughPixel(
                        WIDTH, HEIGHT, j, i, 1, 3 * WIDTH, 3 * HEIGHT);

                List<Point3D> rayIntersectionPoints1  = plane1.FindIntersections(rays[i][j]);
                List<Point3D> rayIntersectionPoints2  = plane2.FindIntersections(rays[i][j]);

                for (Point3D iPoint: rayIntersectionPoints1)
                    intersectionPointsPlane1.add(iPoint);

                for (Point3D iPoint: rayIntersectionPoints2)
                    intersectionPointsPlane2.add(iPoint);
            }
        }

        assertTrue(intersectionPointsPlane1.size() == 9);
        assertTrue(intersectionPointsPlane2.size() == 9);

        System.out.println("Plane1 intersetions");
        for (Point3D iPoint: intersectionPointsPlane1)
            System.out.println(iPoint);
        System.out.println("---");
        System.out.println("Plane2 intersetions");
        for (Point3D iPoint: intersectionPointsPlane2)
            System.out.println(iPoint);
    }

}