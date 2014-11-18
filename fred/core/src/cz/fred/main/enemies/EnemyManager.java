package cz.fred.main.enemies;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

import cz.fred.main.enemies.Enemy.State;
import cz.fred.main.hero.Hero;
import cz.fred.main.map.Map;

public class EnemyManager {

	private ArrayList<Enemy> enemies;
	private Hero hero;
	private World world;

	public EnemyManager(Hero hero, ArrayList<Enemy> enemies, World world) {
		this.hero = hero;
		this.enemies = enemies;
		this.world = world;
	}

	public void update() {
		for(final Enemy enemy:enemies) {
			if(enemy.getState() == Enemy.State.IDLE 
					&& enemy.getStateWaitTime() > enemy.getStateWaitTimeTarget()) {
				Vector2 enemyVec = new Vector2(enemy.getX(), enemy.getY());
				
				//				for(int i=0;i<10;i++) {
				Vector2 target = enemyVec.cpy().add(	(float)(Math.random()*100-50),
														(float)(Math.random()*100-50));

				//				Vector2 target = enemyVec.cpy().add(	0,
				//						-500);
				enemy.setTarget(target);

				enemy.setState(Enemy.State.MOVE);
				enemy.setStateWaitTime(0);
				world.rayCast(new RayCastCallback() {

					@Override
					public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
						if(fixture == enemy.getFixture())
							return 1;
						else {

							enemy.setState(Enemy.State.IDLE);
							enemy.setStateWaitTime(15);
							return 0;
						}
					}
				}, enemyVec.cpy().scl(1/Map.TO_PIXEL), target.cpy().scl(1/Map.TO_PIXEL));
				//				}
		}

			if(enemy.getState() == State.CHASE) {
				enemy.setTarget(new Vector2(hero.getX(), hero.getY()));
			}
			
			enemy.update();
		}
	}

}
