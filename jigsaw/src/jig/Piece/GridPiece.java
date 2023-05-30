package jig.Piece;

import java.awt.Point;
import java.util.ArrayList;

/**
 * A class representing a piece made up of grid cells
 */
public class GridPiece{

    //The grid cells in the piece
    private ArrayList<Point> cells = new ArrayList<>();

    public GridPiece(){

    }
    
    public GridPiece(Point startCell){
        cells.add(startCell);
    }

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
    

}
