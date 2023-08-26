package jig.generation.piecegeneration;

import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import jig.piece.Piece;

/**
 * A class representing a piece made up of grid cells
 */
public class GridPiece{

    //The grid cells in the piece
    private ArrayList<Point> cells = new ArrayList<>();

    /*
     * Constructs a new grid piece with no current cells.
     */
    public GridPiece(){

    }
    /**
     * Constructs a new Grid piece with a single cell.
     * @param cell
     */
    public GridPiece(Point cell){
        cells.add(cell);
    }

    /**
     * Constructs a new grid piece with the specified cells.
     * @param cells
     */
    public GridPiece(ArrayList<Point> cells)
    {
        this.cells = cells;
    }

    /**
     * Add a single grid cell to the piece
     * @param cell  // Coordinate of the grid cell to add
     * @return
     */
    public boolean addCell(Point cell)
    {
        if(cells.contains(cell)){
            return false;
        }
        cells.add(cell);
        return true;
    }

    /**
     * Get all the grid cells that make up this piece
     * @return
     */
    public ArrayList<Point> getCells()
    {
        return cells;
    }

     /**
     * Convert this gridPiece, that is a piece made up of grid cells into a piece.
     * The piece stores the coordinates of the corners of the polygon that make up this piece.
     * a grid cell is assumed to be 1 unit wide.
     * 
     * For example, a piece made of of grid cells (1,2),(2,2),(3,2)
     * That is, a 1x3 rectangle will return a Piece with polygon coordinates of (1,2)(3,2)(4,2)(1,3)
     * @return The piece that represents this GridPiece.
     */
    public Piece convertToPiece()
    {

        /*
        Convert to piece algorithm
    
        Grid Pieces store the locations on a grid of the cells that make up the shape.

        We want to convert to a Piece. That is have the coordinates of the corners on the outer edge so
        it may be reconstructed as a polygon. 

        Consider this shape made up of 4 grid cells

         --- --- --- ---
        |   |   |   |   |
         --- --- --- ---

        By storing the coordinates of the corners the shape can be reconstructed.

        The algorithm is as follows:
            1) Count the number of times each vertex appears in the shape. 
                Verticies that appear an odd number of times are the corners

            2) Count the number of times each edge appears in the shape
                Edges that appear once only are outside edges

            3) Any corner is chosen 

            4) an outside edge is followed untill another corner is reached

            step 4 is repeated untill we reach the initial corner

            this may need to be repeated if inner outside edges are present
        
        */

        //TODO refactor
    
        //Count the instances of each vertex and each edge
        HashMap<Point,Integer> vertexCount   = new HashMap<>();
        HashMap<Point,Integer> vertEdgeCount = new HashMap<>();
        HashMap<Point,Integer> horEdgeCount  = new HashMap<>();




        //loop through every cell that makes up the shape
        for (Point point : getCells()) {

            //for each corner of cell increment its count
            for(int xOffset = 0; xOffset<2; xOffset++)
            {
                for(int yOffset = 0; yOffset<2; yOffset++)
                {
                    Point vertex = point.getLocation();
                    vertex.translate(xOffset, yOffset);
                    Integer cur = vertexCount.get(vertex);
                    if(cur == null)
                    {
                        cur = 0;
                    }
                    vertexCount.put(vertex, ++cur);
                }
            }

            //increment the horizontal edge counts for both horizontal edges of the cell
            for(int yOffset = 0; yOffset<2; yOffset++)
            {
                Point vertex = point.getLocation();
                vertex.translate(0, yOffset);
                Integer cur = horEdgeCount.get(vertex);
                if(cur == null)
                {
                    cur = 0;
                }
                horEdgeCount.put(vertex, ++cur);
            }

            //increment the vertical edge counts for both vertical edges of the cell
            for(int xOffset = 0; xOffset<2; xOffset++)
            {
                Point vertex = point.getLocation();
                vertex.translate(xOffset, 0);
                Integer cur = vertEdgeCount.get(vertex);
                if(cur == null)
                {
                    cur = 0;
                }
                vertEdgeCount.put(vertex, ++cur);
            }
            
        }

        //Now we know how many times each vertex edge is in the shape
        //All the vertices that occur an odd number of times are a corner of the shape
        //All the edges that occur only once are an outer edges of the shape

        //choose a corner vertex to start from
        Point startVertex = null;
        for (Point p : vertexCount.keySet()) {
            if (vertexCount.get(p)%2 != 0) {
              startVertex = p;
              break;
            }
        }
        
        

        Point currentVertex = startVertex.getLocation();

        //variable to indicate which direction we have came from along an edge
        //This is used to not back track the way we came.
        Point dirFrom = new Point(0,0);

        //Array to store the potitions of the corners of the shape
        ArrayList<Point2D> corners = new ArrayList<>();

        do {
            
            //Initialise the vertex mover, This stores the direction we are moving along an outside edge
            Point vertexMover = new Point();
            

            //get an outer edge that extends from the corner to determine which direction the edge extends from the vertex

            //This checks if there is an outer edge from the vertex in the x direction
            Boolean edgeFound = false;
            for(int dir = -1; dir<1 &&!edgeFound; dir++)
            {
                Point p = currentVertex.getLocation();
                p.translate(dir, 0);

                //The vertex may return null if the shape does not extent that far
                if(horEdgeCount.get(p)!=null && horEdgeCount.get(p)==1){
                    //if-1 we want x direction to remain -1
                    //if 0 we want x direction to be 1
                    vertexMover = new Point(dir*2+1, 0);
                    
                    //If this is the direction we came from, keep looking
                    if(!vertexMover.equals(dirFrom))
                        edgeFound = true;
                }
            }

            //This checks if there is an outer edge from the vertex in the y direction
            for(int dir = -1; dir<1 &&!edgeFound; dir++)
            {
                Point p = currentVertex.getLocation();
                p.translate(0, dir);
                if(vertEdgeCount.get(p)!=null && vertEdgeCount.get(p)==1){
                    //if-1 we want y direction to remain -1
                    //if 0 we want y direction to be 1
                    vertexMover = new Point(0, dir*2+1);
                    if(!vertexMover.equals(dirFrom))
                        edgeFound = true;
                }
            }
            
            
            //move along edge untill next corner found
            do {
                currentVertex.translate(vertexMover.x, vertexMover.y);;
            } while (vertexCount.get(currentVertex) %2 == 0);

            //Store the direction we came from
            dirFrom.setLocation(vertexMover.x*-1, vertexMover.y*-1);


            //Add the coordinate of the corner to the return piece
            corners.add(new Point2D.Double(currentVertex.x, currentVertex.y));
 
        } while (!currentVertex.equals(startVertex));

        Path2D.Double shape = convertCornersToShape(corners.toArray(new Point2D.Double[corners.size()]));

        return new Piece(shape);
        
        
    }

    private  Path2D.Double convertCornersToShape(Point2D[] corners)
    {
        Path2D.Double path = new Path2D.Double();

        

        path.moveTo(corners[0].getX(), corners[0].getY());

        for(int i = 0; i<corners.length; i++)
        {
            path.lineTo(corners[i].getX(), corners[i].getY());
        }
        path.closePath();

        
        return path;
    }
    

}
