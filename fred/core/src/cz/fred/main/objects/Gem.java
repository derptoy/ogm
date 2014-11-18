package cz.fred.main.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import cz.fred.main.Assets;
import cz.fred.main.graphics.Drawable;
import cz.fred.main.map.Map;
import cz.fred.main.physics.FixtureDefFactory;
import cz.fred.main.physics.SpriteBody;
import cz.fred.main.util.CustomRect;

public class Gem implements Drawable {
	
	public enum State {INDEPENDENT, HERO};
	private State state = State.INDEPENDENT;
	
	private float offsetY;
	
	private Texture texture;
	private Texture shadow;
	
	private SpriteBody body;
	
	private float animationTime;

	private Texture textureHero;
	
	public Gem(World world, int x, int y) {
		texture = Assets.getTexture("objects/gem_0001_gem.png");
		textureHero = Assets.getTexture("objects/gem_0002_gemHero.png");
		shadow = Assets.getTexture("objects/gem_0000_shadow.png");
		
		body = new SpriteBody(world, FixtureDefFactory.getGemDef());
		body.setPosition(	x + texture.getWidth()/2, 
							y + texture.getHeight()/2 - 25);
		
		animationTime = (float) (MathUtils.random()*2*Math.PI);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(shadow, 
					getX()-texture.getWidth()/2, 
					getY()-texture.getHeight()/2+25);
		if(state == State.INDEPENDENT)
			batch.draw(texture, 
					getX()-texture.getWidth()/2, 
					getY()-texture.getHeight()/2+25+offsetY);
		else if(state == State.HERO)
			batch.draw(textureHero, 
					getX()-texture.getWidth()/2, 
					getY()-texture.getHeight()/2+25+offsetY);
	}
	
	public float getX() {
		return body.getPosition().x * Map.TO_PIXEL;
	}

	public float getY() {
		return body.getPosition().y * Map.TO_PIXEL;
	}

	public void update() {
		animationTime += 5*Gdx.graphics.getDeltaTime();
		if(animationTime > 2*Math.PI)
			animationTime -= 2*Math.PI;
		
		offsetY = 3*(float) Math.sin(animationTime);
	}

	@Override
	public int compareTo(Drawable o) {
		float diff = getComparingY() - o.getComparingY();
		if(diff > 0)
			return 1;
		else if( diff < 0)
			return -1;
		else return 0;
	}
	
	public void setState(State state) {
		this.state = state;
	}

	@Override
	public float getComparingY() {
		return body.getPosition().y;
	}

	public void addForce(Vector2 vector) {
		vector.scl(1/Map.TO_PIXEL);
		body.applyForce(vector);
	}

	public CustomRect getPickRectangle() {
		return new CustomRect("", (int)getX() - 16, (int)getY()+15, 32, 40);
	}
	
	public State getState() {
		return state;
	}
}
