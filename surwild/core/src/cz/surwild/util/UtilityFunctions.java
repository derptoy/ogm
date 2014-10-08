package cz.surwild.util;

import java.awt.Point;

public class UtilityFunctions {
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

	public static Point[] getSurroundingX(int xpart, int ypart, float direction) {
		if(direction > 0) {
			Point[] points = new Point[3];
			points[0] = new Point(xpart+1, ypart-1);
			points[1] = new Point(xpart+1, ypart);
			points[2] = new Point(xpart+1, ypart+1);
			return points;
		} else if(direction < 0) {
			Point[] points = new Point[3];
			points[0] = new Point(xpart-1, ypart-1);
			points[1] = new Point(xpart-1, ypart);
			points[2] = new Point(xpart-1, ypart+1);
			return points;
		} else
			return new Point[0];
	}

	public static Point[] getSurroundingY(int xpart, int ypart, float direction) {
		if(direction > 0) {
			Point[] points = new Point[3];
			points[0] = new Point(xpart-1, ypart+1);
			points[1] = new Point(xpart, ypart+1);
			points[2] = new Point(xpart+1, ypart+1);
			return points;
		} else if(direction < 0) {
			Point[] points = new Point[3];
			points[0] = new Point(xpart-1, ypart-1);
			points[1] = new Point(xpart, ypart-1);
			points[2] = new Point(xpart+1, ypart-1);
			return points;
		} else
			return new Point[0];
	}
}
