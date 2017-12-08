package com.iw2fag.lab.algorithm;

/**
 * Created with IntelliJ IDEA.
 * User: yanghanx
 * Date: 12/6/2017
 * Time: 5:30 PM
 */
public class Sort {

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
