package cz.surwild.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import cz.surwild.main.Assets;

public class Popup extends Window implements InputProcessor {
	
	private List<String> list;
	private InputProcessor previousInputProcessor;

	public Popup() {
		super("",Assets.getUISkin());
		this.setSize(110, 110);
		this.setMovable(false);
//		this.setDebug(true);
		
		list = new List<String>(Assets.getUISkin());
		list.setItems("Process","Use","Drop");
		list.setSelectedIndex(-1);
		list.setSize(100, 60);
		list.layout();
		
		
		this.add(list).pad(5);
		this.validate();
		this.pack();
		this.setVisible(false);
	}
	
	public void show(float x, float y) {
		this.setPosition(x/*-getWidth()/2*/, y/*-getHeight()/2*/);
		this.setVisible(true);
		list.setSelectedIndex(0);
		
		previousInputProcessor = Gdx.input.getInputProcessor();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public boolean keyDown(int keycode) {
		if(Keys.W == keycode) {
			int index = list.getSelectedIndex()-1;
			if(index < 0)
				index = list.getItems().size-1;
			list.setSelectedIndex(index);
		} else if(Keys.S == keycode) {
			int index = list.getSelectedIndex()+1;
			if(index > list.getItems().size-1)
				index = 0;
			list.setSelectedIndex(index);
		} else if(Keys.F == keycode) {
			//Selected
			System.out.println("CLICKED "+list.getSelected());
			setVisible(false);
			Gdx.input.setInputProcessor(previousInputProcessor);
		} else if(Keys.ESCAPE == keycode
					|| Keys.A == keycode
					|| Keys.D == keycode) {
			setVisible(false);
			Gdx.input.setInputProcessor(previousInputProcessor);
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
