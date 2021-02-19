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
        
        //create nxn matrix
        double[][] matrix = methods.setMatrix(coeffVec,varCount);

        System.out.println("\nStarting Matrix: ");
        methods.printMatrix(matrix);

        //create upper triangular matrix
        getUpperTriangularMatrix(matrix,0,0,0,1);

        //solve for missing variables
        double[] missingVars = methods.solveMissingVars(matrix);
        System.out.print("\nMISSING VARIABLES = [");
        for(int x=0;x<missingVars.length;x++) {
            System.out.print(missingVars[x]);
            if(x!=missingVars.length-1) System.out.print(",");
        }
        System.out.println("]");
    }

    //user input
    public static void setCoeff(){
        //define functions
        System.out.println("Naive Gaussian Elimination: Solve a Simultaneous Linear Equation!");
        System.out.print("\nEnter number of variables: ");
        varCount = variables.nextInt();
        System.out.println("Equation's coefficients ([D C B A] Format): ");

        for(int x=1;x<varCount+1;x++){
            System.out.print("Eq." + x + ": ");
            tempVec = methods.stringToDblVector(coefficients.nextLine());
            coeffVec.addElement(tempVec);
        }

         //System.out.println("\nStarting Matrix: ");
         //methods.printMatrix(coeffVec,varCount);
        return;
    }

    public static void getUpperTriangularMatrix(double[][] matrix, int currIndex, int refRow, int column, int row){
        double[][] updatedMatrix = new double[matrix.length][matrix.length+1];
        for (int x=row;x<=varCount-1;x++){
            double[] productRow = new double[varCount+1];
            double[] newRow = new double[varCount+1];

            int prodRowIndex = 0;

            BigDecimal numerator = new BigDecimal(Double.toString(matrix[x][currIndex]));
            BigDecimal denominator = new BigDecimal(Double.toString(matrix[refRow][currIndex]));
            BigDecimal multiplier = numerator.divide(denominator,MathContext.DECIMAL64);

            System.out.println("\na" + (x+1) + (column+1) + " = " + numerator);
            System.out.println("a" + row + (column+1) + " = " + denominator);
            System.out.println("Multiplier = " + multiplier);

            for(int i=0;i<=varCount;i++){
                BigDecimal coefficient = new BigDecimal(Double.toString(matrix[refRow][i]));
                BigDecimal product = coefficient.multiply(multiplier);
                productRow[i] = product.doubleValue();
            }

            System.out.print("PRODUCT ROW = [");
            methods.printArray(productRow);
            System.out.println("]");
        
            for(int j=0;j<=varCount;j++){                                
                double rowDiff = matrix[x][j] - productRow[prodRowIndex];

                BigDecimal coefficient = new BigDecimal(Double.toString(matrix[x][j]));
                BigDecimal product = new BigDecimal(Double.toString(productRow[prodRowIndex]));
                BigDecimal diff = coefficient.subtract(product);                                
                BigDecimal truncatedDiff;

                if(rowDiff > 0) truncatedDiff = diff.setScale(5,RoundingMode.FLOOR);
                else truncatedDiff = diff.setScale(5,RoundingMode.CEILING);

                Double doubleDiff = truncatedDiff.doubleValue();
                newRow[j] = doubleDiff;
                prodRowIndex++;
            }

            System.out.print("NEW ROW = [");
            methods.printArray(newRow);
            System.out.println("]");   
            
            updatedMatrix = methods.updateMatrix(matrix,newRow,x);

            System.out.println("\nUpdated Matrix: ");
            methods.printMatrix(updatedMatrix);
        }
        if(refRow < varCount-2) {
            getUpperTriangularMatrix(updatedMatrix,currIndex+1,refRow+1,column+1,row+1);
        }
        return;
    }

}