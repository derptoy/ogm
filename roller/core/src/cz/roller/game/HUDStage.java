package cz.roller.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import cz.roller.game.level.Level;

public class HUDStage extends Stage {
	
	private Skin skin;
	private Label time;
	private Level level;
	
	public HUDStage(Level level) {
		this.level = level;
		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), new TextureAtlas("ui/atlas.pack"));
		
		time = new Label("0/"+level.getTimes()[0], skin,"white");
		time.setColor(0, 0, 0, 1);
		time.setPosition(10, Gdx.graphics.getHeight()-42);
		addActor(time);
	}
	
	public void update() {
		time.setText(""+ScoreManager.getTime()+"/"+level.getTimes()[0]);
		
		if(ScoreManager.getTimeValue() > level.getTimes()[0]
				&& ScoreManager.getTimeValue() < level.getTimes()[1])
			time.setColor(new Color(1.0f, 0.7f, 0.0f, 1));
		if(ScoreManager.getTimeValue() > level.getTimes()[1])
			time.setColor(new Color(1, 0.2f, 0.2f, 1));
	}

}
