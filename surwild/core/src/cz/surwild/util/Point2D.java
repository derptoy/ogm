package cz.surwild.util;

public class Point2D implements Cloneable {
	public int x;
	public int y;
	
	public Point2D(int x2, int y2) {
		x = x2;
		y = y2;
	}

	public Point2D(Point2D orig) {
		x = orig.x;
		y = orig.y;
	}

	public Point2D() {
	}

	@Override
	public Point2D clone() {
		return new Point2D(x, y);
	}

	public void set(int x2, int y2) {
		x = x2;
		y = y2;
	}
}
