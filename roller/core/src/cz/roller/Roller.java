package cz.roller;

import com.badlogic.gdx.Game;

import cz.roller.game.MainGame;
import cz.roller.game.util.AssetManager;

public class Roller extends Game {

	public static final String TITLE = "Roller", VERSION = "0.0.0.0.reallyEarly";

	@Override
	public void create() {
		AssetManager.init();
		setScreen(new MainGame(this));
		System.out.println(System.currentTimeMillis()+" CREATE");
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

}