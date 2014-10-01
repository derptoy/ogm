package cz.roller.game;

public class ScoreManager {

	private static float damage;
	private static float time;
	private static int lostLimbs;
	
	public static void reset() {
		damage = 0;
		time = 0;
		lostLimbs = 0;
	}

	public static void updateTime(float deltaTime) {
		time += deltaTime;
	}
	
	public static void lostLimb() {
		lostLimbs++;
	}
	
	public static void doneDamage(float ll) {
		damage += ll;
	}

	public static float getDamageValue() {
		return damage;
	}
	
	public static String getDamage() {
		return String.format("%4.0f", damage);
	}

	public static float getTimeValue() {
		return time;
	}

	
	public static String getTime() {
		return String.format("%4.2f", time);
	}

	public static int getLostLimbs() {
		return lostLimbs;
	}
	
	
}
