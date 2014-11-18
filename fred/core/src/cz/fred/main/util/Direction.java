package cz.fred.main.util;

import com.badlogic.gdx.math.Vector2;


public class Direction {
	public static final Vector2 NONE = new Vector2(0, 0);
	public static final Vector2 UP = new Vector2(0, 1);
	public static final Vector2 DOWN = new Vector2(0, -1);
	public static final Vector2 LEFT = new Vector2(-1, 0);
	public static final Vector2 RIGHT = new Vector2(1, 0);
	
	public static int getOffset(Vector2 direction, int currentDirectionIndex) {
		float angle = getAngle(currentDirectionIndex);
		double diff = Math.abs(direction.angleRad() - angle);
		if(diff > Math.PI)
			diff = Math.abs(diff - 2*Math.PI);
		if(diff < 0.45f*Math.PI)
			return currentDirectionIndex;
		
		if(Math.abs(direction.x) > Math.abs(direction.y)) {
			if(direction.x > 0)
				return 1;
			else
				return 3;
		} else {
			if(direction.y > 0)
				return 0;
			else
				return 2;			
		}
	}

	private static float getAngle(int currentDirectionIndex) {
		switch(currentDirectionIndex) {
		case 0:
			return (float) (Math.PI/2);
		case 1:
			return 0f;
		case 2:
			return (float) (3*Math.PI/2);
		case 3:
			return (float) (Math.PI);
		default:
			return (float) (3*Math.PI/4);
		}
	}

	public static Vector2 getDirection(Vector2 enemyVec) {
		if(Math.abs(enemyVec.x) > Math.abs(enemyVec.y)) {
			if(enemyVec.x > 0)
				return RIGHT.cpy();
			else
				return LEFT.cpy();
		} else {
			if(enemyVec.y > 0)
				return UP.cpy();
			else
				return DOWN.cpy();
		}
	}
}
