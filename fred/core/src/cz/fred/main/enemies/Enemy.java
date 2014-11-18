package cz.fred.main.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

import cz.fred.main.graphics.Drawable;
import cz.fred.main.map.Map;
import cz.fred.main.physics.FixtureDefFactory;
import cz.fred.main.physics.SpriteBody;
import cz.fred.main.util.Direction;

public class Enemy implements Drawable {
	
	public enum State {IDLE, MOVE, CHASE, ATTACK, FEAR};
	private State state = State.IDLE;
	private float stateWaitTime = 100;
	private float stateWaitTimeTarget = 100;
	
	private EnemyGraphics graphics;
	private EnemyData data;
	private Map map;
	
	private SpriteBody body;
	private World world;
	private Vector2 moveTarget;
	
	public Enemy(World world, Map map) {
		this.map = map;
		this.world = world;
		
		data = new EnemyData();
		graphics = new EnemyGraphics(data);
		
		body = new SpriteBody(world, FixtureDefFactory.getEnemyDef());
		data.setBody(body);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		graphics.draw(batch);
	}

	public void setPosition(int x, int y) {
		body.setPosition(x+graphics.getWidth()/2f, y+graphics.getHeight()/2f - 16);
	}

	public void update() {
		if(state != State.IDLE)
			graphics.update();
		
		stateWaitTime += Gdx.graphics.getDeltaTime();
		
		move();
	}
	
	private void move() {
		if(moveTarget != null) {
			Vector2 enemyVec = new Vector2(getX(), getY());
			Vector2 diff = moveTarget.cpy().sub(enemyVec);
			
			
			Vector2 dir = diff.cpy();
			dir.nor();
			dir.scl(0.2f);
			setDirection(dir);
			
			if(diff.len() < 2 
					|| (stateWaitTime > 10 && state == State.MOVE)) {
				state = State.IDLE;
				moveTarget = null;
				stateWaitTime = 0;
				stateWaitTimeTarget = (float)Math.random()*10;
			}
		}
	}

	@Override
	public int compareTo(Drawable o) {
		float diff = getComparingY() - o.getComparingY();
		if(diff > 0)
			return 1;
		else if( diff < 0)
			return -1;
		else return 0;
	}

	@Override
	public float getComparingY() {
		return body.getPosition().y;
	}
	
	public float getX() {
		return body.getPosition().x * Map.TO_PIXEL;
	}

	public float getY() {
		return body.getPosition().y * Map.TO_PIXEL;
	}

	public void setDirection(Vector2 dir) {
		data.setMove(dir);
		data.setDirection(dir);
		body.applyForce(dir);
	}

	public float getStateWaitTime() {
		return stateWaitTime;
	}

	public void setStateWaitTime(float stateWaitTime) {
		this.stateWaitTime = stateWaitTime;
	}

	public float getStateWaitTimeTarget() {
		return stateWaitTimeTarget;
	}

	public void setStateWaitTimeTarget(float stateWaitTimeTarget) {
		this.stateWaitTimeTarget = stateWaitTimeTarget;
	}

	public State getState() {
		return state;
	}

	public void setState(State move) {
		state = move;
	}

	public void setTarget(Vector2 target) {
		moveTarget = target;
	}

	public Fixture getFixture() {
		return body.getFixture();
	}
}
