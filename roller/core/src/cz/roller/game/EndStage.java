package cz.roller.game;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import cz.roller.game.level.Level;
import cz.roller.game.util.AssetManager;

public class EndStage extends Stage implements InputProcessor{
	
	private Skin skin;
	private Window window;
	
	private int width = 400;
	@SuppressWarnings("unused")
	private int height = 240;
	private Label damageLabel;
	private Label damageValue;
	private Label lostLimbsLabel;
	private Label listLimbsValue;
	private Label timeLabel;
	private Label timeValue;
	private Texture star;
	private Texture starEarned;
	private Image starEarned1;
	private Image starEarned2;
	private Image starEarned3;
	private Level level;
	private EndGameListener listener;

	public EndStage(Level level, final EndGameListener listener) {
		this.level = level;
		this.listener = listener;
		
		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), new TextureAtlas("ui/atlas.pack"));
		window = new Window("Score", skin);
		
		star = AssetManager.getTexture("result/star.png");
		starEarned = AssetManager.getTexture("result/starEarned.png");
		
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
		
		lostLimbsLabel = new Label("Lost limbs",skin);
		lostLimbsLabel.setPosition(-220, 130);
		window.addActor(lostLimbsLabel);
		
		timeLabel = new Label("Time",skin);
		timeLabel.setPosition(-90, 80);
		window.addActor(timeLabel);
		
		Image star1 = new Image(star);
		star1.setColor(1, 1, 1, 0.5f);
		star1.setCenterPosition(100, star1.getHeight()/2+10);
		window.addActor(star1);
		Image star2 = new Image(star);
		star2.setColor(1, 1, 1, 0.5f);
		star2.setCenterPosition(200, star1.getHeight()/2+10);
		window.addActor(star2);
		Image star3 = new Image(star);
		star3.setColor(1, 1, 1, 0.5f);
		star3.setCenterPosition(300, star1.getHeight()/2+10);
		window.addActor(star3);
		
		starEarned1 = new Image(starEarned);
		starEarned1.setCenterPosition(100, -200);
		window.addActor(starEarned1);
		starEarned2 = new Image(starEarned);
		starEarned2.setCenterPosition(200, -200);
		window.addActor(starEarned2);
		starEarned3 = new Image(starEarned);
		starEarned3.setCenterPosition(300, -200);
		window.addActor(starEarned3);
		
		Button endButton = new Button(skin, "end");
		endButton.setCenterPosition(Gdx.graphics.getWidth()/2, -500);
		addActor(endButton);
		endButton.addAction(sequence(parallel(moveTo(Gdx.graphics.getWidth()/2-endButton.getWidth()/2, Gdx.graphics.getHeight()/2-window.getHeight()/2 - endButton.getHeight() - 10,1),fadeIn(1))));
		endButton.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				listener.endGame();
			}
		});
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(Keys.ENTER == keycode
				|| Keys.ESCAPE == keycode) {
			listener.endGame();
		}
		return false;
	}
	
	private void doActions() {
		Gdx.input.setInputProcessor(this);
		
		damageValue = new Label(""+ScoreManager.getDamage(),skin,"red");
		damageValue.setPosition(width-damageValue.getWidth()-20+200, 180);
		window.addActor(damageValue);
		damageLabel.addAction(parallel(moveTo(20, 180,0.5f),fadeIn(0.5f)));
		damageValue.addAction(parallel(moveTo(width-damageValue.getWidth()-20, 180,0.5f),fadeIn(0.5f)));
		
		listLimbsValue = new Label(""+ScoreManager.getLostLimbs(),skin,"red");
		listLimbsValue.setPosition(width-listLimbsValue.getWidth()-20+200, 130);
		window.addActor(listLimbsValue);
		lostLimbsLabel.addAction(parallel(moveTo(20, 130,0.5f),fadeIn(0.5f)));
		listLimbsValue.addAction(parallel(moveTo(width-listLimbsValue.getWidth()-20, 130,0.5f),fadeIn(0.5f)));
		
		timeValue = new Label(""+ScoreManager.getTime(),skin,"red");
		timeValue.setPosition(width-timeValue.getWidth()-20+200, 80);
		window.addActor(timeValue);
		timeLabel.addAction(parallel(moveTo(20, 80,0.5f),fadeIn(0.5f)));
		timeValue.addAction(parallel(moveTo(width-timeValue.getWidth()-20, 80,0.5f),fadeIn(0.5f)));

		window.validate();
		
		int earnedStars = 0;
		if(ScoreManager.getLostLimbs() < 2
				&& ScoreManager.getDamageValue() < 50
				&& ScoreManager.getTimeValue() < level.getTimes()[0])
			earnedStars = 3;
		else if(ScoreManager.getLostLimbs() < 8
				&& ScoreManager.getDamageValue() < 300
				&& ScoreManager.getTimeValue() < level.getTimes()[1])
			earnedStars = 2;
		else if(ScoreManager.getLostLimbs() < 15
				&& ScoreManager.getDamageValue() < 600
				&& ScoreManager.getTimeValue() < level.getTimes()[2])
			earnedStars = 1;
		
		if(earnedStars >= 1)
			starEarned1.addAction(sequence(delay(0.5f),moveTo(starEarned1.getX(), 10, 0.5f)));
		if(earnedStars >= 2)
			starEarned2.addAction(sequence(delay(0.5f),moveTo(starEarned2.getX(), 10, 0.5f)));
		if(earnedStars >= 3)
			starEarned3.addAction(sequence(delay(0.5f),moveTo(starEarned3.getX(), 10, 0.5f)));
		
		if(level.getEarnedStars() < earnedStars)
			level.setEarnedStars(earnedStars);
	}
	
	@Override
	public void act() {
		super.act();
	}

}
