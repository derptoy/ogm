package cz.roller.game.menu;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class MainMenu implements Screen {
	
	private Stage stage;
	private Skin skin;
	private Image logo;
	private Clouds clouds;
	private TextButton newGame;
	private TextButton exit;
	private Timer timer;
	private Game game;
	
	public MainMenu(Game game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(157/255.0f, 249/255.0f, 255/255.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		clouds.update();
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

		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), new TextureAtlas("ui/atlas.pack"));

        newGame = new TextButton("Newgame",skin);
        newGame.setPosition(stage.getWidth()/2 - newGame.getWidth()/2, 0.5f*stage.getHeight() - newGame.getHeight()/2);
//        newGame.setCenterPosition(-200, 0.5f*stage.getHeight());
//        MoveToAction action = new MoveToAction();
//		action.setPosition(stage.getWidth()/2 - newGame.getWidth()/2, 0.5f*stage.getHeight() - newGame.getHeight()/2);
//		action.setDuration(1);
//		action.setActor(newGame);
//		newGame.addAction(action);
		newGame.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				stage.addAction(sequence(moveTo(0,-stage.getHeight(),0.5f),run(new Runnable() {
					public void run() {
						game.setScreen(new LevelSelector(game));
					}
				})));
				
			}
		});
        stage.addActor(newGame);
        
        exit = new TextButton("Exit",skin);
        exit.setPosition(stage.getWidth()/2 - exit.getWidth()/2, 0.35f*stage.getHeight() - exit.getHeight()/2);
//        exit.setCenterPosition(stage.getWidth()+200, 0.4f*stage.getHeight());
//        action = new MoveToAction();
//		action.setPosition(stage.getWidth()/2 - exit.getWidth()/2, 0.4f*stage.getHeight() - exit.getHeight()/2);
//		action.setDuration(1);
//		action.setActor(exit);
//		exit.addAction(action);
		exit.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				dispose();
				System.exit(0);
			}
		});
        stage.addActor(exit);
		
        Texture tex = new Texture(Gdx.files.internal("ui/menu/logo.png"));
        tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		logo = new Image(tex);
		logo.setPosition(stage.getWidth()/2 - logo.getWidth()/2, 0.7f*stage.getHeight() - logo.getHeight()/2);
		stage.addActor(logo);
		
//		RotateToAction action2 = new RotateToAction();
//		action2.setRotation(0);
//		action2.setDuration(1);
//		action2.setActor(logo);
//		action = new MoveToAction();
//		action.setPosition(stage.getWidth()/2 - logo.getWidth()/2, 0.7f*stage.getHeight() - logo.getHeight()/2);
//		action.setDuration(1);
//		action.setActor(logo);
//		logo.addAction(new ParallelAction(action, action2));
		
		stage.addAction(sequence(moveTo(0, Gdx.graphics.getHeight()),moveTo(0, 0,0.4f)));
		
		clouds = new Clouds();
		clouds.init(stage);
		
		System.out.println(System.currentTimeMillis()+" SHOW");
		timer = new Timer();
		timer.scheduleTask(new Task() {
			
			@Override
			public void run() {
				logo.setOrigin(logo.getWidth()/2,logo.getHeight());
				logo.addAction(sequence(rotateTo(5,0.5f), rotateTo(-5,1), rotateTo(0,0.5f)));
			}
			
			@Override
			public void cancel() {
				
			}
		}, 5, 10);
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
		skin.dispose();
		clouds.dispose();
		timer.stop();
		timer.clear();
	}

}
