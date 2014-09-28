package cz.roller.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;;

public class EndStage extends Stage {
	
	private Skin skin;
	private Window window;
	
	private int width = 400;
	private int height = 240;
	private Label damageLabel;
	private Label damageValue;
	private Label lostLimbsLabel;
	private Label listLimbsValue;
	private Label timeLabel;
	private Label timeValue;

	public EndStage() {
		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), new TextureAtlas("ui/atlas.pack"));
		window = new Window("Score", skin);
		
		window.setSize(400, 240);
		window.setColor(1,1,1,0);
		window.setCenterPosition(Gdx.graphics.getWidth()/2, -500);
		window.addAction(sequence(parallel(moveTo(Gdx.graphics.getWidth()/2-window.getWidth()/2, Gdx.graphics.getHeight()/2-window.getHeight()/2,1),fadeIn(1)),run(new Runnable() {
			public void run() {
				doActions();
			}
		})));
		addActor(window);
		
		damageLabel = new Label("Damage",skin);
		damageLabel.setPosition(-80, 180);
		damageLabel.setColor(1, 1, 1, 0);
		window.addActor(damageLabel);
		
		damageValue = new Label("100",skin,"red");
		damageValue.setPosition(width-damageValue.getWidth()-20+100, 180);
		window.addActor(damageValue);
		
		lostLimbsLabel = new Label("Lost limbs",skin);
		lostLimbsLabel.setPosition(-220, 130);
		window.addActor(lostLimbsLabel);
		listLimbsValue = new Label("2",skin,"red");
		listLimbsValue.setPosition(width-listLimbsValue.getWidth()-20+100, 130);
		window.addActor(listLimbsValue);
		
		timeLabel = new Label("Time",skin);
		timeLabel.setPosition(-90, 80);
		window.addActor(timeLabel);
		timeValue = new Label("300",skin,"red");
		timeValue.setPosition(width-timeValue.getWidth()-20+100, 80);
		window.addActor(timeValue);
	}
	
	private void doActions() {
		damageLabel.addAction(parallel(moveTo(20, 180,0.5f),fadeIn(0.5f)));
		damageValue.addAction(parallel(moveTo(width-damageValue.getWidth()-20, 180,0.5f),fadeIn(0.5f)));
		
		lostLimbsLabel.addAction(parallel(moveTo(20, 130,0.5f),fadeIn(0.5f)));
		listLimbsValue.addAction(parallel(moveTo(width-listLimbsValue.getWidth()-20, 130,0.5f),fadeIn(0.5f)));
		
		timeLabel.addAction(parallel(moveTo(20, 80,0.5f),fadeIn(0.5f)));
		timeValue.addAction(parallel(moveTo(width-timeValue.getWidth()-20, 80,0.5f),fadeIn(0.5f)));
	}
	
	@Override
	public void act() {
		super.act();
	}

}
