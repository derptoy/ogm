package cz.roller.game.track;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import cz.roller.game.world.Settings;

public class SupportPole {

	private Sprite top;
	private Sprite pole;
	private float x;
	private float y;
	private Sprite base;
	
	public SupportPole(float x, float y, Texture trackSupportTexture, Texture trackSupportPoleTexture, Texture trackSupportPoleBase) {
		this.x = x;
		this.y = y;
		top = new Sprite(trackSupportTexture);
		top.setSize(Settings.TO_PIXELS*0.5f, Settings.TO_PIXELS*0.5f);
		pole = new Sprite(trackSupportPoleTexture);
		pole.setSize(Settings.TO_PIXELS*0.2f, Settings.TO_PIXELS*y-60);
		
		base = new Sprite(trackSupportPoleBase);
		base.setSize(Settings.TO_PIXELS*0.5f, Settings.TO_PIXELS*0.5f);
	}
	
	public void draw(SpriteBatch batch) {
		top.setCenter(		Settings.TO_PIXELS*x, 
				Settings.TO_PIXELS*(y-0.7f));
		pole.setPosition(	Settings.TO_PIXELS*(x-0.1f),
							Settings.TO_PIXELS*(y-0.7f-pole.getHeight()/Settings.TO_PIXELS));
		base.setCenter(		Settings.TO_PIXELS*x, 
							Settings.TO_PIXELS*(y-0.7f-pole.getHeight()/Settings.TO_PIXELS));
		
		top.draw(batch);
		pole.draw(batch);
		base.draw(batch);
	}

}
