//package GaussianEliminationwithPartialPivoting;  //uncomment to run in main, comment to run here

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class GaussianEliminationMethods {

    public Vector<Double> stringToDblVector(String coefficients) {
        String[] abc = coefficients.split(" "); //whitespaces can be denoted by \\s+

        Vector<Double> coeffVec = new Vector<Double>();

        for(String strCoeff : abc){
            double intCoeff = Double.parseDouble(strCoeff);
            coeffVec.addElement(intCoeff);
        }

        //System.out.println("Inputted Vector: " + coeffVec);
        return coeffVec;
    }

    public void printMatrix(Vector<Vector<Double>> coeffVec, int varCount) {
        for(int x=0;x<varCount;x++){
            for(int y=coeffVec.get(x).size()-1;y>=0;y--){
                if (y!=0) System.out.printf("%-12s",coeffVec.get(x).get(y));
                else System.out.printf(":%12s",coeffVec.get(x).get(y));
            }
            System.out.println();
        }
        return;
    }

    /*public double getA3 (Vector<Vector<Double>> coeffVec) {
        double a3 = coeffVec.get(2).get(0) / coeffVec.get(2).get(1);
        DecimalFormat df = new DecimalFormat("0.00000");
        if(a3 > 0) df.setRoundingMode(RoundingMode.FLOOR);
        else df.setRoundingMode(RoundingMode.CEILING);
        double truncatedA3 = Double.parseDouble(df.format(a3));
        return truncatedA3;
    }

    public double getA2 (Vector<Vector<Double>> coeffVec, double a3) {
        double a2 = (coeffVec.get(1).get(0) + ((coeffVec.get(1).get(1)*-1)*a3)) / coeffVec.get(1).get(2);
        DecimalFormat df = new DecimalFormat("0.00000");
        if(a2 > 0) df.setRoundingMode(RoundingMode.FLOOR);
        else df.setRoundingMode(RoundingMode.CEILING);
        double truncatedA2 = Double.parseDouble(df.format(a2));
        return truncatedA2;
    }
    
    public double getA1 (Vector<Vector<Double>> coeffVec, double a3, double a2) {
        double a1 = (coeffVec.get(0).get(0) + ((coeffVec.get(0).get(1)*-1)*a3) + ((coeffVec.get(0).get(2)*-1)*a2)) / coeffVec.get(0).get(3);
        DecimalFormat df = new DecimalFormat("0.00000");
        if(a1 > 0) df.setRoundingMode(RoundingMode.FLOOR);
        else df.setRoundingMode(RoundingMode.CEILING);
        double truncatedA1 = Double.parseDouble(df.format(a1));
        return truncatedA1;
    }*/
}
