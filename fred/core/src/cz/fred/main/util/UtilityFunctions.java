package cz.fred.main.util;

import java.awt.Point;

public class UtilityFunctions {
	
	public static CustomRect EMPTY_RECT = new CustomRect("empty", 0, 0, 0, 0);
	
	public static Point[] getSurrounding(int xpart, int ypart) {
		Point[] points = new Point[9];
		points[0] = new Point(xpart, ypart);
		points[1] = new Point(xpart-1, ypart-1);
		points[2] = new Point(xpart, ypart-1);
		points[3] = new Point(xpart+1, ypart-1);
		points[4] = new Point(xpart-1, ypart);
		points[5] = new Point(xpart+1, ypart);
		points[6] = new Point(xpart-1, ypart+1);
		points[7] = new Point(xpart, ypart+1);
		points[8] = new Point(xpart+1, ypart+1);
		
		return points;
	}
	
	public static Point[] getSurroundingDirect(int xpart, int ypart) {
		Point[] points = new Point[4];
		points[0] = new Point(xpart, ypart-1);
		points[1] = new Point(xpart-1, ypart);
		points[2] = new Point(xpart+1, ypart);
		points[3] = new Point(xpart, ypart+1);
		
		return points;
	}
	
	public static Point[] getSurroundingDiagonal(int xpart, int ypart) {
		Point[] points = new Point[4];
		points[0] = new Point(xpart-1, ypart-1);
		points[1] = new Point(xpart+1, ypart-1);
		points[2] = new Point(xpart+1, ypart+1);
		points[3] = new Point(xpart-1, ypart+1);
		
		return points;
	}

	public static void printMatrix(float[][] lightValues) {
		for(int i=0;i<lightValues[0].length;i++) {
			for(int j=0;j<lightValues.length;j++) {
				System.out.format("%4.1f ", lightValues[j][i]);
//				System.out.print(lightValues[j][i]+" ");
			}
			System.out.println();
		}
	}

}
