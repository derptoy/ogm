package cz.surwild.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import cz.surwild.harvestable.Tree;
import cz.surwild.ui.Popup;

public class Inventory extends Actor {
	
	private Texture background;
	private Texture selected;
	private int offsetIndexX = 0;
	private int offsetIndexY = 0;
	private Popup popup;
	
	private Tree[][] slots;
	
	public void load(Popup popup) {
		this.popup = popup;
		slots = new Tree[3][3];
		
		background = new Texture(Gdx.files.internal("inventory/inventory.png"));
		selected = new Texture(Gdx.files.internal("inventory/select.png"));
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		batch.draw(background, 	Gdx.graphics.getWidth()/2 - background.getWidth()/2,
								Gdx.graphics.getHeight()/2 - background.getHeight()/2);
		
		int offsetx, offsety;
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				if(slots[i][j] != null) {
					offsetx = -2 + (j-1)*32;
					offsety = 2 - (i-1)*32;
					batch.draw(slots[i][j].getTexture(), 	Gdx.graphics.getWidth()/2 + offsetx - slots[i][j].getTexture().getRegionWidth()/2,
															Gdx.graphics.getHeight()/2 + offsety - slots[i][j].getTexture().getRegionHeight()/2);
				}
			}
		}
		
		offsetx = -2 + offsetIndexX*32;
		offsety = 2 - offsetIndexY*32;
		batch.draw(selected, 	Gdx.graphics.getWidth()/2 + offsetx - selected.getWidth()/2,
								Gdx.graphics.getHeight()/2 + offsety - selected.getHeight()/2);
	}

	public void keyDown(int keycode) {
		if(Keys.W == keycode) {
			offsetIndexY--;
		} else if(Keys.S == keycode) {
			offsetIndexY++;
		} else if(Keys.A == keycode) {
			offsetIndexX--;
		} else if(Keys.D == keycode) {
			offsetIndexX++;
		} else if(Keys.F == keycode) {
			int offsetx = offsetIndexX*32 - 16;
			int offsety = - offsetIndexY*32 + 16;
			popup.show(Gdx.graphics.getWidth()/2 + offsetx, Gdx.graphics.getHeight()/2 + offsety);
		}
		
		if(offsetIndexX > 2)
			offsetIndexX = 0;
		if(offsetIndexX < 0)
			offsetIndexX = 2;
		if(offsetIndexY > 2)
			offsetIndexY = 0;
		if(offsetIndexY < 0)
			offsetIndexY = 2;
	}

	public boolean addItem(Tree harvest) {
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				if(slots[i][j] == null) {
					slots[i][j] = harvest;
					return true;
				}
					
			}
		}
		
		return false;
	}
}
