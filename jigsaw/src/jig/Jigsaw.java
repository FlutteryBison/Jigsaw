package jig;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import jig.Piece.Piece;


public class Jigsaw {

    private int numPieces;

    private int width,height;

    //The pieces that make up the jigsaw.
    //A pieces index is its ID.
    private Piece[] pieces;


    public Jigsaw(int width, int height, int numPieces)
    {
        this.width = width;
        this.height = height;
        this.numPieces = numPieces;


        Cutter cutter = new GrowCutter();
        pieces = cutter.cut(height, width, numPieces);
        


    }


    //TODO dont work with corners
    public Shape[] getPieceShapes()
    {
        
        Polygon[] polys = new Polygon[pieces.length];
        Path2D.Double[] paths = new Path2D.Double[numPieces];


        for(int i=0; i<numPieces; i++)
        {

            Point2D.Double offset = pieces[i].getOffset();

            Point2D.Double[] corners = pieces[i].getPieceCorners();
            int[] xpoints = new int[corners.length];
            int[] ypoints = new int[corners.length];

            paths[i] = new Path2D.Double();
            paths[i].moveTo(corners[0].x + offset.x, corners[0].y + offset.y);

            for(int j = 0; j<corners.length; j++)
            {
                xpoints[j] = (int)Math.round(corners[j].x + offset.x);
                ypoints[j] = (int)Math.round(corners[j].y + offset.y);

                paths[i].lineTo(corners[j].x + offset.x, corners[j].y+offset.y);

                
            }
            polys[i] = new Polygon(xpoints,ypoints,xpoints.length);
            paths[i].closePath();

        }
        //return polys;
        return paths;
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


    public int[] getPieceIDsAt(Point2D point)
    {
        Shape[] shapes = getPieceShapes();
        ArrayList<Integer> IDs = new ArrayList<>();
        for (int i = 0; i< numPieces; i++) {
            if(shapes[i].contains(point.getX(), point.getY()))
            {
                IDs.add(i);
            }   
        }
        int[] arr = new int[IDs.size()];

        for(int i = 0; i < IDs.size(); i++) {
            if (IDs.get(i) != null) {
                arr[i] = IDs.get(i);
            }
        }
        return arr;
    }

    public void movePiece(int id, Point2D.Double change)
    {
        if(id< pieces.length && id >= 0)
        {
            pieces[id].addOffset(change);
        }
        
    }






    
    
}
