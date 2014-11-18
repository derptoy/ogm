package cz.fred.main.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import cz.fred.main.Assets;
import cz.fred.main.util.Direction;

public class EnemyGraphics {
	private Animation[] walkAnimations;
	private Animation currentAnimation;
	private float animationTime;
	private EnemyData data;
	private AtlasRegion shadow;
	private int direction;
	
	public EnemyGraphics(EnemyData data) {
		this.data = data;
		TextureAtlas atlas = Assets.getTextureAtlas("enemy/enemy.txt");
		
		walkAnimations = new Animation[4];
		
		Array<TextureRegion> reg = new Array<TextureRegion>();
		reg.add(atlas.findRegion("circle_0002s_0001_back"));
		reg.add(atlas.findRegion("circle_0002s_0000_back2"));
		walkAnimations[0] = new Animation(0.2f, reg);
		walkAnimations[0].setPlayMode(PlayMode.LOOP);
		
		reg = new Array<TextureRegion>();
		reg.add(atlas.findRegion("circle_0001s_0001_right"));
		reg.add(atlas.findRegion("circle_0001s_0000_right2"));
		walkAnimations[1] = new Animation(0.2f, reg);
		walkAnimations[1].setPlayMode(PlayMode.LOOP);
		
		reg = new Array<TextureRegion>();
		reg.add(atlas.findRegion("circle_0003s_0001_front"));
		reg.add(atlas.findRegion("circle_0003s_0000_front2"));
		walkAnimations[2] = new Animation(0.2f, reg);
		walkAnimations[2].setPlayMode(PlayMode.LOOP);
		
		reg = new Array<TextureRegion>();
		reg.add(atlas.findRegion("circle_0000s_0001_left"));
		reg.add(atlas.findRegion("circle_0000s_0000_left2"));
		walkAnimations[3] = new Animation(0.2f, reg);
		walkAnimations[3].setPlayMode(PlayMode.LOOP);
		
		shadow = atlas.findRegion("circle_0000_shadow");
		
		direction = Direction.getOffset(data.getDirection(),direction);
		currentAnimation = walkAnimations[direction];
	}

	public void draw(SpriteBatch batch) {
//		sprite.setRegion(currentAnimation.getKeyFrame(animationTime));
		direction = Direction.getOffset(data.getDirection(), direction);
		currentAnimation = walkAnimations[direction];
		batch.draw(shadow, 	data.getX() - getWidth()/2,
							data.getY() - getHeight()/2 +18);
		batch.draw(currentAnimation.getKeyFrame(animationTime), 
							data.getX() - getWidth()/2, 
							data.getY() - getHeight()/2 +18);
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

