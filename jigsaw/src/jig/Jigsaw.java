package jig;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import jig.Piece.Piece;


public class Jigsaw {

    private int numPieces;

    private int width,height;

    private Piece[] pieces;


    public Jigsaw(int width, int height, int numPieces)
    {
        this.width = width;
        this.height = height;
        this.numPieces = numPieces;


        Cutter cutter = new GrowCutter();
        pieces = cutter.cut(height, width, numPieces);
        


    }


    public Polygon[] getPiecePolygons(int normalisedWidth, int normalisedHeight)
    {
        double wScale = normalisedWidth/width;
        double hScale = normalisedHeight/height;
        Polygon[] polys = new Polygon[pieces.length];


        for(int i=0; i<pieces.length; i++)
        {
            Point2D.Double[] corners = pieces[i].getPieceCorners();
            int[] xpoints = new int[corners.length];
            int[] ypoints = new int[corners.length];
            for(int j = 0; j<corners.length; j++)
            {
                xpoints[j] = (int)Math.round(corners[j].x*wScale);
                ypoints[j] = (int)Math.round(corners[j].y*hScale);
            }
            polys[i] = new Polygon(xpoints,ypoints,xpoints.length);

        }
        return polys;
    }







    
    
}
