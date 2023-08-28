package jig.generation.cutter;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import directions.RandomDirection;
import jig.consoledisplay.ConsoleGridDisp;
import jig.piece.Piece;


public class GrowCutter implements Cutter{

    
    private int[][] grid;

    //The grid cells that are not currently part of any piece.
    private ArrayList<Point> unasigned;

    //Each jigsaw piece has an array list of grid cells that it currently contains
    private ArrayList<Point>[] pieceCells;

    //Stores the pieces each piece is adjacent to.
    private ArrayList<Integer>[] adjacancies;

    private int numPieces;

    

    public GrowCutter(){
        
    }

    

    @Override
    public Piece[] cut(int height, int width, int numPieces) {

        this.numPieces = numPieces;

        //Make sure there is not more pieces than grid cells
        if(numPieces >= width*height)
        {
            System.out.println("To many pieces for this size of grid");
            return null;
        }

        //
        Set<Point> startPoints = new HashSet<Point>();
        Random rand = new Random();

        //Randomise start positions
        do
        {
            startPoints.add(new Point(rand.nextInt(width),rand.nextInt(height)));
        }while(startPoints.size()<numPieces);

        Point[] arr = new Point[startPoints.size()];

        return cut(height,width,startPoints.toArray(arr));


    }

    
    public Piece[] cut(int height, int width, Point[] pieceStarts) {
        int numPieces = pieceStarts.length;

        //initialise vars
        unasigned = new ArrayList<Point>();

        grid = new int[width][height];
        //initialise grid to -1 to denote unasinged
        for(int i=0; i<width;i++){
            for(int j=0; j<height; j++){
                grid[i][j] = -1;
                unasigned.add(new Point(i, j));
            }
        }

        
        pieceCells = new ArrayList[numPieces];

        for(int i=0; i<numPieces; i++)
        {
            //Create the array list for the piece
            pieceCells[i] = new ArrayList<Point>();

            //Add the starting cell of the piece
            pieceCells[i].add(pieceStarts[i]);

            //Update the grid to reflect the new piece.
            grid[pieceStarts[i].x][pieceStarts[i].y] = i;

            //remove the point from the unasigned array
            unasigned.remove(pieceStarts[i]);
        }

        
        grow();
        
        adjacancies = calculateAdjecency();

        Piece[] pieces = new Piece[pieceStarts.length];

        //convert the grid cell visualisation to a piece using GridPiece
        for(int i = 0; i<pieceCells.length; i++)
        {
            pieces[i] = convertToPiece(pieceCells[i], i);
        }


        return pieces;


    }

    private void grow()
    {
        int curPiece = 0;
        while(unasigned.size()>0)
        {
            growPiece(curPiece, 1);

            //Increment cur piece and reset to 0 after the last piece.
            curPiece++;
            curPiece %= pieceCells.length;
        }
    }


    /**
     * 
     * @param pieceIndex
     * @param growthSize
     */
    private void growPiece(int pieceIndex,int growthSize)
    {
        Random rand = new Random();
        RandomDirection rd = new RandomDirection(RandomDirection.PERM_RANDOM);

        for(int growIndex = 0; growIndex<growthSize; growIndex++){
            

            int curSize = pieceCells[pieceIndex].size();
            Point growFrom = pieceCells[pieceIndex].get(rand.nextInt(curSize));
            

            //Attempt to grow from the random point in each direction
            boolean validGrowth = false;
            Point newPoint = new Point();
            
            for(int i=0; i<4 && !validGrowth; i++)
            {
                newPoint = new Point(growFrom);
                Point dir = rd.getNextRandomDirection();
                newPoint.translate(dir.x,dir.y);
                if(isUnasigned(newPoint)){
                    grid[newPoint.x][newPoint.y] = pieceIndex;
                    pieceCells[pieceIndex].add(newPoint);
                    unasigned.remove(newPoint);
                    validGrowth = true;
                }
            }
        }
        //System.out.println("\n");
        //ConsoleGridDisp.dispGrid(grid);
        
    }

    private boolean isUnasigned(Point p)
    {
        //Check the point is in the grid
        if(p.x < 0 || p.x >=grid.length)
        {
            return false;
        }

        if(p.y < 0 || p.y >=grid[0].length)
        {
            return false;
        }

        return grid[p.x][p.y] == -1;
    }



    private ArrayList<Integer>[] calculateAdjecency()
    {

        ArrayList<Integer>[] adjs = new ArrayList[numPieces];
        //initialise lists
        for (int i = 0; i < adjs.length; i++) {
            adjs[i] = new ArrayList<Integer>();
        }

        //for each cell store the values of all cells it borders unless they are the same as itself or have already been stored
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {


                for(int compareX=-1; compareX<2 && x+compareX>=0 && x-compareX<grid.length-1; compareX +=2)
                {
                    int cellVal = grid[x][y];
                    int compareVal = grid[x+compareX][y];
                    if(compareVal != cellVal){
                        if(!adjs[cellVal].contains(compareVal)){
                            adjs[cellVal].add(compareVal);
                        }
                    } 
                }

                for(int compareY = -1; compareY<2 && y+compareY>=0 && y-compareY < grid[0].length-1; compareY +=2)
                {
                    int cellVal = grid[x][y];
                    int compareVal = grid[x][y+compareY];
                    if(compareVal != cellVal){
                        if(!adjs[cellVal].contains(compareVal)){
                            adjs[cellVal].add(compareVal);
                        }
                    } 
                }


            }
        }

        return adjs;
    }




    /**
     * Convert this gridPiece, that is a piece made up of grid cells into a piece.
     * The piece stores the coordinates of the corners of the polygon that make up this piece.
     * a grid cell is assumed to be 1 unit wide.
     * 
     * For example, a piece made of of grid cells (1,2),(2,2),(3,2)
     * That is, a 1x3 rectangle will return a Piece with polygon coordinates of (1,2)(3,2)(4,2)(1,3)
     * @return The piece that represents this GridPiece.
     * @param ID The ID of the new piece
     */
    public Piece convertToPiece(ArrayList<Point> cells, int ID)
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
        for (Point point : cells) {

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

        //convert array list of Integer to int[]
        int[] adjs = new int[adjacancies[ID].size()];
        for (int i = 0; i < adjacancies[ID].size(); i++) {
            adjs[i] = adjacancies[ID].get(i);
        }
        return new Piece(shape,ID,adjs);
        
        
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
