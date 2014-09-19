package cz.roller.game.track;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SupportPole {

	private Sprite top;
	private Sprite pole;
	private float x;
	private float y;
	
	public SupportPole(float x, float y, Texture trackSupportTexture, Texture trackSupportPoleTexture) {
		this.x = x;
		this.y = y;
		top = new Sprite(trackSupportTexture);
		top.setSize(0.5f, 0.5f);
		top.setCenter(x, y-0.5f);
		pole = new Sprite(trackSupportPoleTexture);
		pole.setSize(0.2f, 20);
		pole.setPosition(x, y+20.5f);
	}
	
	public void draw(SpriteBatch batch) {
		top.draw(batch);
		pole.draw(batch);
		
		top.setCenter(x, y-0.7f);
		pole.setPosition(x-0.1f, y-20.7f);
	}

}
