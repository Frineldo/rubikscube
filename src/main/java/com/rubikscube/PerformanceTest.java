package com.rubikscube;

public class PerformanceTest {
    static int testSize = 1000;

    public static void testPerformance() {
        int moves[] = new int[testSize];
        int successfulSolves = 0;
        int times[] = new int[testSize];

        for (int i = 0; i < testSize; i++) {
            if (i%(testSize/10) == 0) System.out.println(i);
            MoveHandler.scrambleCube(false);
            long startTime = (long) System.currentTimeMillis();
            int moveCount = Solver.searchMoves(MoveHandler.inputEdgeState, MoveHandler.inputCornerState, false);
            long endTime = (long) System.currentTimeMillis();
            times[i] = (int)(endTime-startTime);
            if (moveCount != -1) {
                moves[i] = moveCount;
                successfulSolves++;
            }
            if (times[i] > 500 || moves[i] == -1) {
                System.out.println(MoveHandler.inputEdgeState + " " + MoveHandler.inputCornerState);
            }
        }

        System.out.println(successfulSolves);
        analyzeMoves(moves, times, successfulSolves);

        //System.out.println("Durchschnittlich " + ((float)moveCount/(float)successfulSolves) + " Züge in " + ((float)time/(float)successfulSolves) + " Millisekunden.");
        //System.out.println(successfulSolves + " erfolgreiche Solves aus " + testSize + " Versuchen.");
    }

    public static void analyzeMoves(int[] moves, int[] times, int successfulSolves) {
        for (int i = getMin(moves); i <= getMax(moves); i++) {
            System.out.println(i + " Moves: " + countObjectsInArray(i, moves) + "/" + countObjectsInArray(i, moves)/(float)successfulSolves);
        }
        System.out.println("Mean Solves: " + meanArray(moves));
        for (int i = 0; i <= getMax(times); i+=20) {
            System.out.println(i + "-" + (i+20) + " ms: " + countBoundsInArray(i, i+20, times) + "/" + countBoundsInArray(i, i+20, times)/(float)successfulSolves);
        }
        System.out.println("Mean Time: " + meanArray(times));
    }

    public static int countObjectsInArray(int object, int[] array) {
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == object) count++;
        }
        return count;
    }

    public static int countBoundsInArray(int lowerBound, int upperBound, int[] array) {
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (lowerBound <= array[i] && array[i] < upperBound) count++;
        }
        return count;
    }

    public static int sumArray(int[] array) {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }

    public static float meanArray(int[] array) {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum/(float)array.length;
    }

    public static int getMax(int[] array) {
        int max = array[0];
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) max = array[i];
        }
        return max;
    }

    public static int getMin(int[] array) {
        int min = array[0];
        for (int i = 0; i < array.length; i++) {
            if (array[i] < min) min = array[i];
        }
        return min;
    }

    public static int getLength(int[] moves) {
        for (int i = 0; i < moves.length; i++) {
            if (moves[i] == -1) return i;
        }
        return moves.length;
    }
}

/*
    659429922862016867 18960865636
    126950208665380039 309614488269
    384329584948757139 260220077448
    673887074119685944 626705144457
    290485877876139162 741196914187
    277087651035698021 760251927893
*/

/*
10000 Tries
9987 Successes
0 Moves: 13/0.0013016922 <- Fails
1 Moves: 0/0.0
2 Moves: 0/0.0
3 Moves: 0/0.0
4 Moves: 0/0.0
5 Moves: 0/0.0
6 Moves: 0/0.0
7 Moves: 0/0.0
8 Moves: 0/0.0
9 Moves: 0/0.0
10 Moves: 0/0.0
11 Moves: 0/0.0
12 Moves: 0/0.0
13 Moves: 0/0.0
14 Moves: 0/0.0
15 Moves: 0/0.0
16 Moves: 0/0.0
17 Moves: 5/5.0065084E-4
18 Moves: 18/0.0018023431
19 Moves: 58/0.0058075497
20 Moves: 219/0.021928508
21 Moves: 759/0.0759988
22 Moves: 1909/0.19114849
23 Moves: 2825/0.28286773
24 Moves: 2304/0.23069991
25 Moves: 815/0.08160609
26 Moves: 476/0.04766196
27 Moves: 254/0.025433064
28 Moves: 144/0.0144187445
29 Moves: 87/0.008711325
30 Moves: 52/0.005206769
31 Moves: 26/0.0026033844
32 Moves: 15/0.0015019525
33 Moves: 6/6.00781E-4
34 Moves: 11/0.0011014319
Mean Solves: 23.3821
0-20 ms: 3848/0.3853009
20-40 ms: 1096/0.109742664
40-60 ms: 2586/0.2589366
60-80 ms: 735/0.07359567
80-100 ms: 485/0.048563134
100-120 ms: 360/0.036046863
120-140 ms: 234/0.02343046
140-160 ms: 191/0.019124862
160-180 ms: 151/0.015119656
180-200 ms: 108/0.010814059
200-220 ms: 58/0.0058075497
220-240 ms: 43/0.0043055974
240-260 ms: 33/0.0033042955
260-280 ms: 16/0.0016020827
280-300 ms: 14/0.0014018223
300-320 ms: 7/7.0091116E-4
320-340 ms: 10/0.0010013017
340-360 ms: 5/5.0065084E-4
360-380 ms: 4/4.005207E-4
380-400 ms: 3/3.003905E-4
400-420 ms: 2/2.0026034E-4
420-440 ms: 2/2.0026034E-4
440-460 ms: 0/0.0
460-480 ms: 1/1.0013017E-4
480-500 ms: 0/0.0
500-520 ms: 0/0.0
520-540 ms: 0/0.0
540-560 ms: 0/0.0
560-580 ms: 0/0.0
580-600 ms: 0/0.0
600-620 ms: 1/1.0013017E-4
620-640 ms: 0/0.0
640-660 ms: 0/0.0
660-680 ms: 1/1.0013017E-4
680-700 ms: 4/4.005207E-4
700-720 ms: 0/0.0
720-740 ms: 0/0.0
740-760 ms: 0/0.0
760-780 ms: 0/0.0
780-800 ms: 0/0.0
800-820 ms: 1/1.0013017E-4
820-840 ms: 0/0.0
840-860 ms: 0/0.0
860-880 ms: 0/0.0
880-900 ms: 1/1.0013017E-4
Mean Time: 49.2949
*/