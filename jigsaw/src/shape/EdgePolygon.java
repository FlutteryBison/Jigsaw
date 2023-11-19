package shape;

import java.awt.Point;
import java.awt.Polygon;
import java.util.HashSet;

import delaunaytriangulation.Edge;

/**
 * Adds some Edge functionality to 
 */
public class EdgePolygon extends Polygon{

    //Store the edges.
    private HashSet<Edge> edges = new HashSet<>();

    //Dont update edges whenever the int[] arrays in the parent are updated.
    //flag that a change is made and only update when necassary.
    private boolean dirty;

    public EdgePolygon()
    {
        super();
    }

    public EdgePolygon(int[] xpoints, int[] ypoints, int npoints)
    {
        super(xpoints, ypoints, npoints);
        dirty = true;
    }

    public EdgePolygon(Point[] points)
    {
        
        super(getXArrFromPoints(points), getYArrFromPoints(points), points.length);
        dirty = true;
    }

    private static int[] getXArrFromPoints(Point[] points)
    {
        int[] ret = new int[points.length];
        for(int i=0; i<points.length;i++)
        {
            ret[i] = points[i].x;
        }
        return ret;
    }

    private static int[] getYArrFromPoints(Point[] points)
    {
        int[] ret = new int[points.length];
        for(int i=0; i<points.length;i++)
        {
            ret[i] = points[i].y;
        }
        return ret;
    }


    @Override
    public void addPoint(int x, int y)
    {
        super.addPoint(x, y);
        dirty = true;
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        dirty = true;
    }

    

    private void updateEdges()
    {
        edges.clear();
        for(int i=0; i<xpoints.length-1;i++)
        {
            edges.add(new Edge(new Point(xpoints[i],ypoints[i]), new Point(xpoints[i+1],ypoints[i+1])));
        }
        edges.add(new Edge(new Point(xpoints[xpoints.length-1],ypoints[ypoints.length-1]), new Point(xpoints[0],ypoints[0])));
        dirty =false;
    }

    /**
     * Returns if the polygon has the edge e.
     * 
     * @param e
     * @return
     */
    public boolean hasEdge(Edge e)
    {
        if(dirty)
        {
            updateEdges();
        }
        return edges.contains(e);
    }

    /**
     * Returns the edges that make up the polygon
     * @return
     */
    public Edge[] getEdges(){
        if(dirty)
        {
            updateEdges();
        }
        return edges.toArray(new Edge[edges.size()]);
    }

    
}
