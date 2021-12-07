package com.oop.labs.gesturetracker.dtw;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DTWAlgorithm {
    public static double compute(final double[] ASeries, final double[] BSeries) {
        int ASeriesLength = ASeries.length;
        int BSeriesLength = BSeries.length;

        double[][] ManhattanDistancesMatrix = calculateManhattanDistances(ASeries, BSeries);
        double[][] costMatrix = calculateCostMatrix(ManhattanDistancesMatrix,
                ASeriesLength, BSeriesLength);

        List<Double> warpingPath = findWarpingPath(ManhattanDistancesMatrix, costMatrix,
                ASeriesLength, BSeriesLength);

        return calculateDTWResult(warpingPath);
    }

    private static double[][] calculateManhattanDistances(double[] ASeries, double[] BSeries) {
        int ASeriesLength = ASeries.length;
        int BSeriesLength = BSeries.length;
        double[][] ManhattanDistancesMatrix = new double[ASeriesLength][BSeriesLength];
        for (int i = 0; i < ASeriesLength; i++) {
            for (int j = 0; j < BSeriesLength; j++) {
                ManhattanDistancesMatrix[i][j] = Math.abs(ASeries[i] - BSeries[j]);
            }
        }
        return ManhattanDistancesMatrix;
    }

    private static double[][] calculateCostMatrix(double[][] ManhattanDistancesMatrix,
                                                  int ASeriesLength, int BSeriesLength) {
        double[][] costMatrix = new double[ASeriesLength][BSeriesLength];
        for (int i = 0; i < ASeriesLength; i++) {
            for (int j = 0; j < BSeriesLength; j++) {
                costMatrix[i][j] = ManhattanDistancesMatrix[i][j]
                        + costMatrixMin(costMatrix, i, j).getValue();
            }
        }
        return costMatrix;
    }

    private static WPStructure costMatrixMin(double[][] costMatrix, int curr_i, int curr_j) {
        if (curr_i == 0 && curr_j == 0) {
            return new WPStructure(0, 0, 0);
        } else if (curr_i == 0) {
            return new WPStructure(curr_i, curr_j - 1, costMatrix[curr_i][curr_j - 1]);
        } else if (curr_j == 0) {
            return new WPStructure(curr_i - 1, curr_j, costMatrix[curr_i - 1][curr_j]);
        } else {
            double value = Math.min(Math.min(costMatrix[curr_i - 1][curr_j - 1],
                    costMatrix[curr_i - 1][curr_j]), costMatrix[curr_i][curr_j - 1]);
            int iOfMin, jOfMin;
            if (value == costMatrix[curr_i - 1][curr_j - 1]) {
                iOfMin = curr_i - 1;
                jOfMin = curr_j - 1;
            } else if (value == costMatrix[curr_i - 1][curr_j]) {
                iOfMin = curr_i - 1;
                jOfMin = curr_j;
            } else {
                iOfMin = curr_i;
                jOfMin = curr_j - 1;
            }
            return new WPStructure(iOfMin, jOfMin, value);
        }
    }

    private static List<Double> findWarpingPath(double[][]ManhattanDistancesMatrix,
                                                double[][] costMatrix,
                                                int ASeriesLength, int BSeriesLength) {
        List<Double> warpingPath
                = new ArrayList<>(Math.max(ASeriesLength, BSeriesLength));
        int i = ASeriesLength - 1, j = BSeriesLength - 1;
        warpingPath.add(ManhattanDistancesMatrix[ASeriesLength - 1][BSeriesLength - 1]);
        while (i != 0 && j != 0) {
            WPStructure wpStructure = costMatrixMin(costMatrix, i, j);
            i = wpStructure.getI();
            j = wpStructure.getJ();
            warpingPath.add(ManhattanDistancesMatrix[i][j]);
        }
        return warpingPath;
    }

    private static double calculateDTWResult(List<Double> warpingPath) {
        double DTW = 0;
        for (double element : warpingPath) {
            DTW += element;
        }
        DTW = DTW / warpingPath.size();
        return DTW;
    }
}
