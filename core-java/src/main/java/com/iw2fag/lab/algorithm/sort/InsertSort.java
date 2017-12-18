package com.iw2fag.lab.algorithm.sort;

/**
 * Created with IntelliJ IDEA.
 * User: yanghanx
 * Date: 12/18/2017
 * Time: 11:14 AM
 */
public class InsertSort {

    public static int[] insertSort(int[] arr) {

        for (int i = 1; i < arr.length; i++) {
            int j = i - 1;
            int t = arr[i];
            while (j >= 0 && arr[j] > t) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = t;
        }

        return arr;
    }

}
