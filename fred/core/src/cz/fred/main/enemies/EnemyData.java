package cz.fred.main.enemies;

import com.badlogic.gdx.math.Vector2;

import cz.fred.main.map.Map;
import cz.fred.main.physics.SpriteBody;
import cz.fred.main.util.Direction;

public class EnemyData {
	private Vector2 direction = Direction.DOWN;
	private Vector2 move = Direction.NONE;
	private SpriteBody body;
	
	public Vector2 getDirection() {
		return direction;
	}

	public void setDirection(Vector2 direction) {
		this.direction = direction;
	}

	public void setMove(Vector2 move) {
		this.move = move;
	}

	public Vector2 getMove() {
		return move;
	}

	public void setBody(SpriteBody body) {
		this.body = body;
	}

	public SpriteBody getBody() {
		return body;
	}
	
	public float getX() {
		return body.getPosition().x * Map.TO_PIXEL;
	}

	public float getY() {
		return body.getPosition().y * Map.TO_PIXEL;
	}
	
}
