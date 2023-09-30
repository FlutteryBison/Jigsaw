package jig;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Random;

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

    


    public Jigsaw(int width, int height, int numPieces)
    {
        this.width = width;
        this.height = height;
        this.numPieces = numPieces;


        Cutter cutter = new GrowCutter();
        Piece[] pieces = cutter.cut(height, width, numPieces);
        shuffle(pieces);
        gm = new GroupManager(pieces,width,height);
        
    }

    /**
     * Puts each piece in a random location.
     * 
     * @param pieces //The pieces to be shuffled.
     */
    private void shuffle(Piece[] pieces)
    {
        Random rand = new Random(System.currentTimeMillis());
        for (Piece piece : pieces) {

            //Bounding box for the piece
            Rectangle2D bounds = piece.getPieceLook().getShape().getBounds2D();

            //The random location to move the piece to
            Point2D.Double randLoc;

            //maximum x and y locations that that the piece can go at and be fully onscreen
            //these coordinates refer to the top left corner of the bounding box
            // the minimum x y coordinate is is always (0,0) as that would place the upper left corner of the bounding box in the upper left corner of the screen
            double maxX = width  - bounds.getWidth();
            double maxY = height - bounds.getHeight();
            

            //Choose a random location for the upper left corner where the bounding box is entirely withing the puzzle width
            randLoc = new Point2D.Double(rand.nextDouble()*maxX, rand.nextDouble()*maxY);

            //Pieces work with offsets from their original position
            //Calculate the offset the will move the piece to the desired location
            Point2D.Double offset = new Point2D.Double(randLoc.x - bounds.getMinX(), randLoc.y - bounds.getMinY());
            piece.setOffset(offset);
            
        }
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
        PieceLook[] shapes = Arrays.copyOf(gm.getSortedPieceLooks(), numPieces);
 
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
        PieceLook[] pieces = gm.getSortedPieceLooks();
        for (PieceLook pieceLook : pieces) {
            if(pieceLook.getShape().contains(point))
            {
                return pieceLook.getID();
            }
        }
        return -1;

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
