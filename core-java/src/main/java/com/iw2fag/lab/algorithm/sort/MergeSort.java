package com.iw2fag.lab.algorithm.sort;

/**
 * Created with IntelliJ IDEA.
 * User: yanghanx
 * Date: 12/18/2017
 * Time: 11:14 AM
 */
public class MergeSort {

    //10 0-9:0-4 5-9
    public static int[] mergeSort(int[] arr, int lo, int hi) {
        if (lo >= hi) {
            return arr;
        }
        int mid = lo + (hi - lo) / 2;
        mergeSort(arr, lo, mid);
        mergeSort(arr, mid + 1, hi);
        merge(arr, lo, mid, hi);
        return arr;
    }


    private static int[] merge(int[] arr, int first, int mid, int last) {
        int[] newArr = new int[last + 1];

        int l = first;
        int r = mid + 1;

        for (int i = first; i <= last; i++) {
            newArr[i] = arr[i];
        }

        for (int i = first; i <= last; i++) {

            if (l > mid) {
                arr[i] = newArr[r++];
            } else if (r > last) {
                arr[i] = newArr[l++];
            } else if (newArr[l] < newArr[r]) {
                arr[i] = newArr[l++];
            } else {
                arr[i] = newArr[r++];
            }
        }
        return arr;
    }

}
