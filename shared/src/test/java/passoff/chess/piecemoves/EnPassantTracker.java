package chess;

import chess.ChessPosition;

/**
 * Tracks the square that is eligible for en passant capture,
 * and which side just moved two squares.
 */
public class EnPassantTracker {
    // The position you can capture *into* via en passant (or null)
    public ChessPosition enPassantTarget = null;
    // Which color may capture en passant right now
    public ChessGame.TeamColor vulnerable = null;
}

//
