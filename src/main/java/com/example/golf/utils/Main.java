package com.example.golf.utils;

import java.util.Arrays;

public class Main {


    public static int[][] multiply(int [][] a, int [][] b) {
        int m = a.length;
        int n = a[0].length;
        int p = b[0].length;
        int q = b.length;
        if(n != q) {
            throw new IllegalArgumentException("Matrix dimensions do not match for multiplication.");
        }
        int[][] result = new int[m][p];
        for(int i = 0; i < m; i++)
            for(int j = 0; j< p; j++)
                for(int k = 0; k < n; k++)
                    result[i][j] += a[i][k] * b[k][j];
        return result;
    }
    public static void printArray(int[][] arr) {
        for (int[] row : arr) {
            System.out.println(Arrays.toString(row));
        }
    }
    public static void main(String[] args) {
        System.out.println("Hello, Golf Application!");
        int[][] arr = {
            {1, 2, 3},
            {4, 5, 6},
        };
        int [][] arr2 = {{1,2,3},{4,5,6}, {7,8,9}};

    }
}
