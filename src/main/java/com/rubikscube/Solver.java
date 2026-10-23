package com.rubikscube;
// distance to Domino/solved does not work for states, that are already in the sandwich maps, doesnt see the better ones in those sets whyever idfk
// you stupid piece of shit. Who wrote the method to create the sandwich maps?? the ones 6 steps down in the branch wrote the input and the next ones 1 step down couldn't overwrite it :)
public class Solver {
    static int searchDepth = 0;
    static final int maxStartSearchDepth = 6;
    static final int maxDominoSearchDepth = 7;
    static final int maxMoveCount = 35;
    static long count = 0;
    static boolean preDominoReached = false, dominoReached = false, preSolvedReached = false;

    public static void startSolve() {
        long startTime = (long) System.currentTimeMillis();
        searchMoves(MoveHandler.inputEdgeState, MoveHandler.inputCornerState, true);
        long endTime = (long) System.currentTimeMillis();
        System.out.println("Time: " + (endTime-startTime));
        System.out.println("Positions Searched: " + count);
    }

    // 346964164791502198, 388231617956
    // 113158841966512330, 672616632327
    // F' U F R U' R2 B F' L U' R D B R2 B2 U2 R2 U L B'

    /*public static void checkPreMaps() {
        long dominoState = SandwichMaker.getDominoRelevant(MoveHandler.inputEdgeState, MoveHandler.inputCornerState);
        //System.out.println(dominoState);
        SandwichMaker.Node solvedState = new SandwichMaker.Node(MoveHandler.inputEdgeState, MoveHandler.inputCornerState);
        //System.out.println(solvedState);
        if (SandwichMaker.statesFromDomino.containsKey(dominoState)) {
            System.out.println("Distance to Domino: " + SandwichMaker.statesFromDomino.get(dominoState));
        }
        if (SandwichMaker.statesFromSolved.containsKey(solvedState)) {
            System.out.println("Distance to Solved: " + SandwichMaker.statesFromSolved.get(solvedState));
        }
    }*/

    public static int searchMoves(long edgeState, long cornerState, boolean doLogs) {
        int[] moveSequence = new int[maxMoveCount];
        for (int i = 0; i < maxMoveCount; i++) {
            moveSequence[i] = -1;
        }
        long currentEdgeState = edgeState;
        long currentCornerState = cornerState;
        preDominoReached = false;
        dominoReached = false;
        preSolvedReached = false;
        searchDepth = maxStartSearchDepth;
        int previousMove = -1;
        int previousDistance = 20;
        for (int i = 0; i < maxMoveCount; i++) {
            int[] results = nextMove(currentEdgeState, currentCornerState, previousMove, previousDistance, searchDepth);
            //System.out.println(" - " + currentEdgeState + ", " + currentCornerState);
            moveSequence[i] = results[0];
            previousDistance = results[1];
            currentEdgeState = MoveHandler.moveEdges(currentEdgeState, moveSequence[i]);
            currentCornerState = MoveHandler.moveCorners(currentCornerState, moveSequence[i]);
            previousMove = moveSequence[i];

            if (doLogs && !preDominoReached && SandwichMaker.statesFromDomino.containsKey(SandwichMaker.getDominoRelevant(currentEdgeState, currentCornerState))) {
                preDominoReached = true;
                System.out.println("Predomino at " + (i+1));
            }
            if (!dominoReached && isDominoReduced(currentEdgeState, currentCornerState)) {
                dominoReached = true;
                searchDepth=maxDominoSearchDepth;
                previousDistance = 20;
                if (doLogs) System.out.println("Domino at " + (i+1));
            }
            if (doLogs && !preSolvedReached && SandwichMaker.statesFromSolved.containsKey(new SandwichMaker.Node(currentEdgeState, currentCornerState))) {
                System.out.println("Presolution at " + (i+1));
                preSolvedReached = true;
            }
            if (isSolved(currentEdgeState, currentCornerState)) {
                if (doLogs) {
                    System.out.print("Lösung: ");
                    MoveHandler.printMoveSequence(moveSequence);
                    GraphicsHandler.printSolutionToOutput(moveSequence);
                }
                return PerformanceTest.getLength(moveSequence);
            }
        }
        //if (doLogs) {
            System.out.println("Failed: " + MoveHandler.inputEdgeState + " " + MoveHandler.inputCornerState);
            //MoveHandler.printMoveSequence(moveSequence);
            //GraphicsHandler.visualizeState(currentEdgeState, currentCornerState);
        //}
        return -1; // DNF
    }

    public static int[] nextMove(long edgeState, long cornerState, int previousMove, int previousDistanceToGoal, int maxSearchDepth) { // tries all 18 moves for which has the best potential in the known path
        int[] bestEval = {0,20};
        int bestNextMove[] = {0,20};
        int maxJ = 0;
        
        //long startTime = (long) System.currentTimeMillis();
        for (int j = 1; j <= maxSearchDepth; j++) {
            for (int i = 0; i < 18; i++) {
                if (!dominoReached || (i > 11 || i%3==0)) { // if domino state is reached, only allow domino moves
                    if (j == 1 || !(previousMove != -1 && (i%6==previousMove%6 || (i%3==previousMove%3 && i%6>previousMove%6)))) {
                        int[] newEval = searchStates(MoveHandler.moveEdges(edgeState, i), MoveHandler.moveCorners(cornerState, i), i, 1, j);
                        if (newEval[1] < bestEval[1] || (newEval[1] == bestEval[1] && newEval[0] > bestEval[0])) {
                            bestEval = newEval;
                            bestNextMove[0] = i;
                            bestNextMove[1] = bestEval[1];
                        }
                    }
                }
            }
            if (j > maxJ) maxJ = j;

            if (bestEval[1] < previousDistanceToGoal) {
                break;
            }
        }

        /*System.out.print(maxJ + "; " + bestNextMove[0] + "; " + bestEval[1] + "; " + bestEval[0]);
        long endTime = (long) System.currentTimeMillis();
        System.out.print("  Time: " + (endTime-startTime));*/
        
        return bestNextMove;
    }

    public static int[] searchStates(long edgeState, long cornerState, int previousMove, int depth, int maxSearchDepth) { // tries from this state all 18 next states
        count++;
        int[] eval = evaluateState(edgeState, cornerState, depth);
        if (depth == maxSearchDepth) return eval;
        for (int i = 0; i < 18; i++) {
            if (!dominoReached || (i > 11 || i%3==0)) { // allow only halfturns + U/D if domino is reached
                if (!(i%6==previousMove%6 || (i%3==previousMove%3 && i%6>previousMove%6))) { // not moving the same face twice OR count U D and D U
                    int[] newEval = searchStates(MoveHandler.moveEdges(edgeState, i), MoveHandler.moveCorners(cornerState, i), i, depth+1, maxSearchDepth);
                    if (newEval[1] < eval[1] || (newEval[1] == eval[1] && newEval[0] > eval[0])) {
                        eval = newEval;
                    }
                }
            }
        }
        return eval;
    }

    public static int[] evaluateState(long edgeState, long cornerState, int depth) { // points for pieces on the right position, pieces oriented correctly, bonus points for a piece fulfilling both
        int goodEdges=0, goodCorners=0, distance = 20;
        int[] out = {0,20};
        
        if (!dominoReached) {
            for (int i = 0; i < 12; i++) {
                if ((edgeState>>5*i&MoveHandler.edgeFlipMask) == 0 && ((edgeState>>5*i&MoveHandler.edgePieceMask) < 8 && i < 8) || ((edgeState>>5*i&MoveHandler.edgePieceMask) > 7 && i > 7)) {
                    goodEdges++;
                }
            }
            for (int i = 0; i < 8; i++) {
                if ((cornerState>>5*i&MoveHandler.cornerTwistMask) == 0) {
                    goodCorners++;
                }
            }
            if (SandwichMaker.statesFromDomino.containsKey(SandwichMaker.getDominoRelevant(edgeState, cornerState))) {
                distance = SandwichMaker.statesFromDomino.get(SandwichMaker.getDominoRelevant(edgeState, cornerState)) + depth;
            }
            out[0] = 6*goodEdges + 6*goodCorners;
            out[1] = distance;
        } else { // domino reached
            for (int i = 0; i < 12; i++) {
                if ((edgeState>>5*i&MoveHandler.edgePieceMask) == i && i < 8) {
                    goodEdges++;
                }
            }
            for (int i = 0; i < 8; i++) {
                if ((cornerState>>5*i&MoveHandler.cornerPieceMask) == i) {
                    goodCorners++;
                }
            }
            if (SandwichMaker.statesFromSolved.containsKey(new SandwichMaker.Node(edgeState, cornerState))) {
                distance = SandwichMaker.statesFromSolved.get(new SandwichMaker.Node(edgeState, cornerState)) + depth;
            }
            out[0] = 6*goodEdges + 6*goodCorners;// - 10*depth;
            out[1] = distance;
        }
        return out;
        /* 
        
        if (!dominoReached && !dominoInSight) {
            int edgesInCorrectGroup = 0, goodEdges = 0, correctButFlippedEdges = 0, allWrongEdges = 0, goodCorners = 0, inEndStadium = 0;
            for (int i = 0; i < 12; i++) {
                if ((edgeState>>5*i&MoveHandler.edgeFlipMask) == 0 && ((edgeState>>5*i&MoveHandler.edgePieceMask) < 8 && i < 8) || ((edgeState>>5*i&MoveHandler.edgePieceMask) > 7 && i > 7)) {
                    goodEdges++;
                }
                /*boolean good = (edgeState>>5*i&MoveHandler.edgeFlipMask) == 0;
                boolean correctGroup = ((edgeState>>5*i&MoveHandler.edgePieceMask) < 8 && i < 8) || ((edgeState>>5*i&MoveHandler.edgePieceMask) > 7 && i > 7);
                if (good) {
                    goodEdges++;
                }
                if (correctGroup) {
                    edgesInCorrectGroup++;
                    if (!good) {
                        correctButFlippedEdges++;
                    }
                }
                if (!correctGroup && !good) {
                    allWrongEdges++;
                }
            }
            for (int i = 0; i < 8; i++) {
                if ((cornerState>>5*i&MoveHandler.cornerTwistMask) == 0) {
                    goodCorners++;
                }
            }
            if (SandwichMaker.statesFromDomino.contains(SandwichMaker.getDominoRelevant(edgeState, cornerState))) {
                inEndStadium+=10;
                goalSeen = true;
            }
            if (SandwichMaker.getDominoRelevant(edgeState, cornerState) == SandwichMaker.solvedDominoState) {
                inEndStadium += 100;
                dominoInSight = true;
            }

            //return 6*goodEdges + 4*edgesInCorrectGroup- 4*correctButFlippedEdges - 7*allWrongEdges + 5*goodCorners-4*depth+200*inEndStadium;
            return 3*goodEdges + 4*goodCorners-20*depth+200*inEndStadium;
        } else if (dominoInSight && !dominoReached) {
            int correctEdges = 0, correctCorners = 0, isDomino = 0;
            for (int i = 0; i < 12; i++) {
                if ((edgeState>>5*i&MoveHandler.edgePieceMask)/4 == i/4 && (edgeState>>5*i&MoveHandler.edgeFlipMask) == 0) {
                    correctEdges++;
                }
            }
            for (int i = 0; i < 8; i++) {
                if ((cornerState>>5*i&MoveHandler.cornerPieceMask)/4 == i/4 && (cornerState>>5*i&MoveHandler.cornerTwistMask) == 0) {
                    correctCorners++;
                }
            }
            if (SandwichMaker.getDominoRelevant(edgeState, cornerState) == SandwichMaker.solvedDominoState) {
                isDomino += 10;
                goalSeen = true;
            }
            return 20*correctEdges+20*correctCorners-5*depth+50*isDomino;
        } else if (dominoReached && !solutionInSight) {
            int correctEdges = 0, correctCorners = 0, inEndStadium = 0;
            for (int i = 0; i < 12; i++) {
                if ((edgeState>>5*i&MoveHandler.edgePieceMask) == i) {
                    correctEdges++;
                }
            }
            for (int i = 0; i < 8; i++) {
                if ((cornerState>>5*i&MoveHandler.cornerPieceMask) == i) {
                    correctCorners++;
                }
            }
            if (SandwichMaker.statesFromSolved.contains(new SandwichMaker.Node(edgeState, cornerState))) {
                if (!solutionInSight) {
                    goalSeen = true;
                    solutionInSight = true;
                }
                inEndStadium+=2;
            }
            if (isSolved(edgeState, cornerState)) {
                inEndStadium += 10;
            }
            return 6*correctEdges+6*correctCorners-12*depth+100*inEndStadium;
        } else {
            int solutionSeen = 0;
            if (isSolved(edgeState, cornerState)) {
                goalSeen = true;
                solutionSeen++;
            }
            return -depth+100*solutionSeen;
        }*/
    }

    public static boolean isSolved(long edgeState, long cornerState) {
        return edgeState == MoveHandler.solvedEdgeState && cornerState == MoveHandler.solvedCornerState;
    }

    public static boolean isDominoReduced(long edgeState, long cornerState) {
        for (int i = 0; i < 12; i++) {
            if ((edgeState>>5*i&MoveHandler.edgeFlipMask) != 0) {
                return false; // if an edge is not oriented
            }
            if (((edgeState>>5*i&MoveHandler.edgePieceMask) < 8 && i > 7) || ((edgeState>>5*i&MoveHandler.edgePieceMask) > 7 && i < 8)) {
                return false; // if an equator edge is on top, or the other way around
            }
        }
        for (int i = 0; i < 8; i++) {
            if ((cornerState>>5*i&MoveHandler.cornerTwistMask) != 0) {
                return false; // if a corner is not oriented
            }
        }
        return true;
    }
}