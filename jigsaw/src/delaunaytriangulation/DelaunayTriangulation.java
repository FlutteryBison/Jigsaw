package delaunaytriangulation;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import jig.generation.cutter.graphcutting.PointsConnector;
import shape.Circle;
import shape.EdgePolygon;
import shape.Triangle;

/**
 * Class that completes delaney triangulation on a set of points
 */
public class DelaunayTriangulation implements PointsConnector{

    //The triangles that make up the Delauney diagram store as the more generic EdgePolygon.
    private EdgePolygon[] tris;

    //The Edges that make up the delauney diagram
    private Edge[] edges;
    
    private ArrayList<Point> points = new ArrayList<>();
    private Rectangle boundingbox;

    private boolean triangulation_complete = false;


    /**
     * Completes a Delaney triangulation for the given set of points.
     * The bounding rectangle must contain all points in points
     * @param points        //The points to complete the triangulation with. 
     * @param boundingBox   //A rectangle that contains all the points.
     */
    public DelaunayTriangulation(Point[] points, Rectangle boundingBox){
        for (Point p : points) {
            this.points.add(p);
        }
        this.boundingbox = boundingBox;
    }

    /**
     * Completes a Delaney triangulation for the given set of points.
     * @param points //The points to complete the triangulation with.
     */
    public DelaunayTriangulation(Point[] points){
        for (Point p : points) {
            this.points.add(p);
        }
        this.boundingbox = getBoundingBox(points);
    }
    public DelaunayTriangulation()
    {
        //may mean bounding box is much larger as will start at 0,0, but doesnt affect performance.
        this.boundingbox = new Rectangle();
    }

    /**
     * Returns a rectangle that contains all the points in points
     * If points is empty, returns a rectangle at (0,0) with width and height of 0.
     * @param points //The points the rectangle must contain.
     * @return       //A rectangle that contains all the points
     */
    private Rectangle getBoundingBox(Point[] points){
        if(points.length ==0)
        {
            return new Rectangle();
        }
        Rectangle boundingBox = new Rectangle(points[0]);
        for (Point point : points) {
            boundingBox.add(point);
        }

        return boundingBox;
    }


    @Override
    public void connectPoints() {
        //Only complete the triangulation if it has not already been done with the current set of points
        if(!triangulation_complete)
        {
            triangulation(points.toArray(new Point[points.size()]), boundingbox);
            triangulation_complete = true;
        }
    }

    /**
     * Uses the Bowyer-Watson algorithm to complete the triangulation
     * @param points //The points to be triangulated.
     * @param d      //a rectangle that contains all the points in points.
     */
    private void triangulation(Point[] points, Rectangle d)
    {
        //super tiriangle contains all points in points
        Triangle superTri = Triangle.createSuperTriangle(d);

        //Array of triangles valid delaney triangles
        ArrayList<Triangle> tris = new ArrayList<>();
        tris.add(superTri);

        //iteratively add verticves to the delaney diagram and recompute the triangles
        for (Point p : points) {
            addVertex(p, tris);
        }

        //All points have been added.
        //remove supertriangle from triangulation
        Iterator<Triangle> it = tris.iterator();

    

        Point[] superEdges = superTri.getPoints();

        while(it.hasNext())
        {
            Triangle next = it.next();
            if(next.hasVertex(superEdges[0]) || next.hasVertex(superEdges[1]) || next.hasVertex(superEdges[2]))
            {
                it.remove();
            }


        
        }

        //store triangles
        this.tris = new EdgePolygon[tris.size()];
        for(int i=0; i<tris.size(); i++)
        {
            this.tris[i] = new EdgePolygon(tris.get(i).getPoints());
        }

        initEdges();
        triangulation_complete = true;

    }

    /**
     * Adds a vertex to the Delauney diagram and recomputes the valid triangles in tris.
     * 
     * @param vertex    //The vertex to add to the diagram.
     * @param tris      //The current triangles that make up the delauney diagram.
     */
    private void addVertex(Point vertex, ArrayList<Triangle> tris)
    {
        //tris.add(new Triangle(vertex, vertex, vertex));
        ArrayList<Triangle> badTriangles = new ArrayList<>();
        
        //find triangles with a circumcircle that contains the points.
        //These invalid and need to be recomputed
        for (Triangle tri : tris) {
            Point[] points = tri.getPoints();
            if(Circle.contains(points[0], points[1], points[2], vertex))
            {
                badTriangles.add(tri);
            }

        }

        //find the outside edge of the polygon made up by bad triangles
        ArrayList<Edge> outerPoly = getOutsidePolygon(badTriangles);

        //remove bad triangles from triangulation
        tris.removeAll(badTriangles);

        //Retriangulate pologonal hole of outer poly, create triangles from all outer edges to new point
        for (Edge edge : outerPoly) {
            tris.add(new Triangle(edge.p1, edge.p2, vertex));
        }



    }

    /**
     * Calculates the outside edge of connected triangles. Connected triangles must share an edge.
     * In other words, returns all edges that exist in only one triangle.
     * @param tris //The connected triangles to get the outside polygon of.
     * @return     //A list of edges that make up the polygon. No garuntess to the order of the edges.
     */
    private ArrayList<Edge> getOutsidePolygon(ArrayList<Triangle> tris)
    {
        ArrayList<Edge> outerPoly = new ArrayList<>();
        for (Triangle tri : tris) {
            Edge[] edges = tri.getEdges();
            for(int i=0; i<3; i++)
            {
                //an edge can only be shared by two tringles
                //an edge will be added to the outer poly then removed on its second sighting.
                int idx = outerPoly.indexOf(edges[i]);
                if(idx == -1)
                {
                    outerPoly.add(edges[i]);
                }
                else{
                    outerPoly.remove(idx);
                }
            }
            
        }

        return outerPoly;
    }



    //Stores all the edges from triangles in edges, edges are only stored once.
    private void initEdges()
    {
        ArrayList<Edge> tempEdges = new ArrayList<>();
        for (EdgePolygon tri : tris) {
            Edge[] e = tri.getEdges();
            for(int i =0; i<3; i++)
            {
                if(!tempEdges.contains(e[i]))
                {

                    tempEdges.add(e[i]);
                }
            }
        }

        edges = tempEdges.toArray(new Edge[tempEdges.size()]);
    }


    

    public EdgePolygon[] getTriangles()
    {
        return tris;
    }

    

    

    @Override
    public void addPoint(Point point) {
        points.add(point);
        boundingbox.add(point);
        triangulation_complete = false;
    }

    @Override
    public void addPoints(Point[] points) {
        for (Point point : points) {
            this.points.add(point);
            boundingbox.add(point);
        }
        triangulation_complete = false;
    }

    @Override
    public EdgePolygon[] getShapes() {
        if(tris ==null)
        {
            return new EdgePolygon[0];
        }
        return tris;
    }
    @Override
    public Edge[] getEdges()
    {
        if(edges == null)
        {
            return new Edge[0];
        }
        return edges;
    }

    @Override
    public void setPoints(Point[] points) {
        this.points.clear();
        for (Point point : points) {
            this.points.add(point);
        }
        triangulation_complete = false;
    }
    
}
