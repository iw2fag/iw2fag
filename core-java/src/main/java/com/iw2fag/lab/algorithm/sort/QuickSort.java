package com.iw2fag.lab.algorithm.sort;

/**
 * Created with IntelliJ IDEA.
 * User: yanghanx
 * Date: 12/6/2017
 * Time: 5:30 PM
 */
public class QuickSort {

    public static int[] quickSort(int[] arr) {
        return quickSort(arr, 0, arr.length - 1);
    }

    private static int[] quickSort(int[] arr, int low, int high) {

        if (high == -1 || low == arr.length - 1 || low > high) {
            return arr;
        }

        int i = low;
        int j = high + 1;
        int base = arr[low];

        while (true) {

            while (arr[++i] < base) {
                if (i == high) {
                    break;
                }
            }

            while (arr[--j] > base) {
                if (j == low) {
                    break;
                }
            }

            if (i >= j) {
                break;
            }


            if (i < j) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }

        }

        // i j
        int temp = arr[low];
        arr[low] = arr[j];
        arr[j] = temp;

        //System.out.println(i + "-" + j);

        quickSort(arr, low, j - 1);
        quickSort(arr, j + 1, high);

        return arr;
    }


    public static void printArr(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }


}
