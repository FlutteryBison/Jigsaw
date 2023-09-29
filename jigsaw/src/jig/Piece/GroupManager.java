package jig.piece;

import java.util.ArrayList;
import java.util.Iterator;

import jig.piece.Piece.PieceLook;

import java.awt.geom.Point2D;

/**
 * A class for managing the groups.
 * manages the movement of groups
 * manages the merging of groups when they are placed next to correct pieces
 */
public class GroupManager {

    //How close adjacent pieces have to be to be considered merge their groups together
    private final double touchingThreshold;

    //groups of pieces that have been connected and should be moved as one.
    //This list is ordered by which piece was most recently moved.
    private ArrayList<Group> groups = new ArrayList<>();
    
    //TODO put pieces in the same group if they are close enough
    /**
     * Constructs a new Group Manager with the pieces
     * @param pieces
     */
    public GroupManager(Piece[] pieces, int width, int height)
    {
        //For now just put each piece in its own group at the start

        for (Piece piece : pieces) {
            Group g = new Group();
            g.addPiece(piece);
            groups.add(g);
        }

        touchingThreshold = (width+height)/100.0;
    }


    /**
     * Moves the piece with the given ID and all pieces that are connected to it.
     * @param id        ID of the piece to move
     * @param change    The dx and dy to move the piece by. e.g.
     *                      if the piece is currently at (x,y)
     *                      change is (dx,dy)
     *                      the new position will be (x+dx,y+dy)
     */
    public void moveGroupContaining(int id, Point2D.Double change){
        Group group = getGroupContaining(id);
        
        if(group!=null)
        {
            //maintain correct oredering of the list.
            //As the group was the most recently moved, if it is not at  index 0, move it to index 0
            if(groups.indexOf(group)!=0){
                groups.remove(group);
                groups.add(0, group);
            }
            group.moveGroup(change);
        }
        

    }


    /**
     * Check if the offsets of the last moved group are equal with any others and need to be merged
     *  Merged groups will be moved together on future calls to movePiece 
     * @param pieceID
     */
    public void updateGroupContaining(int pieceID)
    {
        Group g = getGroupContaining(pieceID);

        if(g == null)
        {
            return;
        }
        
        //get the offset of the group
        //All offsets in the group are equal
        Point2D.Double offset = g.getOffset();

        //get the pieces that are directly adjecent to this piece
        //We only check for the offsets to be equal in pieces that are adjacent.
        //Because we dont want to merge groups unless they are touching even if the offsets are equal
        Integer[] adjPieces = g.getGroupAdjacancentPieces();

        Iterator<Group> it = groups.iterator();

        while(it.hasNext())
        {
            Group next = it.next();

            //check it isnt the current group & the offsets are equat
            if(next != g)
            {
                //check if the groups meet are close enough to be merged
                double dist = offset.distance(next.getOffset());
                if(dist<touchingThreshold)
                {
                    //Check if the groups are adjecant to eahcother
                    if(next.containsAny(adjPieces))
                    {
                        //If they are close enough merge them into a single group and remove the old one.
                        g.mergeGroup(next);
                        it.remove();
                    }
                }
            }

        }


    }

  
    
    
    /**
     * Returns all the pieces in the jigsaw sorted by which was moved most recently.
     * ie. piece at index 0 is the most recently moved piece and piece at size-1 is the least recently moved piece
     * @return  all pieces sorted by last moved.
     */
    public PieceLook[] getSortedPieceLooks()
    {
        ArrayList<Piece> allPieces = new ArrayList<>();
        for (Group group : groups) {
            
            allPieces.addAll(group.getPieces().values());
        }
        PieceLook[] pieceLooks = new PieceLook[allPieces.size()];
        
        for (int i = 0; i < allPieces.size(); i++) {
            pieceLooks[i] = allPieces.get(i).getPieceLook();
        }

        return pieceLooks;
    }


    /**
     * Returns thee group that contains the piece with the given ID
     * @param pieceID  //The ID to find the group containing
     * @return         //The group that contains the piece, null if not groups contain a piece with that ID.
     */
    private Group getGroupContaining(int pieceID)
    {
        for (Group group : groups) {
            if(group.contains(pieceID)){
                return group;
            }
        }

        return null;
    }
}
