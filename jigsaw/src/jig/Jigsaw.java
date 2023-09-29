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
        Rectangle2D screenRect = new Rectangle(width, height);
        Random rand = new Random(System.currentTimeMillis());
        for (Piece piece : pieces) {

            Rectangle2D transBounds;
            Point2D.Double randLoc;
            Rectangle2D bounds;

            do{
                randLoc = new Point2D.Double(rand.nextDouble()*width, rand.nextDouble()*height);

                bounds = piece.getPieceLook().getShape().getBounds2D();
                double xDist = bounds.getCenterX() - randLoc.x;
                double yDist = bounds.getCenterY() - randLoc.y;
                transBounds = new Rectangle2D.Double(bounds.getMinX() - xDist, bounds.getMinY() - yDist, bounds.getWidth(), bounds.getHeight());
                
                //Check the translating bounding box is within the window

            }while(!screenRect.contains(transBounds) && !screenRect.intersects(transBounds));

            Point2D.Double cent = new Point2D.Double(bounds.getCenterX(),bounds.getCenterY());
            Point2D.Double offset = new Point2D.Double(randLoc.x - cent.x, randLoc.y - cent.y);
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
