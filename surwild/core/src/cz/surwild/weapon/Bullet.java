package cz.surwild.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import cz.surwild.main.Map;
import cz.surwild.util.Point2D;


public class Bullet {
	
	private Vector2 position = new Vector2();
	private Point2D direction = new Point2D();
	private Texture texture;
	
	public Bullet(Texture texture2) {
		texture = texture2;
	}

	public void update() {
		position.x += Gdx.graphics.getDeltaTime() * direction.x * 200f;
		position.y += Gdx.graphics.getDeltaTime() * direction.y * 200f;
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(texture, position.x - texture.getWidth()/2, position.y - texture.getHeight()/2);
	}

	public int getIndexX() {
		return (int)position.x/Map.TILE_SIZE;
	}

	public int getIndexY() {
		return (int)position.y/Map.TILE_SIZE;
	}

	public void setPosition(Vector2 origin) {
		position.set(origin);
	}

	public void setDirection(Point2D direction2) {
		direction.set(direction2.x, direction2.y);
	}

	public Vector2 getPosition() {
		return position;
	}

}
