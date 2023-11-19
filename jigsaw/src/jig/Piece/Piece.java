package jig.piece;


import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Random;


/**
 * A class representing a piece
 */
public class Piece {

    /**
     * Class containing all the information needed to draw the shape
     */
    public class PieceLook{
        private int ID;  //The ID of the shape piece it belongs to.
        private Path2D.Float shape;
        private Color col;

        public PieceLook(Path2D.Float shape,Color col, int ID)
        {
            this.shape = shape;
            this.col = col;
            this.ID = ID;
        }

        public Shape getShape(){
            return shape;
        }
        public Color getCol()
        {
            return col;
        }
        public int getID(){
            return ID;
        }
    }

    private int ID;
    
    private Shape baseShape;
    private Point2D.Double offset = new Point2D.Double(0, 0);

    private Color col;

    //Stores the IDs of pieces that are directly adjacent to this one in the solved state.
    private int[] adjacancentPieces;

    

    /**
     * Creates a new piece with the given shape and a random color.
     * @param shape The shape of the piece.
     */
    public Piece(Shape shape, int ID, int[] adjacancies)
    {
        this.baseShape = shape;
        Random rand = new Random();
        this.col = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        this.ID = ID;
        this.adjacancentPieces = adjacancies;
    }

    /**
     * Creates a new piece with the given shape and color.
     * @param shape The Shape of the piece.
     * @param col   The color of the piece
     */
    public Piece(Shape shape, Color col,int ID)
    {
        this.baseShape = shape;
        this.col = col;
        this.ID = ID;
    }
    

    
    /** 
     * Sets the new value of the offset for the piece.
     * @param offset // The new offset
     * 
     */
    
    public void setOffset(Point2D.Double offset)
    {
        this.offset = offset;
    }


  
    /**
     * Adds an x and y coord to the offset
     *   eg. 
     *          adding offset (dx,dy) to the current offset (x,y)
     *          Results in a new offset of (x+dx,y+dy)
     * @param change
     */
    public void addOffset(Point2D.Double change)
    {
        this.offset.setLocation(offset.x + change.x, offset.y+change.y);
    }
    
    public Point2D.Double getOffset()
    {
        return offset;
    }

    

    /**
     * Gets the piece shape positioned in its current position.
     * @return
     */
    public PieceLook getPieceLook() {
        return new PieceLook(new Path2D.Float(baseShape, AffineTransform.getTranslateInstance(offset.x, offset.y)), col, ID);
    }

    /**
     * Returns the ID of this piece
     * @return The ID of the piece
     */
    public int getID()
    {
        return ID;
    }

    public int[] getAdjacentPieces(){
        return adjacancentPieces;
    }


    

}
