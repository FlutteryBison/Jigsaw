package jig.generation.cutter.graphcutting;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import delaunaytriangulation.Edge;
import jig.generation.cutter.Cutter;
import jig.piece.Piece;
import shape.EdgePolygon;

public class GraphCutter implements Cutter{
    private PointsConnector pc;

    /**
     * Constructs a new Graph cutter using the Points connector as a way to connect the points in the graph.
     * @param pc The points cutter to use. It should already have all the points to connect
     */
    public GraphCutter(PointsConnector pc)
    {
        this.pc = pc;
    }
    /**
     * Constructs a new graph cutter and uses the points provided. Any points currently the Points connector will be removed.
     * @param pc   The points connector to use.
     * @param points The points to use.
     */
    public GraphCutter(PointsConnector pc, Point[] points)
    {
        pc.setPoints(points);
        this.pc = pc;
    }

    @Override
    public Piece[] cut(int height, int width, int numPieces) {
        pc.connectPoints();
        EdgePolygon[] shapes = pc.getShapes();
         
        Piece[] pieces = new Piece[shapes.length];


        //TODO improve speed
        //very quickly implimented for testing.

        //compute adjacencies
        for (int i = 0; i < pieces.length; i++) {
            ArrayList<Integer> adjs = new ArrayList<>();
            for (int j = 0; j < pieces.length; j++) {
                if(j==i)
                {
                    continue;
                }
                Edge[] edges = shapes[i].getEdges();
                for (Edge edge : edges) {
                    if(shapes[j].hasEdge(edge))
                    {
                        adjs.add(j);
                        break;
                    }

                }
            }

            //convert arraylist to int array
            int[] adjsArr = new int[adjs.size()];
            for(int c=0;c<adjs.size(); c++)
            {
                adjsArr[c] = adjs.get(c);
            }
            pieces[i] = new Piece(
                            shapes[i], 
                            i,
                            adjsArr
            );
        }


        return pieces;
        

    }


    /**
     * Create an array of random points within the bounding box given.
     * @param numPoints //Number of random points to create.
     * @param bounds    //The bounding box that all points will exist within.
     * @return          //An array of random points within the box bounds.
     */
    public static Point[] generateRandomPoints(int numPoints, Rectangle bounds)
    {
        return generateRandomPoints(numPoints, bounds, System.currentTimeMillis());
    }


    /**
     * Create an array of random points within the bounding box given.
     * @param numPoints //Number of random points to create.
     * @param bounds    //The bounding box that all random points will exist within.
     * @param seed      //Seed.
     * @return          //Array of random points within bounds.
     */
    public static Point[] generateRandomPoints(int numPoints, Rectangle bounds,long seed)
    {
        Random rand = new Random(seed);
        Point[] points = new Point[numPoints];

        int randXBound = (int)(bounds.getMaxX()-bounds.getMinX());
        int randYBound = (int)(bounds.getMaxY()-bounds.getMinY());

        for(int i=0;i<points.length;i++)
        {
            points[i] = new Point(
                (int)(rand.nextInt(randXBound)+bounds.getMinX()),
                (int)(rand.nextInt(randYBound)+bounds.getMinY())
            );
        }

        return points;
    }


    /**
     * Generates points in a grid pattern. in the area specified by bounds.
     * The first row and collumn of points will align with the left and top edge respectivly of the bounds rectangle.
     * The last row and collumn of points will align with the right and bottom edge respectively of bounds.
     * 
     * Random radius sets a radius around the true grid position that the point may fall in.
     * For example a grid point centred at (2,2) with random radius of 2 will fall within
     *  the circle with centre(2,2) and radius 2 so long as it is within bounds.
     * 
     * 
     * @param rows          //The number of rows in the grid.
     * @param cols          //The number of collumns in the grid.
     * @param bounds        //The boundaries of the grid.
     * @param randomRadius  //The distance from each actuall grid point the generated points can be.
     * @return              //rows*cols number of points.
     */
    public static Point[] generateGridPoints(int rows, int cols ,Rectangle bounds, int randomRadius)
    {
        return generateGridPoints(rows, cols, bounds, randomRadius, System.currentTimeMillis());
    }

    /**
     * Generates points in a grid pattern. in the area specified by bounds.
     * The first row and collumn of points will align with the left and top edge respectivly of the bounds rectangle.
     * The last row and collumn of points will align with the right and bottom edge respectively of bounds.
     * 
     * Random radius sets a radius around the true grid position that the point may fall in.
     * For example a grid point centred at (2,2) with random radius of 2 will fall within
     *  the circle with centre(2,2) and radius 2 so long as it is within bounds.
     * 
     * 
     * @param rows          //The number of rows in the grid.
     * @param cols          //The number of collumns in the grid.
     * @param bounds        //The boundaries of the grid.
     * @param randomRadius  //The distance from each actuall grid point the generated points can be.
     * @param seed          //A seed for the randomness.
     * @return              //rows*cols number of points.
     */
    public static Point[] generateGridPoints(int rows, int cols ,Rectangle bounds, int randomRadius, long seed)
    {

        Point[] points = new Point[rows*cols];

        
        double xGap = (cols>1) ? bounds.getWidth()/(cols-1):0;
        double yGap = (rows>1) ? bounds.getHeight()/(rows-1):0;
        

        Random rand = new Random(seed);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {

                double xBase = bounds.getMinX() + xGap*x,
                       yBase = bounds.getMinY() + yGap*y;

                long pos; //for intermediate result

                double xOffset, yOffset;
                do{
                    xOffset = rand.nextFloat() * randomRadius;
                    //Rectangle.Contains returns false for points on the edge.
                    //Check manually
                    pos = Math.round(xBase+xOffset);
                }while(bounds.getMinX() > pos && bounds.getMaxX() < pos);

                do{
                    yOffset = rand.nextFloat() * randomRadius;
                    pos = Math.round(yBase+yOffset);
                }while(bounds.getMinY() > pos && bounds.getMaxY() < pos);


                points[y*cols+x] = new Point(
                    (int)Math.round(xBase+xOffset),
                    (int)Math.round(yBase+yOffset)     
                );
            }
            
        }


        return points;
    }
    
    
}
