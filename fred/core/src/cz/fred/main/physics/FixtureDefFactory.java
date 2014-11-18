package cz.fred.main.physics;

import cz.fred.main.map.Map;

public class FixtureDefFactory {
	
	private static FixtureDefinitionBox gemDef;
	private static FixtureDefinitionBox heroDef;
	private static FixtureDefinitionBox enemyDef;

	public static void init() {
		gemDef = new FixtureDefinitionBox(32 / Map.TO_PIXEL, 12 / Map.TO_PIXEL, 1f, 0f, 0f);
		heroDef = new FixtureDefinitionBox(32 / Map.TO_PIXEL, 12 / Map.TO_PIXEL, 10f, 0f, 0f);
		enemyDef = new FixtureDefinitionBox(50 / Map.TO_PIXEL, 6 / Map.TO_PIXEL, 10f, 0f, 0f);
	}
	
	public static void dispose() {
		gemDef.dispose();
		heroDef.dispose();
	}

	public static FixtureDefinition getGemDef() {
		return gemDef;
	}

	public static FixtureDefinition getHeroDef() {
		return heroDef;
	}
	
	public static FixtureDefinition getEnemyDef() {
		return enemyDef;
	}

}
