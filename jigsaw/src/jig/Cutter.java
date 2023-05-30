package jig;

import jig.Piece.Piece;

public interface Cutter {
    public Piece[] cut(int height, int width, int numPieces);
}
