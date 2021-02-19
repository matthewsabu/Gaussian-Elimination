import java.util.*;
import java.math.*;
//Created by: Matthew Simon M. Sabularse | ID 11813652
public class GaussianElimination {

    public static void main(String[] args) {
        Scanner variables = new Scanner(System.in);
        Scanner coefficients = new Scanner(System.in);
        Vector<Vector<Double>> coeffVecNest = new Vector<Vector<Double>>(); //[[D C,B,A],[D C,B,A],[D C,B,A]]    

        System.out.println("Gaussian Elimination: Solve Simultaneous Linear Equations!");
        System.out.print("\nEnter number of missing variables: ");
        int varCount = variables.nextInt(); //determines matrix size

        //set function assumptions
        coeffVecNest = setCoeff(variables,coefficients,coeffVecNest,varCount);
        
        //create nxn matrix
        double[][] matrix = setMatrix(coeffVecNest,varCount);

        System.out.println("\nStarting Matrix: ");
        printMatrix(matrix);

        //create upper triangular matrix
        getUpperTriangularMatrix(matrix,0,0,0,1);

        //solve for missing variables & display
        double[] missingVars = solveMissingVars(matrix);
        System.out.println("\nMISSING VARIABLES:");
        for(int x=0;x<missingVars.length;x++) {
            System.out.println("x" + (x+1) + " = " + missingVars[x]);
        }

        variables.close();
        coefficients.close();
    }

    //user input
    public static Vector<Vector<Double>> setCoeff(Scanner variables, Scanner coefficients, Vector<Vector<Double>> coeffVecNest,int varCount){
        //define functions
        System.out.println("Equation's coefficients ([D C B A] Format): ");

        Vector<Double> tempVec = new Vector<Double>(); //[D C,B,A]

        for(int x=1;x<varCount+1;x++){
            System.out.print("Eq." + x + ": ");
            tempVec = setVector(coefficients.nextLine());
            coeffVecNest.addElement(tempVec);
        }

         //System.out.println("\nNested Vector: " + coeffVecNest);
        return coeffVecNest;
    }

    public static Vector<Double> setVector(String coefficients) {
        String[] abc = coefficients.split(" "); //whitespaces can be denoted by \\s+

        Vector<Double> coeffVec = new Vector<Double>();

        for(String strCoeff : abc){
            double intCoeff = Double.parseDouble(strCoeff);
            coeffVec.addElement(intCoeff);
        }

        //System.out.println("Inputted Vector: " + coeffVec);
        return coeffVec;
    }

    public static double[][] setMatrix(Vector<Vector<Double>> coeffVec, int matrixSize){
        double[][] matrix = new double[matrixSize][matrixSize+1];   //[rows][columns]
            for (int row=0;row<matrixSize;row++){
                int index = matrixSize;
                for (int col=0;col<=matrixSize;col++){
                    matrix[row][col] = coeffVec.get(row).get(index);
                    index--;
                }
            }
        return matrix;
    }

    public static void printMatrix(double[][] matrix) {
        int row = 0;
        for(double[] rowValues : matrix){
            int column = 0;
            for(double value : rowValues){
                if (column!=matrix.length) System.out.printf("%-12s",value);
                else {
                    System.out.printf(":%6s","");
                    System.out.printf("%s",value);
                }
                column++;
            }
            row++;
            System.out.println();
        }
        return;
    }

    public static void getUpperTriangularMatrix(double[][] matrix, int currIndex, int refRow, int column, int row){
        double[][] updatedMatrix = new double[matrix.length][matrix.length+1];
        for (int rowCurr=row;rowCurr<=matrix.length-1;rowCurr++){            
            //perform partial pivoting
            //COMMENT OUT IF PERFORMING NAIVE (no partial pivoting)
            matrix = pivotMatrix(matrix,refRow,rowCurr);
            
            //BigDecimal is more precise than double
            BigDecimal aUpper = new BigDecimal(Double.toString(matrix[rowCurr][currIndex]));  //string parameter lets us truncate later on
            BigDecimal aLower = new BigDecimal(Double.toString(matrix[refRow][currIndex]));
            BigDecimal multiplier = aUpper.divide(aLower,MathContext.DECIMAL64); //limits to 16 decimal points; avoids non-terminating decimals

            System.out.println("\na" + (rowCurr+1) + (column+1) + " = " + aUpper);
            System.out.println("a" + row + (column+1) + " = " + aLower);
            System.out.println("Multiplier = (" + aUpper + "/" + aLower + ")");

            //creates product row array            
            double[] productRow = new double[matrix.length+1];
            int index = 0;
            for(double value : matrix[refRow]){
                BigDecimal coefficient = new BigDecimal(Double.toString(value));
                BigDecimal product = coefficient.multiply(multiplier);
                if(index != matrix.length-1) productRow[index] = product.doubleValue();  //truncating product row values doesn't make new row values 0
                else productRow[index] = truncate(product,5,false);
                index++;
            }
            
            System.out.print("PRODUCT ROW = [");
            printRow(productRow);
            System.out.println("]");

            //creates new row array
            double[] newRow = new double[matrix.length+1];
            int index2 = 0;
            for(double value : matrix[rowCurr]){                           
                BigDecimal coefficient = new BigDecimal(Double.toString(value));
                BigDecimal product = new BigDecimal(Double.toString(productRow[index2]));
                BigDecimal rowDiff = coefficient.subtract(product);
                newRow[index2] = truncate(rowDiff,5,false);
                index2++;
            }

            System.out.print("NEW ROW = [");
            printRow(newRow);
            System.out.println("]");   
            
            //update matrix with new row
            updatedMatrix = updateMatrix(matrix,newRow,rowCurr);

            System.out.println("\nUpdated Matrix: ");
            printMatrix(updatedMatrix);
        }
        if(refRow < matrix.length-2) {  //repeat zeroing of column until last row is done
            getUpperTriangularMatrix(updatedMatrix,currIndex+1,refRow+1,column+1,row+1);
        }
        return;
    }

    //algorithm functions:
    public static double[][] pivotMatrix(double[][] matrix, int refRow, int rowCurr){
        double refValue = Math.abs(matrix[refRow][refRow]);
        int swappingIndex = 0;
        for(int x=rowCurr;x<matrix.length;x++){
            double checkerValue = Math.abs(matrix[x][refRow]);    //column values after reference row
            if(checkerValue > refValue) {                    
                refValue = checkerValue;
                swappingIndex = x;
            }
        }
        if(swappingIndex != 0) matrix = swapRows(matrix,refRow,swappingIndex); //swap rows
        return matrix;
    }

    public static double[][] swapRows(double[][] matrix, int row1, int row2){
        double[] tempRow = new double[matrix.length+1];
        int index = 0;
        for(double value : matrix[row1]){   //get row values from row that needs swapping
            tempRow[index] = value;
            index++;
        }
        for(int x=0;x<=matrix.length;x++){  
            matrix[row1][x] = matrix[row2][x];  //replace values in reference row with swapped row values
            matrix[row2][x] = tempRow[x];   //replace swapping row with reference row values
        }
        System.out.println("\nPivoted Matrix: ");
        printMatrix(matrix);
        return matrix;
    }

    public static double[][] updateMatrix(double[][] origMatrix, double[] newRow, int row){
        for(int x=0;x<=origMatrix.length;x++) origMatrix[row][x] = newRow[x];
        return origMatrix;
    }

    public static void printRow(double[] array){
        int index = 0;
        for(double value : array){
            BigDecimal valueBd = new BigDecimal(Double.toString(value));
            if(index!=array.length-1) System.out.printf("%-12s",truncate(valueBd,5,false));
            else {
                System.out.printf(":%6s","");
                System.out.printf("%s",truncate(valueBd,5,false));
            }
            index++;
        }
        return;
    }

    public static double[] solveMissingVars(double[][] matrix){
        double[] varArr = new double[matrix.length];
        for(int row=matrix.length-1;row>=0;row--){
            BigDecimal variable = new BigDecimal("0");
            BigDecimal sum = new BigDecimal("0");
            BigDecimal coeff = new BigDecimal(Double.toString(matrix[row][row]));    //value at the matrix index of [current row][current row]
            BigDecimal constant = new BigDecimal(Double.toString(matrix[row][matrix.length]));   //last value in the matrix (constant) at the current row
            if(row==matrix.length-1){   //compute last row first (with 1 missing var)
                variable = constant.divide(coeff,MathContext.DECIMAL64);    //constant / coefficient
            } else {
                for(int col=0;col<matrix.length;col++){
                    BigDecimal term = new BigDecimal(Double.toString(matrix[row][col] * varArr[col]));
                    sum = sum.add(term);    //sum += term
                }
                variable = (constant.subtract(sum)).divide(coeff,MathContext.DECIMAL64);  //(constant - sum) / coefficient
            }
            varArr[row] = truncate(variable,5,false);;
        }
        return varArr;
    }

    public static double truncate(BigDecimal value, int trunLimit, boolean reverse){
        if(value.doubleValue() != 0) {
            if(!reverse){
                if(value.doubleValue() > 0) value = value.setScale(trunLimit,RoundingMode.FLOOR);   //FLOOR rounds to negative infinity
                else value = value.setScale(trunLimit,RoundingMode.CEILING);    //CEILING rounds to positive infinity
            } else {
                if(value.doubleValue() > 0) value = value.setScale(trunLimit,RoundingMode.CEILING);
                else value = value.setScale(trunLimit,RoundingMode.FLOOR);
            }
        }
        return value.doubleValue();
    }

}