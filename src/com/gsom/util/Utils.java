package com.gsom.util;

import com.gsom.util.GSOMConstants;
import java.util.Map;

import com.gsom.objects.GNode;
import com.gsom.ui.MainWindow;

public class Utils {

    
    public static double[] generateLinearArray(int dimensions,double startVal){
        double[] arr = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            arr[i]=startVal;
        }
        return arr;
    }
    
    public static double[] generateRandomArray(int dimensions) {
        double[] arr = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            arr[i] = Math.random();
        }
        return arr;
    }

    public static String generateIndexString(int x, int y) {
        return x + "," + y;
    }

    public static double getLearningRate(int iter, int nodeCount) {
        return GSOMConstants.START_LEARNING_RATE * Math.exp(-(double) iter / GSOMConstants.MAX_ITERATIONS) * (1 - (double)(3.8 / nodeCount));
    }

    public static double getTimeConst() {
        return (double) GSOMConstants.MAX_ITERATIONS / Math.log(GSOMConstants.MAX_NEIGHBORHOOD_RADIUS);
    }

    //get the node with the minimal ED to the input (winner)
    public static GNode selectWinner(Map<String, GNode> nodeMap, double[] input) {
        GNode winner = null;
        double currDist = Double.MAX_VALUE;
        double minDist = Double.MAX_VALUE;

        if (MainWindow.distance == 0) {
            for (Map.Entry<String, GNode> entry : nodeMap.entrySet()) {
                currDist = Utils.calcEucDist(input, entry.getValue().getWeights(), GSOMConstants.DIMENSIONS);

                if (currDist < minDist) {
                    winner = entry.getValue();
                    minDist = currDist;
                }
            }
            return winner;
        } else if (MainWindow.distance == 1) {
            for (Map.Entry<String, GNode> entry : nodeMap.entrySet()) {
                currDist = Utils.calcChiDistance(input, entry.getValue().getWeights(),GSOMConstants.DIMENSIONS);

                if (currDist < minDist) {
                    winner = entry.getValue();
                    minDist = currDist;
                }
            }
            return winner;
        } else if (MainWindow.distance == 2) {
            for (Map.Entry<String, GNode> entry : nodeMap.entrySet()) {
                currDist = Utils.calcCosineDist(input, entry.getValue().getWeights(),GSOMConstants.DIMENSIONS);

                if (currDist < minDist) {
                    winner = entry.getValue();
                    minDist = currDist;
                }
            }
            return winner;
        }
        return null;

    }

    //---TESTED INline with the C# code---
    public static GNode adjustNeighbourWeight(GNode node, GNode winner, double[] input, double radius, double learningRate) {
        double nodeDistSqr = Math.pow(winner.getX() - node.getX(), 2) + Math.pow(winner.getY() - node.getY(), 2);
        double radiusSqr = radius*radius;
        //if node is within the radius
        if (nodeDistSqr < radiusSqr) {
            double influence = Math.exp(-(double)nodeDistSqr / (2 * radiusSqr));
            node.adjustWeights(input, influence, learningRate);
        }
        return node;
    }

    public static double getRadius(int iter, double timeConst) {
        return GSOMConstants.MAX_NEIGHBORHOOD_RADIUS * Math.exp(-(double) iter / timeConst);
    }

    public static double calcEucDist(double[] in1, double[] in2, int dimensions) {
        double dist = 0.0;
        for (int i = 0; i < dimensions; i++) {
            dist += Math.pow(in1[i] - in2[i], 2);
        }

        return Math.sqrt(dist);
    }

    public static double calcChiDistance(double[] vec1, double[] vec2,int dimensions) {
        
        double total=0;
        normalizeVector(vec1);
        normalizeVector(vec2);
        
        for(int i=0;i<dimensions;i++){
            total += Math.pow(vec1[i]-vec2[i], 2)/(vec1[i]+vec2[i]);
        }
        return 0.5*total;
    }

    public static double calcIntersectionDist(double[] vec1, double[] vec2, int dimensions) {
        double total = 0;
        
        normalizeVector(vec1);
        normalizeVector(vec2);
        
        for (int i = 0; i < dimensions; i++) {
            total += Math.min(vec1[i], vec2[i]);
        }
        return 1-total;
    }
    
    public static double calcCosineDist(double[] vec1,double[] vec2,int dimensions){
        double total = 0;
        for(int i=0;i<dimensions;i++){
            total += vec1[i]*vec2[i];
        }
        return total/(dimensions*dimensions);
    }
    private static void normalizeVector(double[] vec){
        double max1=0;
        
        for(int i=0;i<vec.length;i++){
            max1 = Math.max(max1, vec[i]);
        }
        
        for(int i=0;i<vec.length;i++){
            vec[i]=(double)vec[i]/max1;           
        }
    
    }
}
