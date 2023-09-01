package jig.piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



import java.awt.geom.Point2D;

public class GroupManager {
    
    //Maps each piece ID to its current group ID
    private HashMap<Integer,Integer> currentGroup = new HashMap<>();

    //maps the group id to an array list containing all the piece IDs in that group
    private HashMap<Integer,ArrayList<Integer>> group = new HashMap<>();

    //maps the piece IDs to the pieces
    private HashMap<Integer,Piece> pieces = new HashMap<>();

    //Id of the last group that was moved;
    private int lastMovedGroup = -1;

    /**
     * Constructs a new Group Manager with the pieces
     * @param pieces
     */
    public GroupManager(Piece[] pieces)
    {
        int groupID = 0;
        for (Piece piece : pieces) {
            this.pieces.put(piece.getID(), piece);
            currentGroup.put(groupID, piece.getID());
            group.put(groupID, new ArrayList<Integer>());
            group.get(groupID).add(piece.getID());
            groupID++;
        }
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

        //Store the group ID of the group being moved
        lastMovedGroup = currentGroup.get(id);

        //get ids of all pieces in the group that need to be moved
        ArrayList<Integer> IDs = group.get(lastMovedGroup);

        for (Integer ID : IDs) {
            Point2D.Double c2 = (Point2D.Double)change.clone();
            pieces.get(ID).addOffset(c2);
            System.out.println("moved piece:" + ID +" by x:" + change.x +" y:" +change.y);
        }
        System.out.println("end\n\n");

    }

    /**
     *  Check if the offsets of the last moved group are equal with any others and need to be merged
     *  Merged groups will be moved together on future calls to movePiece 
     */
    public void updateGroups()
    {
        //get the offset of the group
        //All offsets in the group are equal
        int groupID = group.get(lastMovedGroup).get(0);
        Point2D.Double offset = pieces.get(groupID).getOffset();

        
        Iterator<Map.Entry<Integer,ArrayList<Integer>>> it = group.entrySet().iterator();

        while(it.hasNext())
        {
            Map.Entry<Integer,ArrayList<Integer>> next = it.next();
            Integer compareGroupID = next.getKey();
            if(compareGroupID!=lastMovedGroup){
                int comparePieceID = group.get(compareGroupID).get(0);
                Point2D.Double compareOffset = pieces.get(comparePieceID).getOffset();
                double dist = offset.distance(compareOffset);

                if(dist<0.2){
                    //groups are very close.
                    //merge them together into one
                    mergeGroups(groupID, compareGroupID);
                    it.remove();
                    
                }

            }
        }
        

        


    }

    //Doesnt remove group ID2 from group hash map to avoid issues when iterating through map
    /**
     * Merges group with ID ID2 into group with ID ID1.
     * The group with ID2 will still need to be removed from group hashmap.
     * 
     * @param ID1  //ID of group to be merged into
     * @param ID2  //ID of group to merge
     */
    private void mergeGroups(int ID1, int ID2)
    {
        //change offsets of close group pieces to exactly match this one
        //and change the mapping of all pieces that have changed groups from the old one to the new one
        Point2D.Double newOffset = (Point2D.Double)pieces.get(ID1).getOffset();
        ArrayList<Integer> g2IDs = group.get(ID2);

        for (Integer pieceID : g2IDs) {
            pieces.get(pieceID).setOffset((Point2D.Double)newOffset.clone());

            
            currentGroup.put(pieceID,ID1);
        }

        //merge the array list of the second group into the first
        ArrayList<Integer> g1IDs = group.get(ID1);

        g1IDs.addAll(g2IDs);

        

    }
    
    
    /**
     * Returns all the pieces in the jigsaw
     * @return
     */
    public Piece[] getPieces()
    {
        return pieces.values().toArray(new Piece[pieces.size()]);
    }
}
