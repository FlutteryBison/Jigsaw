package jig;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import jig.generation.cutter.Cutter;
import jig.generation.cutter.GrowCutter;
import jig.piece.GroupManager;
import jig.piece.Piece;
import jig.piece.Piece.PieceLook;


public class Jigsaw {

    private int numPieces;

    private int width,height;

    //Manages the pieces and how they are grouped together
    private GroupManager gm;

    //The number of turns that have past.
    //This is used for ordering of pieces.
    private int turnCounter;


    public Jigsaw(int width, int height, int numPieces)
    {
        this.width = width;
        this.height = height;
        this.numPieces = numPieces;


        Cutter cutter = new GrowCutter();
        Piece[] pieces = cutter.cut(height, width, numPieces);
        gm = new GroupManager(pieces);

        turnCounter = 0;
        
    }


    /**
     * Returns an array of all the jigsaw piece shapes in sorted order.
     * In a returned array of size n:
     *  The shape at index 0 is the   shape of the piece clicked least recently
     *  The shape at index n-1 is the shape of the piece clicked most recently
     * 
     * @return  The shapes of all the pieces in order of when they were last clicked
     */
    public PieceLook[] getSortedPieceLooks()
    {

        //Sort the Pieces into last pressed order.
        Piece[] sortedPieces = Arrays.copyOf(gm.getPieces(), numPieces);

        Comparator<Piece> piecePriority = Comparator.comparing(Piece::getLastPressed);
        Arrays.sort(sortedPieces, piecePriority);
        
        //Polygon[] polys = new Polygon[numPieces];
        PieceLook[] shapes = new PieceLook[numPieces];


        for(int i=0; i<numPieces; i++)
        {
            shapes[i] = sortedPieces[i].getPieceLook();
        }
        

        return shapes;
    }

    public int getNumPieces(){
        return numPieces;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }


    /**
     * Returns the uppermost piece at the given location.
     * If two or more pieces occupy the same space, The ID returned is of the piece that has been most recently clicked.
     * @param point // The point to return the top piece at.
     * @return      // The top piece ID at the location or -1 if no piece exists there
     */ 
    public int getTopPieceIDAt(Point2D point)
    {
        //Increment the turn counter to keep track of which pieces have been most recently clicked
        turnCounter++;

        Piece[] pieces = gm.getPieces();

        //Find all the pieces at the location
        
        ArrayList<Integer> IDs = new ArrayList<>();
        for (int i = 0; i< numPieces; i++) {
            if(pieces[i].getPieceLook().getShape().contains(point.getX(), point.getY()))
            {
                IDs.add(pieces[i].getID());
            }   
        }

        //Vars to store the ID of the piece that was most recently clicked
        int recentID =-1;
        int highestTurn = -1;

        //Find the ID of the piece that was most recently clicked
        for(int i = 0; i < IDs.size(); i++) {
            if (IDs.get(i) != null) {
                int lastPressed = pieces[IDs.get(i)].getLastPressed();
                if(lastPressed>highestTurn){
                    highestTurn = lastPressed;
                    recentID = IDs.get(i);
                }
            }
        }

        //Update the top piece last pressed value as it has just been pressed
        if(recentID!=-1)
        {
            pieces[recentID].setLastPressed(turnCounter);
        }
        return recentID;
    }

    
    public void movePiece(int id, Point2D.Double change)
    {
        gm.moveGroupContaining(id, change);
        
    }

    /**
     * Should be called whenever a piece is released
     */
    public void dropPiece(int pieceID)
    {
        gm.updateGroupContaining(pieceID);
    }






    
    
}
