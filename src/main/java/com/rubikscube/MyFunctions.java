package com.rubikscube;

public class MyFunctions {
    public static int indexOf(Object object, Object[] array) {
        for (int i = 0; i < array.length; i++) {
            if (object == array[i] || object.equals(array[i])) {
                return i;
            }
        }

        return -1;
    }

    public static int indexOf(String object, String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (object.equals(array[i])) {
                return i;
            }
        }

        return -1;
    }

    public static int indexOf(boolean object, boolean[] array) {
        for (int i = 0; i < array.length; i++) {
            if (object == array[i]) {
                return i;
            }
        }

        return -1;
    }

    public static int getRandomInt(int lowerBound, int upperBound) { // both inclusive
        return (int)(Math.random()*(upperBound-lowerBound+1)+lowerBound);
    }
}
