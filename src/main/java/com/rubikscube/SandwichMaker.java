package com.rubikscube;
// as always: this project was dedicated to my fellow AGT Lara, whom I wanna make Sandwiches with
import java.util.HashMap;
import java.util.Map;

public class SandwichMaker {
    public static final long solvedDominoState = 0b0000000000000000010101010000000000000000L;
    //first rotation of each corner (0,1,2), then rotation (0 good, 1 bad) and group (0 top/bottom, 1 equator) of each edge
    //public static Set<Long> statesFromDomino = new HashSet<>();
    public static Map<Long, Integer> statesFromDomino = new HashMap<>();
    public record Node(long edges, long corners) {}
    //static Set<Node> statesFromSolved = new HashSet<>();
    static Map<Node, Integer> statesFromSolved = new HashMap<>();
    static int depthFromDomino = 6;
    static int depthFromSolved = 8;
    static int countFromSolved = 0;
    static int countFromDomino = 0;


    public static void init() {
        searchStatesFromDomino(MoveHandler.solvedEdgeState, MoveHandler.solvedCornerState, -1, 0);
        System.out.println("States before Domino: " + countFromDomino);
        searchStatesFromSolved(MoveHandler.solvedEdgeState, MoveHandler.solvedCornerState, -1, 0);
        System.out.println("States before Solved: " + countFromSolved);
    }

    public static void searchStatesFromSolved(long edgeState, long cornerState, int previousMove, int depth) {
        if (!statesFromSolved.containsKey(new Node(edgeState, cornerState)) || statesFromSolved.get(new Node(edgeState, cornerState)) > depth) {   
            statesFromSolved.put(new Node(edgeState, cornerState), depth);
        }
        countFromSolved++;
        if (depth == depthFromSolved) return;
        for (int i = 0; i < 18; i++) {
            if (i > 11 || i%3==0) { // only domino moves
                if (depth == 0 || !(i%6==previousMove%6 || (i%3==previousMove%3 && i%6>previousMove%6))) {
                    searchStatesFromSolved(MoveHandler.moveEdges(edgeState, i), MoveHandler.moveCorners(cornerState, i), i, depth+1);
                }
            }
        }
    }

    public static void searchStatesFromDomino(long edgeState, long cornerState, int previousMove, int depth) {
        if (!statesFromDomino.containsKey(getDominoRelevant(edgeState, cornerState)) || statesFromDomino.get(getDominoRelevant(edgeState, cornerState)) > depth) {   
            statesFromDomino.put(getDominoRelevant(edgeState, cornerState), depth);
        }
        countFromDomino++;
        if (depth == depthFromDomino) return;
        for (int i = 0; i < 18; i++) {
            if (depth == 0 || !(i%6==previousMove%6 || (i%3==previousMove%3 && i%6>previousMove%6))) {
                searchStatesFromDomino(MoveHandler.moveEdges(edgeState, i), MoveHandler.moveCorners(cornerState, i), i, depth+1);
            }
        }
    }


    public static long getDominoRelevant(long edgeState, long cornerState) { // takes only rotation of corners, rotation of edges and group of edges
        long dominoState = 0L;

        for (int i = 0; i < 12; i++) {
            dominoState |= (((edgeState>>5*i&MoveHandler.edgePieceMask)<8?0L:1L)|(edgeState>>5*i&MoveHandler.edgeFlipMask)>>3)<<2*i;
        }
        for (int i = 0; i < 8; i++) {
            dominoState |= (cornerState>>5*i&MoveHandler.cornerTwistMask)<<(21+2*i);
        }
        
        return dominoState;
    }
}
// if in doubt, stroke one out