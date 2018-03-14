package com.iw2fag.lab.algorithm.sort;

/**
 * Created with IntelliJ IDEA.
 * User: yanghanx
 * Date: 12/18/2017
 * Time: 11:13 AM
 */
public class SelectSort {


    public static int[] selectSort(int[] arr) {

        for (int i = 0; i < arr.length - 1; i++) {
            int min = i;
            int j;
            for (j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
            }
            int temp = arr[i];
            arr[i] = arr[min];
            arr[min] = temp;
        }
        return arr;

    }

}
