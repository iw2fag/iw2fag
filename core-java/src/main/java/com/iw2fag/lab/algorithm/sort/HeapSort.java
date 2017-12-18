package com.iw2fag.lab.algorithm.sort;

/**
 * Created with IntelliJ IDEA.
 * User: yanghanx
 * Date: 12/13/2017
 * Time: 1:24 PM
 */
public class HeapSort {
    public static void main(String[] args) {
        int[] arr = {1, 3, 5, 7, 9, 2, 4, 6, 8, 10};
        heapSort(arr);
        printArr(arr);
    }

    public static void heapSort(int[] arr) {
        int N = arr.length - 1;
        //build heapify
        for (int i = arr.length / 2 - 1; i >= 0; i--) {
            sink(arr, arr.length, i);//10 9 5 8 3 2 4 6 7 1
        }

        while (N >= 1) {
            int temp = arr[0];
            arr[0] = arr[N];
            arr[N] = temp;
            N--;
            sink(arr, N, 0);
        }

    }

    private static void swim(int arr[], int index) {
        while (index > 0 && (index - 1) / 2 >= 0) {
            if (arr[index] > arr[(index - 1) / 2]) {
                System.out.println("exec:" + index + "," + (index - 1) / 2);
                int temp = arr[(index - 1) / 2];
                arr[(index - 1) / 2] = arr[index];
                arr[index] = temp;

                index = (index - 1) / 2;
            }
        }
    }


    private static void sink(int[] arr, int N, int index) {
        while (2 * index < N) {
            int t = index * 2 + 1;
            if (t + 1 < N && arr[t] < arr[t + 1]) {
                t++;
            }
            System.out.println("exec:" + index + "," + t);
            if (arr[t] <= arr[index]) {
                break;
            }
            int temp = arr[t];
            arr[t] = arr[index];
            arr[index] = temp;
            index = t;

        }
    }


    public static void printArr(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

}
