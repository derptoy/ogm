package cz.fred.main.hero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import cz.fred.main.Assets;
import cz.fred.main.map.Map;
import cz.fred.main.util.Direction;

public class HeroGraphics {
	private Animation[] walkAnimations;
	private Animation currentAnimation;
	private float animationTime;
	private HeroData data;
	private AtlasRegion shadow;
	private int direction;
	
	public HeroGraphics(HeroData data) {
		this.data = data;
		TextureAtlas atlas = Assets.getTextureAtlas("hero/hero.txt");
		
		walkAnimations = new Animation[4];
		
		Array<TextureRegion> reg = new Array<TextureRegion>();
		reg.add(atlas.findRegion("hero_0002s_0000_back"));
		reg.add(atlas.findRegion("hero_0002s_0001_back1"));
		walkAnimations[0] = new Animation(0.2f, reg);
		walkAnimations[0].setPlayMode(PlayMode.LOOP);
		
		reg = new Array<TextureRegion>();
		reg.add(atlas.findRegion("hero_0000s_0000_right"));
		reg.add(atlas.findRegion("hero_0000s_0001_right1"));
		walkAnimations[1] = new Animation(0.2f, reg);
		walkAnimations[1].setPlayMode(PlayMode.LOOP);
		
		reg = new Array<TextureRegion>();
		reg.add(atlas.findRegion("hero_0003s_0000_front"));
		reg.add(atlas.findRegion("hero_0003s_0001_front1"));
		walkAnimations[2] = new Animation(0.2f, reg);
		walkAnimations[2].setPlayMode(PlayMode.LOOP);
		
		reg = new Array<TextureRegion>();
		reg.add(atlas.findRegion("hero_0001s_0000_left"));
		reg.add(atlas.findRegion("hero_0001s_0001_left1"));
		walkAnimations[3] = new Animation(0.2f, reg);
		walkAnimations[3].setPlayMode(PlayMode.LOOP);
		
		shadow = atlas.findRegion("hero_0000_shadow");
		
		direction = Direction.getOffset(data.getDirection(), direction);
		currentAnimation = walkAnimations[direction];
	}

	public void draw(SpriteBatch batch) {
//		sprite.setRegion(currentAnimation.getKeyFrame(animationTime));
		direction = Direction.getOffset(data.getDirection(), direction);
		currentAnimation = walkAnimations[direction];
		batch.draw(shadow, 	data.getX() - getWidth()/2,
							data.getY() - getHeight()/2 +20);
		batch.draw(currentAnimation.getKeyFrame(animationTime), 
							data.getX() - getWidth()/2, 
							data.getY() - getHeight()/2 +20);
//		sprite.setTexture(texture);
//		sprite.draw(batch);
	}

	public void update() {
		if( !data.getMove().equals(Direction.NONE))
			animationTime += Gdx.graphics.getDeltaTime();
		else
			animationTime = 0;
	}

	public float getWidth() {
		return currentAnimation.getKeyFrame(0).getRegionWidth();
	}

	public float getHeight() {
		return currentAnimation.getKeyFrame(0).getRegionHeight();
	}

}
