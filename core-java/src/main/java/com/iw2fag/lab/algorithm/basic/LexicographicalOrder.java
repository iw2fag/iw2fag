package com.iw2fag.lab.algorithm.basic;


import java.util.Arrays;

/**
 * Given a number N, get the minimum value which is larger than N.
 * N=12345 V=12354
 * N=12354 V=12435
 * N=12435 V=12453
 */
public class LexicographicalOrder {
    public static void main(String[] args) {
        int[] arr1 = {1, 2, 3, 4, 5};
        int[] arr2 = {1, 2, 3, 5, 4};
        int[] arr3 = {5, 4, 6, 7, 8, 2};
        printArr(getNearestNumber(arr1));
        printArr(getNearestNumber(arr2));
        printArr(getNearestNumber(arr3));

    }

    /**
     * 1:find the index which is (the end point index +1)of the reversed order, mark it as: index
     * 2:get the minimum value which is larger than n[index] from n[index+1]-n[n.length-1], exchange the value with n[index]
     * 3:sort n[index+1]-n[n.length-1]
     *
     * @param n
     * @return v
     */
    public static int[] getNearestNumber(int[] n) {

        //1:
        int index = n.length - 1;
        for (int i = n.length - 1; i >= 1; i--) {
            if (n[i] < n[i - 1]) {
                index = i - 1;
                break;
            }
        }
        int exchangeIndex = index - 1;
        System.out.println("exchangeIndex:" + exchangeIndex);

        //exchange number
        int base = n.length - 1;

        for (int i = n.length - 1; i > exchangeIndex; i--) {
            if (n[i] > n[exchangeIndex]) {
                base = i;
                break;
            }
        }

        int tmp = n[index - 1];
        n[index - 1] = n[base];
        n[base] = tmp;

        //sort
        Arrays.sort(n, index, n.length);

        return n;
    }

    public static void printArr(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

}
