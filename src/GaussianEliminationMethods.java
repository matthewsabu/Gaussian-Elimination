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

    public double[][] setMatrix(Vector<Vector<Double>> coeffVec, int matrixSize){
        double[][] matrix = new double[matrixSize][matrixSize+1];
            for (int row=0;row<matrixSize;row++){
                int index = matrixSize;
                for (int col=0;col<=matrixSize;col++){
                    matrix[row][col] = coeffVec.get(row).get(index);
                    index--;
                }
            }
        return matrix;
    }

    public void printMatrix(double[][] matrix) {
        for(int x=0;x<matrix.length;x++){
            for(int y=0;y<=matrix.length;y++){
                if (y!=matrix.length) System.out.printf("%-12s",matrix[x][y]);
                else System.out.printf(":%12s",matrix[x][y]);
            }
            System.out.println();
        }
        return;
    }

    public double[][] updateMatrix(double[][] origMatrix, double[] newRow, int row){
        for(int x=0;x<=origMatrix.length;x++) origMatrix[row][x] = newRow[x];
        return origMatrix;
    }

    public void printArray(double[] array){
        for(int x=0;x<array.length;x++) {
            if (x!=array.length-1) System.out.printf("%-12s",array[x]);
            else System.out.printf(":%12s",array[x]);
        }
        return;
    }

    public double[] solveMissingVars(double[][] matrix){
        double[] varArr = new double[matrix.length];
        for(int row=matrix.length-1;row>=0;row--){
            double sum = 0,variable = 0,coeff = matrix[row][row],zVal = matrix[row][matrix.length];
            DecimalFormat df = new DecimalFormat("0.00000");
            if(row==matrix.length-1){
                variable = zVal / coeff;
            } else {
                for(int col=0;col<matrix.length;col++){
                    sum += matrix[row][col] * varArr[col];
                }
                variable = (zVal - sum) / coeff;
            }
            if(variable > 0) df.setRoundingMode(RoundingMode.FLOOR);
            else df.setRoundingMode(RoundingMode.CEILING);
            double truncatedVar = Double.parseDouble(df.format(variable));
            varArr[row] = truncatedVar;
        }
        return varArr;
    }
}
