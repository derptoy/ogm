package cz.roller.game.menu;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.color;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Splash implements Screen {

	private Stage stage;
	private Image logo;
	private Game game;
	
	public Splash(Game game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		stage = new Stage();
		
		Gdx.input.setInputProcessor(stage);
		
		logo = new Image(new Texture(Gdx.files.internal("ui/menu/splash.png")));
		logo.setCenterPosition(stage.getWidth()/2, stage.getHeight()/2);
		logo.setColor(1, 1, 1, 0);
		
		stage.addActor(logo);
		logo.addAction(sequence(color(new Color(1,1,1,1),0.5f), delay(1f), color(new Color(1,1,1,0),0.5f), run(new Runnable() {
			
			@Override
			public void run() {
				game.setScreen(new MainMenu(game));
			}
		})));
	}

	@Override
	public void hide() {
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
