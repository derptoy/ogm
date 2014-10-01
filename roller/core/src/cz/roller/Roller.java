package cz.roller;

import com.badlogic.gdx.Game;

import cz.roller.game.level.LevelsData;
import cz.roller.game.menu.MainMenu;
import cz.roller.game.util.AssetManager;

public class Roller extends Game {

	public static final String TITLE = "Roller", VERSION = "0.0.0.0.reallyEarly";

	@Override
	public void create() {
		AssetManager.init();
		LevelsData.init();
		setScreen(new MainMenu(this));
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