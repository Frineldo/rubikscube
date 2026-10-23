package com.rubikscube;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

public class GraphicsHandler {
    static int buttonSize = 50;

    static JFrame frame;
    static JPanel panel;
    static JButton[] colorButtons = new JButton[54];
    /* Solve Button used for scrambling. Doesn't solve anything currently */
    static JButton solveButton, scrambleButton, saveButton, resetButton, inputButton;
    static JTextField inputField;
    static JLabel outputLabel;
    static ActionListener colorButtonListener, actionButtonListener;

    //public GraphicsHandler() {
    public static void init() {
        frame = new JFrame();
        frame.setTitle("Rubiks Cube Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        //frame.setLayout(null);
        frame.setFocusable(true);
        frame.setVisible(true);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.GRAY);
        panel.setBounds(0,0,1200,700);
        panel.setFocusable(true);

        frame.add(panel);

        colorButtonListener = e -> {
            int index = Integer.parseInt(((JButton) e.getSource()).getName());
            changeButtonColor(index);
        };

        actionButtonListener = e -> {
            switch ((((JButton)e.getSource()).getName())) {
                case ("solve") -> Solver.startSolve();
                case ("scramble") -> MoveHandler.scrambleCube(true);
                case ("save") -> readCubeFromStickers();
                case ("reset") -> MoveHandler.resetCube();
                case ("input") -> inputScramble();
            }
        };

        initButtons();
        initKeyBindings();
        panel.revalidate();
        panel.repaint();
    }

    static final int[] xPositions = {1,0,1,2,3,1};
    static final int[] yPositions = {0,1,1,1,1,2};
    static final Color w = Color.WHITE, o = Color.ORANGE, g = Color.GREEN, r = Color.RED, b = Color.BLUE, y = Color.YELLOW;
    static final Color[] colors = {w, o, g, r, b, y};

    static void initButtons() {
        for (int i = 0; i < 54; i++) { // create all stickers
            colorButtons[i] = new JButton();
            colorButtons[i].setBounds(100+buttonSize*(3*xPositions[i/9] + i%3), 100+buttonSize*(3*yPositions[i/9] + (i%9)/3), 50, 50);
            colorButtons[i].setName(""+i);
            colorButtons[i].setBackground(colors[i/9]);
            colorButtons[i].setBorder(new LineBorder(Color.BLACK));
            colorButtons[i].setOpaque(true);
            colorButtons[i].setVisible(true);
            colorButtons[i].addActionListener(colorButtonListener);

            panel.add(colorButtons[i]);
        }

        solveButton = new JButton();
        solveButton.setVisible(true);
        solveButton.setBounds(250, 575, 150, 50);
        solveButton.setName("solve");
        solveButton.setText("Lösen");
        solveButton.addActionListener(actionButtonListener);
        scrambleButton = new JButton();
        scrambleButton.setVisible(true);
        scrambleButton.setBounds(450, 575, 150, 50);
        scrambleButton.setName("scramble");
        scrambleButton.setText("Verdrehen");
        scrambleButton.addActionListener(actionButtonListener);
        saveButton = new JButton();
        saveButton.setVisible(true);
        saveButton.setBounds(450, 500, 150, 50);
        saveButton.setName("save");
        saveButton.setText("Zustand speichern");
        saveButton.addActionListener(actionButtonListener);
        resetButton = new JButton();
        resetButton.setVisible(true);
        resetButton.setBounds(450, 425, 150, 50);
        resetButton.setName("reset");
        resetButton.setText("Zurücksetzen");
        resetButton.addActionListener(actionButtonListener);
        inputButton = new JButton();
        inputButton.setVisible(true);
        inputButton.setBounds(650, 500, 150, 50);
        inputButton.setName("input");
        inputButton.setText("Eingeben");
        inputButton.addActionListener(actionButtonListener);

        outputLabel = new JLabel();
        outputLabel.setVisible(true);
        outputLabel.setOpaque(true);
        outputLabel.setBounds(650, 575, 450, 50);
        outputLabel.setName("output");

        inputField = new JTextField();
        inputField.setVisible(true);
        inputField.setOpaque(true);
        inputField.setBounds(650, 425, 450, 50);
        inputField.setName("inputField");

        panel.add(solveButton);
        panel.add(scrambleButton);
        panel.add(saveButton);
        panel.add(resetButton);
        panel.add(inputButton);
        panel.add(outputLabel);
        panel.add(inputField);
    }

    static void initKeyBindings() {
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("U"), 0);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("R"), 1);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F"), 2);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"), 3);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("L"), 4);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("B"), 5);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.SHIFT_DOWN_MASK), 6);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.SHIFT_DOWN_MASK), 7);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.SHIFT_DOWN_MASK), 8);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.SHIFT_DOWN_MASK), 9);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.SHIFT_DOWN_MASK), 10);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.SHIFT_DOWN_MASK), 11);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK), 12);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK), 13);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), 14);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK), 15);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK), 16);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK), 17);
        /*
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("U"), 0);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"), 1);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("R"), 2);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("L"), 3);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F"), 4);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("B"), 5);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK), 6);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK), 7);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK), 8);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK), 9);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), 10);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK), 11);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.ALT_DOWN_MASK), 12);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_DOWN_MASK), 13);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK), 14);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_DOWN_MASK), 15);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.ALT_DOWN_MASK), 16);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.ALT_DOWN_MASK), 17); */
        for (int i = 0; i < 18; i++) {
            panel.getActionMap().put(i, new KeyDown(i));
        }
    }

    static void changeButtonColor(int index) {
        if ((index == 4) || (index == 13) || (index == 22) || (index == 31) || (index == 40) || (index == 49)) return; // dont change a centers color
        int i = MyFunctions.indexOf(colorButtons[index].getBackground(), colors);
        colorButtons[index].setBackground(colors[(i+1)%6]);
    }

    // first good face, then bad face
    static final Color[] edgeColors = { w,b,  w,r,  w,g,  w,o,  y,g,   y,r,   y,b,   y,o,   g,o,   g,r,   b,r,   b,o};
    static final int[] edgeStickers = { 1,37, 5,28, 7,19, 3,10, 46,25, 50,34, 52,43, 48,16, 21,14, 23,30, 39,32, 41,12};
    // first top/bottom face, then front/back face, then side face
    static final Color[] cornerColors = { w,o,b,  w,r,b,   w,r,g,   w,o,g,   y,o,g,    y,r,g,    y,r,b,    y,o,b};
    static final int[] cornerStickers = { 0,9,38, 2,29,36, 8,27,20, 6,11,18, 45,17,24, 47,33,26, 53,35,42, 51,15,44};

    public static void visualizeState(long edgeState, long cornerState) {

        for (int i = 0; i < 12; i++) {
            long flip = (edgeState>>5*i & MoveHandler.edgeFlipMask)>>4;
            long piece = (edgeState>>5*i & MoveHandler.edgePieceMask);
            colorButtons[edgeStickers[2*i]].setBackground(edgeColors[(int)(2*piece+flip)]);
            colorButtons[edgeStickers[2*i+1]].setBackground(edgeColors[(int)(2*piece+1-flip)]);
        }
        for (int i = 0; i < 8; i++) {
            long twist = (cornerState>>5*i & MoveHandler.cornerTwistMask)>>3;
            long piece = (cornerState>>5*i & MoveHandler.cornerPieceMask);
            boolean slotOtherDirection = (piece%2-i%2)!=0; // even slots turn clockwise counting up in rotation, odd ones turn counterclockwise
            int a = (twist==0&&slotOtherDirection)?1:0;
            int b = (twist==1&&slotOtherDirection)?1:0;
            int c = (twist==2&&slotOtherDirection)?1:0;
            colorButtons[cornerStickers[3*i]].setBackground(cornerColors[(int)(3*piece + (twist+b-c)%3)]);
            colorButtons[cornerStickers[3*i+1]].setBackground(cornerColors[(int)(3*piece+ (1+twist+a-b)%3)]);
            colorButtons[cornerStickers[3*i+2]].setBackground(cornerColors[(int)(3*piece+ (2+twist-a+c)%3)]);
        }
    }

    public static void printSolutionToOutput(int[] moveSequence) {
        String out = "";
        for (int i = 0; i < moveSequence.length; i++) {
            if (moveSequence[i] == -1) break;
            out += MoveHandler.moveLetters[moveSequence[i]] + " ";
        }
        outputLabel.setText(out);
    }

    public static void printScrambleToInput(int[] scramble) {
        String out = "";
        for (int i = 0; i < scramble.length; i++) {
            out += MoveHandler.moveLetters[scramble[i]] + " ";
        }
        inputField.setText(out);
    }

    public static void inputScramble() {
        MoveHandler.resetCube();
        String in = inputField.getText();
        String[] moves = in.split(" ");
        if (MyFunctions.indexOf(moves[0], MoveHandler.moveLetters) != -1) { // in Scramble syntax
            for (int i = 0; i < moves.length; i++) {
                MoveHandler.inputMove(MyFunctions.indexOf(moves[i], MoveHandler.moveLetters));
            }
        } else { // edge and cornerstates
            MoveHandler.inputEdgeState = Long.parseLong(moves[0]);
            MoveHandler.inputCornerState = Long.parseLong(moves[1]);
            visualizeState(MoveHandler.inputEdgeState, MoveHandler.inputCornerState);
        }
    }

    public static void readCubeFromStickers() {
        long edgeState = readEdges();
        long cornerState = readCorners();
        if (!validateReadStates(edgeState, cornerState)) return;
        System.out.println("Cube in a legal State");
        visualizeState(edgeState, cornerState);
        MoveHandler.inputEdgeState = edgeState;
        MoveHandler.inputCornerState = cornerState;
    }

    public static long readEdges() {
        long edgeState = 0L;
        for (int i = 0; i < 12; i++) { // all positions
            long piece = -1, flip = 0;
            Color firstSticker = colorButtons[edgeStickers[2*i]].getBackground();
            Color secondSticker = colorButtons[edgeStickers[2*i+1]].getBackground();
            for (int j = 0; j < 12; j++) { // all pieces
                for (int k = 0; k < 2; k++) { // all rotations
                    if (edgeColors[2*j+k] == firstSticker && edgeColors[2*j+1-k] == secondSticker) {
                        piece = j;
                        flip = k;
                    }
                }
            }
            if (piece == -1) {
                System.out.println("Edge in slot " + i + " not found");
                return -1;
            }
            edgeState |= (piece|(flip<<4))<<5*i;
        }
        return edgeState;
    }

    public static long readCorners() {
        long cornerState = 0L;
        for (int i = 0; i < 8; i++) { // all positions
            long piece = -1, twist = 0;
            Color firstSticker = colorButtons[cornerStickers[3*i]].getBackground();
            Color secondSticker = colorButtons[cornerStickers[3*i+1]].getBackground();
            Color thirdSticker = colorButtons[cornerStickers[3*i+2]].getBackground();
            for (int j = 0; j < 8; j++) { // all pieces
                for (int k = 0; k < 3; k++) { // all rotations
                    boolean slotOtherDirection = (i%2-j%2)!=0; // even slots turn clockwise counting up in rotation, odd ones turn counterclockwise
                    int a = (k==0&&slotOtherDirection)?1:0;
                    int b = (k==1&&slotOtherDirection)?1:0;
                    int c = (k==2&&slotOtherDirection)?1:0;
                    if (firstSticker == cornerColors[(int)(3*j + (k+b-c)%3)] &&
                    secondSticker == cornerColors[(int)(3*j+ (1+k+a-b)%3)] &&
                    thirdSticker == cornerColors[(int)(3*j+ (2+k-a+c)%3)]) {
                        piece = j;
                        twist = k;
                    }
                }
            }
            if (piece == -1) {
                System.out.println("Corner in slot " + i + " not found");
                return -1;
            }
            cornerState |= (piece|(twist<<3))<<5*i;
        }
        return cornerState;
    }

    public static boolean validateReadStates(long edgeState, long cornerState) {
        // only correct Pieces were read
        if (edgeState == -1 || cornerState == -1) {
            System.out.println("Not all pieces are correct.");
            return false;
        }

        // all edges occur exactly once
        boolean[] edgesExist = new boolean[12];
        for (int i = 0; i < 12; i++) {
            edgesExist[(int)(edgeState>>5*i&MoveHandler.edgePieceMask)] = true;
        }
        if (MyFunctions.indexOf(false, edgesExist) != -1) {
            System.out.println("Some Edges are duplicates");
            return false;
        }

        // all corners occur exactly once
        boolean[] cornersExist = new boolean[8];
        for (int i = 0; i < 8; i++) {
            cornersExist[(int)(cornerState>>5*i&MoveHandler.cornerPieceMask)] = true;
        }
        if (MyFunctions.indexOf(false, cornersExist) != -1) {
            System.out.println("Some Corners are duplicates");
            return false;
        }

        // no Edge Flip Parity
        int edgeFlipCount = 0;
        for (int i = 0; i < 12; i++) {
            edgeFlipCount += (edgeState>>5*i&MoveHandler.edgeFlipMask)>>4;
        }
        if (edgeFlipCount%2 != 0) {
            System.out.println("Edge Flip");
            return false;
        }

        // no Corner Twist Parity
        int cornerTwistCount = 0;
        for (int i = 0; i < 8; i++) {
            if (/*(cornerState>>5*i&MoveHandler.cornerPieceMask)%2 == */i%2 == 0) {
                cornerTwistCount += (cornerState>>5*i&MoveHandler.cornerTwistMask)>>3;   
            } else {
                cornerTwistCount -= (cornerState>>5*i&MoveHandler.cornerTwistMask)>>3;
            }
        }
        if ((cornerTwistCount+18)%3 != 0) {
            System.out.println("Corner Twist");
            return false;
        } 
        // no Swap Parity
        int swapCount = 0; // not real swaps, but the amount of letters you'd remember in BLD solving
        boolean[] edgesVisited = new boolean[12];
        boolean[] cornersVisited = new boolean[8];
        int currentPiece = 0, nextPiece = 0;
        for (int i = 0; i < 18; i++) {
            currentPiece = nextPiece;
            if ((edgeState>>5*currentPiece&MoveHandler.edgePieceMask) != currentPiece) {
                swapCount++;
                if (edgesVisited[currentPiece]) {
                    nextPiece = MyFunctions.indexOf(false, edgesVisited);
                } else {
                    nextPiece = (int)(edgeState>>5*nextPiece&MoveHandler.edgePieceMask);
                }
                edgesVisited[currentPiece] = true;
            } else {
                edgesVisited[currentPiece] = true;
                nextPiece = MyFunctions.indexOf(false, edgesVisited);
            }
            if (nextPiece == -1) break;
        }
        nextPiece = 0;
        for (int i = 0; i < 12; i++) {
            currentPiece = nextPiece;
            if ((cornerState>>5*currentPiece&MoveHandler.cornerPieceMask) != currentPiece) {
                swapCount++;
                if (cornersVisited[currentPiece]) {
                    nextPiece = MyFunctions.indexOf(false, cornersVisited);
                } else {
                    nextPiece = (int)(cornerState>>5*currentPiece&MoveHandler.cornerPieceMask);
                }
                cornersVisited[currentPiece] = true;
            } else {
                cornersVisited[currentPiece] = true;
                nextPiece = MyFunctions.indexOf(false, cornersVisited);
            }
            if (nextPiece == -1) break;
        }
        if (swapCount%2 != 0) {
            System.out.println("Parity");
            return false;
        }

        // cube must be in a legal state
        return true;
    }
}

class KeyDown extends AbstractAction {
    int input;

    KeyDown (int newInput) {
        input = newInput;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        MoveHandler.inputMove(input);
        //throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }
}