package cz.surwild.util;

public class Point2D implements Cloneable {
	public float x;
	public float y;
	
	public Point2D(float x2, float y2) {
		x = x2;
		y = y2;
	}

	public Point2D() {
	}

	@Override
	public Point2D clone() {
		return new Point2D(x, y);
	}
}
