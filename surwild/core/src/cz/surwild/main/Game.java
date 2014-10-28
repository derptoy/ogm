package cz.surwild.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import cz.surwild.ui.Popup;
import cz.surwild.weapon.BulletManager;

public class Game extends ApplicationAdapter implements InputProcessor {
	private SpriteBatch batch;
	private HeroController heroController;
	private Map map;
	private OrthographicCamera camera;
	private Inventory inventory;
	private Stage ui;
	private OrthographicCamera uicamera;
	private boolean inventoryVisible;
	private BulletManager bulletManager;
	private Popup popup;
	private InputMultiplexer mainInput;
	
	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();;
		
		Assets.init();

		camera = new OrthographicCamera();
		camera.setToOrtho(false,w,h);
		camera.position.x = 1300;
		camera.position.y = 2200;
		camera.zoom = 1.0f;
		camera.update();
		
		ui = new Stage();
		
		uicamera = new OrthographicCamera();
		uicamera.setToOrtho(false,w,h);
		uicamera.zoom = 0.5f;
		uicamera.update();
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		
//		FreetypeFonts.init();
		
		SoundManager.init();
		
		map = new Map(batch, camera);
		inventory = new Inventory();
		bulletManager = new BulletManager(map);
		heroController = new HeroController(map, bulletManager, inventory);
		
		ui.addActor(inventory);
		popup = new Popup();
		ui.addActor(popup);
		inventory.load(popup);
		
		mainInput = new InputMultiplexer(this,ui);
		Gdx.input.setInputProcessor(mainInput);
	}

	@Override
	public void render () {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Map
		map.setProjectionMatrix(camera);
//		map.render();
		batch.begin();
		map.renderGround(batch);
		batch.end();
		
		// Hero
		batch.begin();
		heroController.render(batch);
		bulletManager.render(batch);
		batch.end();
		
		batch.begin();
		map.renderWalkable();
		map.renderCollision();
		batch.end();
		
		if(inventoryVisible) {
			ui.getViewport().setCamera(uicamera);
			ui.act();
			ui.draw();
		}
		
		if( !inventoryVisible) 
			heroController.processKey();
		heroController.update();
		
		bulletManager.update();
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
		} else if(Keys.T == keycode) {
			inventoryVisible = !inventoryVisible;
		} else if(Keys.P == keycode) {
			map.toggleDebug();
		}
		
		if(inventoryVisible)
			inventory.keyDown(keycode);
		
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
