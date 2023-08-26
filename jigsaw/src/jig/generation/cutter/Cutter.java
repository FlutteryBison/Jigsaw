package jig.generation.cutter;

import jig.piece.Piece;

public interface Cutter {
    public Piece[] cut(int height, int width, int numPieces);
}
