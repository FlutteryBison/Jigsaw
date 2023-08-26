package jig.piece;


import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;


/**
 * A class representing a piece
 */
public class Piece {

    private Path2D.Double shape;
    private Point2D.Double offset = new Point2D.Double(0, 0);
    private int lastPressed = 0;

    public Piece(Path2D.Double shape)
    {
        this.shape = shape;
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
    public Shape getPieceShape() {
        return shape.createTransformedShape(AffineTransform.getTranslateInstance(offset.x, offset.y) );
    }

}
