package cz.roller.game.menu;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import cz.roller.game.MainGame;
import cz.roller.game.level.Level;
import cz.roller.game.level.LevelsData;

public class LevelSelector implements Screen {
	
	private Stage stage;
	private Skin skin;
	private Button level1;
	private Game game;
	private TextButton exit;
	private Image logo;

	public LevelSelector(Game game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(157/255.0f, 249/255.0f, 255/255.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		stage = new Stage();
		
		Gdx.input.setInputProcessor(stage);
		
		System.out.println(Gdx.files.internal("ui/menuSkin.json").file().getAbsolutePath());
		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), new TextureAtlas("ui/atlas.pack"));
		
		Texture tex = new Texture(Gdx.files.internal("ui/menu/selectLabel.png"));
        tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		logo = new Image(tex);
		logo.setPosition(50, 0.8f*stage.getHeight() );
		stage.addActor(logo);
		
		HorizontalGroup group = new HorizontalGroup();
		group.setPosition(200, 0.5f*stage.getHeight());
		stage.addActor(group);
		
		Texture smallStar = new Texture(Gdx.files.internal("ui/menu/smallStar.png"));
		for(final Level lvl:LevelsData.getLevels()) {
			TextButton level1 = new TextButton(lvl.getName(),skin,"select");
			level1.getLabel().setColor(Color.RED);
			level1.getLabel().setAlignment(Align.top);
//	        level1.setCenterPosition(0, 0.5f*stage.getHeight());
	        level1.addListener(new ChangeListener() {
				
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					stage.addAction(sequence(moveTo(-stage.getWidth(),0,0.4f),run(new Runnable() {
						public void run() {
							System.out.println("RUN GAME");
							game.setScreen(new MainGame(game,lvl));
						}
					})));
				}
			});
	        
	        if(lvl.getEarnedStars() >=1) {
	        	Image image = new Image(smallStar);
	        	image.setCenterPosition(47, 31);
	        	level1.addActor(image);
	        } 
	        if(lvl.getEarnedStars() >=2) {
	        	Image image = new Image(smallStar);
	        	image.setCenterPosition(61, 31);
	        	level1.addActor(image);
	        }
	        if(lvl.getEarnedStars() >=3) {
	        	Image image = new Image(smallStar);
	        	image.setCenterPosition(73, 31);
	        	level1.addActor(image);
	        }
	        
	        group.addActor(level1);
		}
        
        exit = new TextButton("Exit",skin);
        exit.setPosition(50, 50);
        exit.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				stage.addAction(sequence(moveTo(0,stage.getHeight(),0.4f),run(new Runnable() {
					public void run() {
						game.setScreen(new MainMenu(game));
					}
				})));
				
			}
		});
        stage.addActor(exit);
        
        stage.addAction(sequence(moveTo(0, stage.getHeight()),moveTo(0, 0, 0.4f)));
        LevelsData.save();
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
