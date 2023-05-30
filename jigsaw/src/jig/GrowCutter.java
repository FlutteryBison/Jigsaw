package jig;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import jig.Piece.GridPiece;
import jig.Piece.Piece;
import jig.consoleDisplay.ConsoleGridDisp;

import java.util.Collections;

public class GrowCutter implements Cutter{

    
    private int[][] grid;

    //The grid cells that are not currently part of any piece.
    private ArrayList<Point> unasigned;

    //Each jigsaw piece has an array list of grid cells that it currently contains
    private ArrayList<Point>[] pieceCells;

    //Direction can be added to a point to move in one of the 4 caridinal directions
    private Point[] directions;
    //Perm is used to index into direction.
    //It contains indecies into directions and can be shuffled before use to give a random direction.
    private ArrayList<Integer> perm;

    public GrowCutter(){
        directions = new Point[4];
        directions[0] = new Point(0, -1);
        directions[1] = new Point(1, 0);
        directions[2] = new Point(0, 1);
        directions[3] = new Point(-1, 0);

        perm = new ArrayList<>();
        for(int i=0; i<4; i++)
        {
            perm.add(i);
        }
    }

    

    @Override
    public Piece[] cut(int height, int width, int numPieces) {

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

        Piece[] pieces = new Piece[pieceStarts.length];

        //convert the grid cell visualisation to a piece using GridPiece
        for(int i = 0; i<pieceCells.length; i++)
        {
            pieces[i] = Piece.convertToPiece(new GridPiece(pieceCells[i]));
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

        for(int growIndex = 0; growIndex<growthSize; growIndex++){
            

            int curSize = pieceCells[pieceIndex].size();
            Point growFrom = pieceCells[pieceIndex].get(rand.nextInt(curSize));
            Collections.shuffle(perm);

            //Attempt to grow from the random point in each direction
            boolean validGrowth = false;
            Point newPoint = new Point();
            
            for(int i=0; i<4 && !validGrowth; i++)
            {
                newPoint = new Point(growFrom);
                newPoint.translate(directions[perm.get(i)].x, directions[perm.get(i)].y);
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
    
}
