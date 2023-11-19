package shape;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * A class representing a circle
 */
public class Circle {

    public Point2D.Double centre;
    public double radius;

    
    public Circle(Point2D.Double centre, double radius)
    {
        this.centre = centre;
        this.radius = radius;
    }

    /**
     * creates a circumCircle from 3 points
     * @param p
     * @param q
     * @param r
     */
    public Circle (Point p, Point q, Point r)
    {
        init(new Point2D.Double(p.x, p.y), new Point2D.Double(q.x, q.y), new Point2D.Double(r.x, r.y));
    }


    /**
     * creates a circumCircle from 3 points
     * @param p
     * @param q
     * @param r
     */
    public Circle(Point2D.Double p, Point2D.Double q, Point2D.Double r)
    {
        init(p, q, r);
    }

    private void init(Point2D.Double p, Point2D.Double q, Point2D.Double r)
    {
        //find equation of line pq
        //line pq y = ax+b
        // where a = dy/dx
        // b = y-ax

        double a = (p.y-q.y)/(p.x-q.x);
        //double b_0 = p.y-a_0*p.x;

        //find perpendicular bicetor
        //perp line to pq y = 1/ax + c

        //bicetor goes through the centre point of pq
        Point2D.Double cent_0 = new Point2D.Double((p.x+q.x)/2, (p.y+q.y)/2);
        double _a = -1/a;
        double c = cent_0.y - _a*cent_0.x;

        //find equation of line qr with same method
        //line pq y = dx+e
        

        double d = (p.y-r.y)/(p.x-r.x);


        //bicetor goes through the centre point of pr
        Point2D.Double cent_1 = new Point2D.Double((p.x+r.x)/2, (p.y+r.y)/2);

        //perp line to pq y = 1/dx + e
        double _d = -1/d;
        double e = cent_1.y - _d*cent_1.x;

        //solve to find point of intersection
        //y = a'x+c 
        //y = d'x+e 
        //a'x+c = d'x+e
        //a'x - d'x = e-c
        //(a'-d')x = e-c
        //x = (e-c)/(a'-d')

        double x = (e-c)/(_a-_d);
        double y = _a*x + c; 

        centre = new Point2D.Double(x, y);
        radius = centre.distance(p);
    }

    /**
     * Calculates if point p is within the circle
     * @param p
     * @return
     */
    public boolean contains(Point2D.Double p)
    {
        return p.distance(centre) < radius;
    }

    /**
     * Calculates if point p is within the circle
     * @param p
     * @return
     */
    public boolean contains(Point p)
    {
        return p.distance(centre) < radius;
    }


    /**
     * Calculates if point P is in the circumcircle of triangle abc.
     * does not create the circumcircle itself therefore may be performant if the circle is not needed. 
     * @param a
     * @param b
     * @param c
     * @param p
     * @return
     */
    public static boolean contains(Point a, Point b, Point c, Point p)
    {
        Point A = new Point(a);
        Point B = new Point(b);
        Point C = new Point(c);

        int isCCW = isCCW(a, b, c);

        if(isCCW == 0)
        {
            return false;
        }
        if(isCCW<0)
        {
            //make counter clockwise
            Point copy = C;
            C = A;
            A = copy;
        }
        //insideness can be calculated using the determinant
        //      |ax-px ay-py (ax-px)^2+(ay+py)^2|
        //det = |bx-px by-py (bx-px)^2+(by+py)^2|
        //      |cx-px cy-py (cx-px)^2+(cy+py)^2|
        //
        //if points abc are anti clockwise:
        //  if det > 0  point p is inside  the circumcircle of abc
        //  if det = 0  point p is on      the circumcircle of abc
        //  if det < 0  point p is outside the circumcircle of abc


        int m11 = A.x - p.x;
        int m12 = A.y - p.y;
        long m13 = (A.x-p.x) * (A.x-p.x) + (A.y-p.y) * (A.y-p.y);

        int m21 = B.x - p.x;
        int m22 = B.y - p.y;
        long m23 = (B.x-p.x) * (B.x-p.x) + (B.y-p.y) * (B.y-p.y);

        int m31 = C.x - p.x;
        int m32 = C.y - p.y;
        long m33 = (C.x-p.x) * (C.x-p.x) + (C.y-p.y) * (C.y-p.y); 

        
        //|A| = a11 (a22 a33 – a23 a32) – a12 (a21 a33 – a23 a31) + a13 (a21 a32 – a22 a31)

        return     (m11 * (m22 * m33 - m23 * m32) -
                    m12 * (m21 * m33 - m23 * m31) +
                    m13 * (m21 * m32 - m22 * m31)   ) > 0;
        
    }

    /**
     * Calculates if the points a->b->c are counter clockwise
     * @param a
     * @param b
     * @param c
     * @return  Positive if a->b->c are ccw
     *          Negative if a->b->c are clockwise
     *          0        if a->b->c is collinear
     */
    private static int isCCW(Point a, Point b, Point c)
    {
        //Calculates the double area
        return (b.x-a.x) * (c.y-a.y) - (c.x-a.x)*(b.y-a.y);
    }
}
