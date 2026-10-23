package com.rubikscube;

public class MoveHandler {
    /*
    edges:
    0 1000
    ^ ^Index of Edge, position in long is position in cube
    Orientation

    corners:
    00 100
    ^  ^Index of Corner
    Orientation
    */
    public static final long solvedEdgeState =   0b01011_01010_01001_01000_00111_00110_00101_00100_00011_00010_00001_00000L;
    // 11:R 10:T 9:J 8:L 7:X 6:W 5:V 4:U 3:D 2:C 1:B 0:A
    public static final long solvedCornerState = 0b00111_00110_00101_00100_00011_00010_00001_00000L;
    // 7:X 6:W 5:V 4:U 3:D 2:C 1:B 0:A

    public static long inputEdgeState = solvedEdgeState;
    public static long inputCornerState = solvedCornerState;
    
    public static final long edgePieceMask = 0b01111L;
    public static final long edgeFlipMask =  0b10000L;
    public static final long cornerPieceMask = 0b00111L;
    public static final long cornerTwistMask = 0b11000L;
    public static final long pieceMask = 0b11111L;

    public static void resetCube() {
        inputEdgeState = solvedEdgeState;
        inputCornerState = solvedCornerState;
        GraphicsHandler.visualizeState(inputEdgeState, inputCornerState);
    }

    public static void scrambleCube(boolean doLogs) {
        inputEdgeState = solvedEdgeState;
        inputCornerState = solvedCornerState;
        int[] moveSequence = new int[20];
        for (int i = 0; i < 20; i++) {
            int nextMove = MyFunctions.getRandomInt(0, 17);
            if (i != 0) {
                while (nextMove%6==moveSequence[i-1]%6 || (nextMove%3==moveSequence[i-1]%3 && nextMove%6>moveSequence[i-1]%6)) {    
                    nextMove = MyFunctions.getRandomInt(0, 17);
                }
            }
            moveSequence[i] = nextMove;
            inputEdgeState = moveEdges(inputEdgeState, nextMove);
            inputCornerState = moveCorners(inputCornerState, nextMove);
        }
        if (!doLogs) return;
        GraphicsHandler.visualizeState(inputEdgeState, inputCornerState);
        GraphicsHandler.printScrambleToInput(moveSequence);
        System.out.print("Scramble: ");
        printMoveSequence(moveSequence);
        //System.out.println(inputEdgeState + " " + inputCornerState);
    }

    public static void inputMove(int moveNr) {
        inputEdgeState = moveEdges(inputEdgeState, moveNr);
        inputCornerState = moveCorners(inputCornerState, moveNr);
        GraphicsHandler.visualizeState(inputEdgeState, inputCornerState);
    }

    public static long moveEdges(long edgeState, int moveNr) {
        long edgesChange = 0;
        long edgesMask = 0;
        for (int i = 0; i < 4; i++) {
            edgesChange |= (edgeState>>(5*edgeMoves[8*moveNr+2*i])&edgePieceMask | ((edgeState>>(5*edgeMoves[8*moveNr + 2*i])&edgeFlipMask) ^ edgeFlips[moveNr]<<4))<<(5*edgeMoves[8*moveNr+2*i+1]);
            edgesMask |= pieceMask<<5*edgeMoves[8*moveNr+2*i+1];
        }
        return (edgeState&~edgesMask)|edgesChange;
    }

    public static long moveCorners(long cornerState, int moveNr) {
        long cornersChange = 0;
        long cornersMask = 0;
        for (int i = 0; i < 4; i++) {
            cornersChange |= (cornerState>>(5*cornerMoves[8*moveNr+2*i])&cornerPieceMask | cornerTwists[3*moveNr+(int)((cornerState>>(5*cornerMoves[8*moveNr+2*i])&cornerTwistMask)>>3)]<<3)<<(5*cornerMoves[8*moveNr+2*i+1]);
            cornersMask |= pieceMask<<5*cornerMoves[8*moveNr+2*i+1];
        }
        return (cornerState&~cornersMask)|cornersChange;
    }

    public static void printMoveSequence(int[] moveSequence) {
        for (int i = 0; i < moveSequence.length; i++) {
            if (moveSequence[i] == -1) {
                System.out.print("  "+(i)+" Moves");
                break;
            } 
            System.out.print(moveLetters[moveSequence[i]] + " ");
        }
        System.out.println();
    }

    
    /*
    Moves: 0:U   1:D   2:R   3:L   4:F   5:B
           6:U'  7:D'  8:R'  9:L'  10:F' 11:B'
           12:U2 13:D2 14:R2 15:L2 16:F2 17:B2

    // For Primeturns add 6, for Halfturns add 12
    */
    static final String[] moveLetters = {"U", "R", "F", "D", "L", "B", "U'", "R'", "F'", "D'", "L'", "B'", "U2", "R2", "F2", "D2", "L2", "B2"};
    //{"U", "D", "R", "L", "F", "B", "U'", "D'", "R'", "L'", "F'", "B'", "U2", "D2", "R2", "L2", "F2", "B2"};
    //public static int[] dominoMoves = {0,3,6,9,12,13,14,15,16,17};

    // Edges =>   11:R 10:T 9:J 8:L 7:X 6:W 5:V 4:U 3:D 2:C 1:B 0:A
    // Corners => 7:X 6:W 5:V 4:U 3:D 2:C 1:B 0:A

    static final int[] edgeMoves = {                 
        0,1,   1,2,   2,3,   3,0,      // U
        1,10,  10,5,  5,9,   9,1,      // R
        2,9,   9,4,   4,8,   8,2,      // F
        4,5,   5,6,   6,7,   7,4,      // D
        3,8,   8,7,   7,11,  11,3,     // L
        0,11,  11,6,  6,10,  10,0,     // B

        0,3,   3,2,   2,1,   1,0,      // U'
        1,9,   9,5,   5,10,  10,1,     // R'
        2,8,   8,4,   4,9,   9,2,      // F'
        4,7,   7,6,   6,5,   5,4,      // D'
        3,11,  11,7,  7,8,   8,3,      // L'
        0,10,  10,6,  6,11,  11,0,     // B'

        0,2,   2,0,   1,3,   3,1,      // U2
        1,5,   5,1,   10,9,  9,10,     // R2
        2,4,   4,2,   9,8,   8,9,      // F2
        4,6,   6,4,   5,7,   7,5,      // D2
        3,7,   7,3,   8,11,  11,8,     // L2
        0,6,   6,0,   10,11, 11,10     // B2
    };

    /*        
        0,1,   1,2,   2,3,   3,0,      // U
        4,5,   5,6,   6,7,   7,4,      // D
        1,10,  10,5,  5,9,   9,1,      // R
        3,8,   8,7,   7,11,  11,3,     // L
        2,9,   9,4,   4,8,   8,2,      // F
        0,11,  11,6,  6,10,  10,0,     // B

        0,3,   3,2,   2,1,   1,0,      // U'
        4,7,   7,6,   6,5,   5,4,      // D'
        1,9,   9,5,   5,10,  10,1,     // R'
        3,11,  11,7,  7,8,   8,3,      // L'
        2,8,   8,4,   4,9,   9,2,      // F'
        0,10,  10,6,  6,11,  11,0,     // B'

        0,2,   2,0,   1,3,   3,1,      // U2
        4,6,   6,4,   5,7,   7,5,      // D2
        1,5,   5,1,   10,9,  9,10,     // R2
        3,7,   7,3,   8,11,  11,8,     // L2
        2,4,   4,2,   9,8,   8,9,      // F2
        0,6,   6,0,   10,11, 11,10     // B2 */
    /*
        0,1,   1,2,   2,3,   3,0,      // U 
        4,5,   5,6,   6,7,   7,4,      // D 
        2,1,   1,6,   6,5,   5,2,      // R 
        0,3,   3,4,   4,7,   7,0,      // L 
        3,2,   2,5,   5,4,   4,3,      // F 
        0,7,   7,6,   6,1,   1,0,      // B 

        0,3,   3,2,   2,1,   1,0,      // U'
        4,7,   7,6,   6,5,   5,4,      // D'
        2,5,   5,6,   6,1,   1,2,      // R'
        0,7,   7,4,   4,3,   3,0,      // L'
        3,4,   4,5,   5,2,   2,3,      // F'
        0,1,   1,6,   6,7,   7,0,      // B'

        0,2,   2,0,   1,3,   3,1,      // U2
        4,6,   6,4,   5,7,   7,5,      // D2
        2,6,   6,2,   1,5,   5,1,      // R2
        0,4,   4,0,   3,7,   7,3,      // L2
        3,5,   5,3,   2,4,   4,2,      // F2
        0,6,   6,0,   1,7,   7,1       // B2 */

    static final int[] cornerMoves = {     
        0,1,   1,2,   2,3,   3,0,      // U 
        2,1,   1,6,   6,5,   5,2,      // R 
        3,2,   2,5,   5,4,   4,3,      // F 
        4,5,   5,6,   6,7,   7,4,      // D 
        0,3,   3,4,   4,7,   7,0,      // L 
        0,7,   7,6,   6,1,   1,0,      // B 

        0,3,   3,2,   2,1,   1,0,      // U'
        2,5,   5,6,   6,1,   1,2,      // R'
        3,4,   4,5,   5,2,   2,3,      // F'
        4,7,   7,6,   6,5,   5,4,      // D'
        0,7,   7,4,   4,3,   3,0,      // L'
        0,1,   1,6,   6,7,   7,0,      // B'

        0,2,   2,0,   1,3,   3,1,      // U2
        2,6,   6,2,   1,5,   5,1,      // R2
        3,5,   5,3,   2,4,   4,2,      // F2
        4,6,   6,4,   5,7,   7,5,      // D2
        0,4,   4,0,   3,7,   7,3,      // L2
        0,6,   6,0,   1,7,   7,1       // B2
    };

    static final int[] edgeFlips = { // 0 good edge, 1 bad edge
        0,0,1,0,0,1, // only F and B flip
        0,0,1,0,0,1, // F' B'
        0,0,0,0,0,0  // half turns don't flip
    };

    static final int[] cornerTwists = { // 0 white/yellow facing up/down, 1 facing front/back, 2 facing sides
        0, 2, 1, // U
        1, 0, 2, // R
        2, 1, 0, // F
        0, 2, 1, // D
        1, 0, 2, // L
        2, 1, 0, // B
        0, 2, 1, // U'
        1, 0, 2, // R'
        2, 1, 0, // F'
        0, 2, 1, // D'
        1, 0, 2, // L'
        2, 1, 0, // B'
        0, 1, 2, // Halfturns
        0, 1, 2,
        0, 1, 2,
        0, 1, 2,
        0, 1, 2,
        0, 1, 2
    };
}
