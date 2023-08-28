package jig.piece;


import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;


/**
 * A class representing a piece
 */
public class Piece {

    /**
     * Class containing all the information needed to draw the shape
     */
    public class PieceLook{
        private Shape shape;
        private Color col;

        public PieceLook(Shape shape,Color col)
        {
            this.shape = shape;
            this.col = col;
        }

        public Shape getShape(){
            return shape;
        }
        public Color getCol()
        {
            return col;
        }
    }

    private int ID;
    
    private Path2D.Double baseShape;
    private Point2D.Double offset = new Point2D.Double(0, 0);
    private int lastPressed = 0;

    private Color col;

    //Stores the IDs of pieces that are directly adjacent to this one in the solved state.
    private int[] adjacancentPieces;

    

    /**
     * Creates a new piece with the given shape and a random color.
     * @param shape The shape of the piece.
     */
    public Piece(Path2D.Double shape, int ID, int[] adjacancies)
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
    public Piece(Path2D.Double shape, Color col,int ID)
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
     * Get the value of when this piece was last pressed
     * @return
     */
    public int getLastPressed(){
        return lastPressed;
    }

    /**
     * Sets the last pressed property if the new value is greater than the last.
     * Otherwise leaves it unaltered
     * @param lastPressed
     */
    public void setLastPressed(int lastPressed)
    {   if(lastPressed > this.lastPressed)
        {
            this.lastPressed = lastPressed;
    
        }
    }

    /**
     * Gets the piece shape positioned in its current position.
     * @return
     */
    public PieceLook getPieceLook() {
        return new PieceLook(baseShape.createTransformedShape(AffineTransform.getTranslateInstance(offset.x, offset.y) ), col);
    }

    

}
