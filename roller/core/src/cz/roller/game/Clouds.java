package cz.roller.game;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Clouds {

	private LinkedList<Image> cloudSprites;
	private Texture[] cloudTextures;
	private Stage stage;
	
	private static final int CLOUD_COUNT = 5;
	
	public void init(Stage stage) {
		this.stage = stage;
		cloudTextures = new Texture[4];
		for(int i=0;i<cloudTextures.length;i++) {
			cloudTextures[i] = new Texture(Gdx.files.internal("clouds/cloud"+i+".png"));
		}
		
		cloudSprites = new LinkedList<Image>();
		for(int i=0;i<CLOUD_COUNT;i++) {
			Image image = new Image(cloudTextures[MathUtils.random(3)]);
//			sprite.setPosition(i*300, MathUtils.random(stage.getHeight()-200));
			boolean collision = true;
			while(collision) {
				image.setPosition(MathUtils.random(stage.getWidth()+200)-200, MathUtils.random(stage.getHeight()-200));
				
				collision = checkCollision(image);
			}
			
			cloudSprites.add(image);
			stage.addActor(image);
			image.setZIndex(0);
		}
	}
	
	private boolean checkCollision(Image sprite) {
		for(Image cloud:cloudSprites) {
			if(collisionCheck(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight(), cloud.getX(), cloud.getY(), cloud.getWidth(), cloud.getHeight()))
				return true;
		}
				
		return false;
	}
	
	private boolean collisionCheck(float x, float y, float w, float h, float tx, float ty, float tw, float th) {
		float lx = Math.abs(x+w/2.0f - (tx+tw/2.0f));
		float sumx = (w / 2.0f) + (tw / 2.0f);

		float ly = Math.abs(y+h/2.0f - (ty+th/2.0f));
		float sumy = (h / 2.0f) + (th / 2.0f);

		return (lx <= sumx && ly <= sumy);
	}

	public void update() {
		for(Image sprite:cloudSprites) {
			sprite.moveBy(-1f, 0);
			if(sprite.getX() < -sprite.getWidth()) {
				sprite.setX(stage.getWidth());
				if(checkCollision(sprite))
					sprite.setX(stage.getWidth()+200);
			}
		}
	}

	public void dispose() {
		for(Texture tex:cloudTextures)
			tex.dispose();
	}

}
