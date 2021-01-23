//package GaussianEliminationwithPartialPivoting; //uncomment to run in main, comment to run here

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.math.*;

public class GaussianElimination {
    private static GaussianEliminationMethods methods = new GaussianEliminationMethods();
    private static Scanner variables = new Scanner(System.in);
    private static Scanner coefficients = new Scanner(System.in);
    private static int varCount = 0;
    private static Vector<Double> tempVec = new Vector<Double>(); //[C,B,A]
    private static Vector<Vector<Double>> coeffVec = new Vector<Vector<Double>>(); //[[C,B,A],[C,B,A],[C,B,A]]    

    public static void main(String[] args) {
        //set function assumptions
        setCoeff();
        getUpperTriangularMatrix(coeffVec,varCount,0,0,1);
        /*double var3 = methods.getA3(coeffVec);
        System.out.println("a3 = " + var3);
        double var2 = methods.getA2(coeffVec,var3);
        System.out.println("a2 = " + var2);
        double var1 = methods.getA1(coeffVec,var3,var2);
        System.out.println("a1 = " + var1);*/
    }

    //user input
    public static void setCoeff(){
        //define functions
        System.out.println("Gaussian Elimination: Solve a Simultaneous Linear Equation!");
        System.out.print("\nEnter number of variables: ");
        varCount = variables.nextInt();
        System.out.println("Equation's coefficients ([D C B A] Format): ");

        for(int x=1;x<varCount+1;x++){
            System.out.print("Eq." + x + ": ");
            tempVec = methods.stringToDblVector(coefficients.nextLine());
            coeffVec.addElement(tempVec);
        }

        System.out.println("\nStarting Matrix: ");
        methods.printMatrix(coeffVec,varCount);
        return;
    }

    public static void getUpperTriangularMatrix(Vector<Vector<Double>> coeffVec, int maxIndex, int refRow, int column, int row) {
        Vector<Vector<Double>> updatedMatrix = new Vector<Vector<Double>>();
        for(int x=row;x<=varCount-1;x++) {
            for(int y=varCount;y>=0;y--){
                if(y==varCount) {
                    Vector<Double> productRow = new Vector<Double>();
                    Vector<Double> newRow = new Vector<Double>();

                    int prodRowIndex = 0;

                    double dobMulti = coeffVec.get(x).get(maxIndex) / coeffVec.get(refRow).get(maxIndex);

                    BigDecimal numerator = new BigDecimal(coeffVec.get(x).get(maxIndex));
                    BigDecimal denominator = new BigDecimal(coeffVec.get(refRow).get(maxIndex));
                    BigDecimal multiplier;

                    if(coeffVec.get(x).get(maxIndex)<0 && coeffVec.get(refRow).get(maxIndex)<0) 
                    multiplier = numerator.divide(denominator,5,RoundingMode.FLOOR);
                    else multiplier = numerator.divide(denominator,MathContext.DECIMAL128);

                    System.out.println("\na" + (row+1) + (column+1) + " = " + numerator);
                    System.out.println("a" + row + (column+1) + " = " + denominator);
                    System.out.println("Multiplier = " + multiplier);

                    for(int i=varCount;i>=0;i--){
                        //double dobProd = coeffVec.get(refRow).get(i) * dobMulti;

                        BigDecimal coefficient = new BigDecimal(Double.toString(coeffVec.get(refRow).get(i)));
                        BigDecimal product = coefficient.multiply(multiplier);

                        /*BigDecimal truncatedProd;
                        if(dobProd > 0) truncatedProd = product.setScale(5,RoundingMode.FLOOR);
                        else truncatedProd = product.setScale(5,RoundingMode.CEILING);*/

                        Double doubleProd = product.doubleValue();
                        productRow.addElement(doubleProd);
                    }
                    System.out.println("PRODUCT ROW = " + productRow);
                    for(int j=varCount;j>=0;j--){                                
                        double rowDiff = coeffVec.get(x).get(j) - productRow.get(prodRowIndex);

                        BigDecimal coefficient = new BigDecimal(Double.toString(coeffVec.get(x).get(j)));
                        BigDecimal product = new BigDecimal(Double.toString(productRow.get(prodRowIndex)));
                        BigDecimal diff = coefficient.subtract(product);                                
                        BigDecimal truncatedDiff;

                        if(rowDiff > 0) truncatedDiff = diff.setScale(5,RoundingMode.FLOOR);
                        else truncatedDiff = diff.setScale(5,RoundingMode.CEILING);

                        Double doubleDiff = truncatedDiff.doubleValue();
                        newRow.addElement(doubleDiff);
                        prodRowIndex++;
                    }
                    
                    System.out.println("NEW ROW = " + newRow);
                    Collections.reverse(newRow);
                    coeffVec.set(x,newRow);
                    updatedMatrix = coeffVec;

                    System.out.println("\nUpdated Matrix: ");
                    methods.printMatrix(updatedMatrix,varCount);
                }                    
            }
        }
        if(refRow < varCount-2) {
            getUpperTriangularMatrix(updatedMatrix,maxIndex-1,refRow+1,column+1,row+1);
            System.out.println();
        }
        return;
    }

}