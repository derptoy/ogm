package cz.surwild.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	private HeroController heroController;
	private Map map;
	private OrthographicCamera camera;
	
	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();;

		camera = new OrthographicCamera();
		camera.setToOrtho(false,w,h);
		camera.position.x = 650;
		camera.position.y = 1100;
		camera.update();
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		
		Gdx.input.setInputProcessor(this);
		
		map = new Map(batch, camera);
		heroController = new HeroController(map);
	}

	@Override
	public void render () {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Map
		map.setProjectionMatrix(camera);
		map.render();
		
		// Hero
		batch.begin();
		heroController.render(batch);
		batch.end();
		
		heroController.processKey();
		heroController.update();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(Keys.UP == keycode) {
			camera.position.y += 50;
		} else if(Keys.DOWN == keycode) {
			camera.position.y -= 50;
		} else if(Keys.LEFT == keycode) {
			camera.position.x -= 50;
		} else if(Keys.RIGHT == keycode) {
			camera.position.x += 50;
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
