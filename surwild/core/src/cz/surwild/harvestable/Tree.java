package cz.surwild.harvestable;

import java.awt.Point;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tree {
	
	private TextureRegion texture;
	private Point position;
	private boolean dead;
	
	public Tree(TextureRegion tex, int x, int y) {
		texture = tex;
		position = new Point(x,y);
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(texture,position.x, position.y);
	}

	public Point getPosition() {
		return position;
	}

	public void destroy() {
		dead = true;
	}

	public boolean isDead() {
		return dead;
	}
}
