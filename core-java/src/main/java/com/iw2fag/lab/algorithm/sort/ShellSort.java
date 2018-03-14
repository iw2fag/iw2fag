package com.iw2fag.lab.algorithm.sort;

/**
 * Created with IntelliJ IDEA.
 * User: yanghanx
 * Date: 12/18/2017
 * Time: 11:12 AM
 */
public class ShellSort {

    public static int[] shellSort(int[] arr) {
        int h = 1;

        while (h < arr.length / 3) {
            h = h * 3 + 1;
        }

        while (h >= 1) {

            for (int i = h; i < arr.length; i++) {
                for (int j = i; j >= h && arr[j] < arr[j - h]; j -= h) {
                    System.out.println(i + " " + j);
                    int temp = arr[j];
                    arr[j] = arr[j - h];
                    arr[j - h] = temp;
                }
            }

            h /= 3;

        }
        return arr;
    }

}
