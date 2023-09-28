package jig.piece;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Iterator;

/**
 * A class representing a group of pieces.
 * All the pieces in the group can be moved as one.
 */
public class Group {

    //Maps the IDs of the pieces to the pieces in this group
    private HashMap<Integer,Piece> pieces;

    //Lists the IDs of all pieces adjacent to this group
    private ArrayList<Integer> groupAdjacancentPieces;

    /**
     * Creates a new group
     */
    public Group()
    {
        this.pieces = new HashMap<Integer,Piece>();
        this.groupAdjacancentPieces = new ArrayList<Integer>();
    }

    /**
     * Adds a piece to this group.
     * The 
     * @param piece
     */
    public void addPiece(Piece piece)
    {   
        pieces.put(piece.getID(), piece);
        int[] adjs = piece.getAdjacentPieces();

        for (int i : adjs) {
            if(!groupAdjacancentPieces.contains(i)){
                groupAdjacancentPieces.add(i);
            }
        }
        

    }

    /**
     * Merges group g into this one.
     * All the pieces in group g are added to this group.
     * All the Adjacency inforamtion is combined with this one.
     * The offsets of the pieces in g are set to equal the offsets of this group to align the pieces perfectly
     * @param g //The group to merge into this one.
     */
    public void mergeGroup(Group g)
    {
        //move the pieces in the old group to the same offset as this one
        Point2D.Double oldOffset = g.getOffset();
        Point2D.Double curOffset = getOffset();
        Point2D.Double mov = new Point2D.Double(curOffset.x - oldOffset.x, curOffset.y - oldOffset.y);
        g.moveGroup(mov);
        getOffset().distance(oldOffset);

        
        this.pieces.putAll(g.getPieces());

        Integer[] adjs = g.getGroupAdjacancentPieces();

        //Only store that a piece is adjacent to the group once
        for (Integer i : adjs) {
            if(!groupAdjacancentPieces.contains(i)){
                groupAdjacancentPieces.add(i);
            }
        }

        groupAdjacancentPieces.removeAll(pieces.keySet());
    }

    /**
     * @return pieceIDs mapped to the pieces
     */
    public HashMap<Integer,Piece> getPieces(){
        return pieces;
    }


    /**
     * @return Array of the IDs of pieces that are adjacent to this group
     */
    public Integer[] getGroupAdjacancentPieces()
    {
        return groupAdjacancentPieces.toArray(new Integer[groupAdjacancentPieces.size()]);
    }

    /**
     * Checks if the piece with the given ID is in this group.
     * @param pieceID //The ID of the piece to check for.
     * @return        //True if the piece is in the group, false otherwise.
     */
    public boolean contains(int pieceID)
    {
        return pieces.containsKey(pieceID);
    }

    /**
     * Checks if any of the pieceIDs are in this group.
     * @param pieceIDs  //An array of IDs to check for.
     * @return          //True if 1 or more pieces with IDs from the array are in the group, false otherwise.
     */
    public boolean containsAny(Integer[] pieceIDs)
    {
        for (Integer ID : pieceIDs) {
            if(pieces.keySet().contains(ID))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the piece with the given ID
     * @param pieceID
     * @return          //The piece with the given ID.
     */
    public Piece getPiece(int pieceID)
    {
        return pieces.get(pieceID);
    }


    /**
     * Moves every piece in the group by the amount specified
     * eg. 
     *  if a piece in the group is at (x,y)
     *  and change is (dx,dy)
     *  the new piece coordinate is (x+dx, y+dy)
     * @param change //The amount to move the group by.
     */
    public void moveGroup(Point2D.Double change)
    {
        for (Piece p: pieces.values()) {
            p.addOffset(change);
        }
    }

    //TODO manage the offset by group
    public Point2D.Double getOffset()
    {
       Iterator<Integer> i = pieces.keySet().iterator();
       if(i.hasNext()){
        return pieces.get(i.next()).getOffset();
       }

       return null;
    }


}
