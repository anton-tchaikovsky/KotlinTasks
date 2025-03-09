package com.example.kotlintasks.quick_sort;

import java.util.Arrays;

public class QuickSort {

    static int count;

    public static void quickSort(int[] array) {
        quickSort(array, 0, array.length - 1);
    }

    private static void quickSort(int[] array, int leftId, int rightId) {
        if (count==0){
            System.out.println("QuickSort");
            System.out.println("Исходный массив " + Arrays.toString(array));
        }

        else
            System.out.println("Исходный подмассив " + Arrays.toString(Arrays.copyOfRange(array, leftId, rightId+1)));

        if (array.length ==0 || leftId >= rightId)
            return;

        int pivot = array[leftId + (rightId - leftId) / 2];
        System.out.println("Опорный пункт: " + pivot);
        int i = leftId, j = rightId;

        while (i <= j) {
            while (array[i] < pivot) {
                i++;
            }

            while (array[j] > pivot) {
                j--;
            }

            if (i <= j) {
                swap(array, i, j);
                i++;
                j--;
            }
        }
        count++;
        System.out.println("Шаг " + count + " " + Arrays.toString(array));
        if (leftId < j)
            quickSort(array, leftId, j);
        if (rightId > i)
            quickSort(array, i, rightId);
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
