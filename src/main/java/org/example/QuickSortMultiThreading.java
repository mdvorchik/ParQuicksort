package org.example;

import java.util.concurrent.RecursiveTask;

public class QuickSortMultiThreading extends RecursiveTask<Integer> {

    int start, end;
    int[] arr;

    public QuickSortMultiThreading() {

    }

    //Hoare Splitting
    public QuickSortMultiThreading(int start,
                                   int end,
                                   int[] arr) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    private int partition(int start, int end, int[] arr) {
        int pivot = arr[(start + end) / 2];
        int i = start, j = end;

        while (true) {
            while (arr[i] < pivot)
                i++;
            while (arr[j] > pivot)
                j--;
            if (i >= j)
                return j;
            int temp = arr[j];
            arr[j] = arr[i];
            arr[i] = temp;
            i++;
            j--;
        }
    }

    public void quickSortSeq(int start, int end, int[] arr) {
        if (start >= end)
            return;
        int p = partition(start, end, arr);
        quickSortSeq(start, p, arr);
        quickSortSeq(p + 1, end, arr);
    }

    @Override
    protected Integer compute() {
        return quickSortPar();
    }

    private Integer quickSortPar() {
        if (start >= end)
            return null;
        int p = partition(start, end, arr);
        QuickSortMultiThreading left
                = new QuickSortMultiThreading(start, p, arr);
        QuickSortMultiThreading right
                = new QuickSortMultiThreading(p + 1, end, arr);
        left.fork();
        right.compute();
        left.join();
        return null;
    }
}
