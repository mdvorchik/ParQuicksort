package org.example;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class App {
    public static void main(String[] args) {
        int[] arrForWarmJVM = new int[10_000_000];
        int[] arrForSeq = new int[100_000_000];
        int[] checkCorrectSeq = {7, 2, 98, 455, 453245, 32423, 5};
        int[] checkCorrectPar = checkCorrectSeq.clone();

        Random random = new Random();

        fillArr(arrForWarmJVM, random);
        fillArr(arrForSeq, random);
        int[] arrForPar = arrForSeq.clone();

        ForkJoinPool poolPar = new ForkJoinPool(4);

        //Check correct
        System.out.println("Raw array for fast check correct:");
        printArr(checkCorrectSeq);

        System.out.println("Sorted by Par:");
        poolPar.invoke(new QuickSortMultiThreading(0, checkCorrectPar.length - 1, checkCorrectPar));
        printArr(checkCorrectPar);

        System.out.println("Sorted by Seq:");
        new QuickSortMultiThreading().quickSortSeq(0, checkCorrectSeq.length - 1, checkCorrectSeq);
        printArr(checkCorrectSeq);

        // Warm JVM
        poolPar.invoke(new QuickSortMultiThreading(0, arrForWarmJVM.length - 1, arrForWarmJVM));


        // Calculate Seq
        long startTime = System.nanoTime();
        new QuickSortMultiThreading().quickSortSeq(0, arrForSeq.length - 1, arrForSeq);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Seq: " + (duration / 1000000) + "ms");

        // Calculate Par
        startTime = System.nanoTime();
        poolPar.invoke(new QuickSortMultiThreading(0, arrForPar.length - 1, arrForPar));
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        System.out.println("Par: " + (duration / 1000000) + "ms");
    }

    private static void printArr(int[] checkCorrectSeq) {
        for (int i : checkCorrectSeq) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    private static void fillArr(int[] arr, Random random) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(Integer.MAX_VALUE);
        }
    }
}
