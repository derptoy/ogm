package cz.fred.main.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Drawable extends Comparable<Drawable> {

	public void draw(SpriteBatch batch);

	public float getComparingY();
}
