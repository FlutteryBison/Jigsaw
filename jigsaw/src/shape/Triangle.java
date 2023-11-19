package shape;

import java.awt.Point;
import java.awt.Rectangle;

import delaunaytriangulation.Edge;

public class Triangle {
    private Point a;
    private Point b;
    private Point c;

    private Edge ab;
    private Edge bc;
    private Edge ac;

    /**
     * Constructs a new triangle abc. a,b and c must all be different points.
     * @param a
     * @param b
     * @param c
     */
    public Triangle(Point a, Point b, Point c)
    {
        if(a.equals(b)||a.equals(c)||b.equals(c))
        {
            throw new IllegalArgumentException("Cannot make a triangle with the points: " + a.toString() +b.toString()+c.toString());
        }
        this.a = a;
        this.b = b;
        this.c = c;
        
        ab = new Edge(a, b);
        bc = new Edge(b, c);
        ac = new Edge(a, c);
    }

    //TODO impliment supertriangle from point array
    //TODO impliment smaller supertriangle
    // public static Triangle createSuperTriangle(Point[] points)
    // {
    //     return null;
    // }

    /**
     * Creates a super triangle that completely encloses a rectangle
     * @param r
     * @return
     */
    public static Triangle createSuperTriangle(Rectangle r)
    {
        //Creates a Triangle that completely encloses the rectangle
        //When calculating the triangle we aim for the one below with a shared bottom right point and 45 degree angles at the top and left
        //before returning this triangle it is expanded

        //             +
        //           +++
        //         +++++
        //       ooooooo
        //     ++ooooooo
        //   ++++ooooooo
        // ++++++ooooooo
        

        //get length c which is the width of the enclosing triangle minus the widht of the rectanle
        double angle = 45;
        double c = r.height/Math.tan(angle);

        double triWidth = r.width+c+10;

        //again make it slightly bigger to work with int points
        //This may mean the angles are not 45 but it will enclose the rectangle
        double triHeight = triWidth*Math.tan(angle)+10;
        
    
        //move the bottom right corner down and right to ensure no points exist on the triangle boundary
         return new Triangle(
             new Point( (int)Math.ceil(r.getMaxX())+10, (int)Math.ceil(r.getMaxY())+10),
             new Point((int)Math.floor(r.getMaxX() - triWidth), (int)Math.ceil(r.getMaxY())),
             new Point((int)Math.ceil(r.getMaxX()),(int)Math.floor((r.getMaxY()-triHeight)))
         );

    }

    public Point[] getPoints()
    {
        return new Point[]{a,b,c};
    }
    public Edge[] getEdges()
    {
        return new Edge[]{ab,bc,ac};
    }

    public boolean hasEdge(Point p1, Point p2)
    {
        return hasEdge(new Edge(p1, p2));
    }
    public boolean hasEdge(Edge e)
    {
        return ab.equals(e) || bc.equals(e) || ac.equals(e);
        
    }

    public boolean hasVertex(Point p)
    {
        return p.equals(a) || p.equals(b) || p.equals(c);
    }
}
