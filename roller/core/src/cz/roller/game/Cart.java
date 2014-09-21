package cz.roller.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import cz.roller.game.world.Settings;

@SuppressWarnings("unused")
public class Cart {

	private Body body;
	private Fixture fixture;
	private Sprite cartSprite;
	
	private float cartWidth = 1.6f;
	private float cartHeight = 1;
	
	private CartWheel leftWheel;
	private CartWheel rightWheel;
	private Body bodySide;
	private Fixture fixtureSide;
	private Fixture fixtureSide2;
	private Fixture fixtureSeat;

	public Cart(float x, float y, World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		
		PolygonShape box = new PolygonShape();
		float[] vertices = new float[]{
				-cartWidth/2,-cartHeight/2,
				-cartWidth/2,-0.3f*cartHeight/2,
				cartWidth/2, -0.3f*cartHeight/2,
				cartWidth/2,-cartHeight/2
				};
		box.set(vertices);
		
		PolygonShape boxSide = new PolygonShape();
		vertices = new float[] {
				-cartWidth/2,-0.3f*cartHeight/2,
				-cartWidth/2,cartHeight/2,
				-0.5f*cartWidth/2,cartHeight/2,
				-0.5f*cartWidth/2,-0.3f*cartHeight/2
		};
		boxSide.set(vertices);
		
		PolygonShape boxSide2 = new PolygonShape();
		vertices = new float[] {
				cartWidth/2,-0.3f*cartHeight/2,
				cartWidth/2,cartHeight/2,
				0.5f*cartWidth/2,cartHeight/2,
				0.5f*cartWidth/2,-0.3f*cartHeight/2
		};
		boxSide2.set(vertices);
		
		PolygonShape boxSeat = new PolygonShape();
		vertices = new float[] {
				-0.5f*cartWidth/2,-0.3f*cartHeight/2,
				-0.5f*cartWidth/2,0.2f*cartHeight/2,
				-0.2f*cartWidth/2,0.2f*cartHeight/2,
				-0.2f*cartWidth/2,-0.3f*cartHeight/2,
				
		};
		boxSeat.set(vertices);
		
		body = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = box;
		fixtureDef.density = 8f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.3f; // Make it bounce a little bit
		fixture = body.createFixture(fixtureDef);
		
		box.dispose();
		
		FixtureDef fixtureDefSide = new FixtureDef();
		fixtureDefSide.shape = boxSide;
		fixtureDefSide.density = 8f; 
		fixtureDefSide.friction = 0.4f;
		fixtureDefSide.restitution = 0.3f; // Make it bounce a little bit
		fixtureSide = body.createFixture(fixtureDefSide);
		
		boxSide.dispose();
		
		FixtureDef fixtureDefSide2 = new FixtureDef();
		fixtureDefSide2.shape = boxSide2;
		fixtureDefSide2.density = 8f; 
		fixtureDefSide2.friction = 0.4f;
		fixtureDefSide2.restitution = 0.3f; // Make it bounce a little bit
		fixtureSide2 = body.createFixture(fixtureDefSide2);
		
		boxSide2.dispose();
		
		FixtureDef fixtureDefSeat = new FixtureDef();
		fixtureDefSeat.shape = boxSeat;
		fixtureDefSeat.density = 8f; 
		fixtureDefSeat.friction = 0.4f;
		fixtureDefSeat.restitution = 0.3f; // Make it bounce a little bit
		fixtureSeat = body.createFixture(fixtureDefSeat);
		
		boxSeat.dispose();
		
		cartSprite = new Sprite(new Texture(Gdx.files.internal("cart/cart.png")));
		cartSprite.setSize(cartWidth*Settings.TO_PIXELS, cartHeight*Settings.TO_PIXELS);
		cartSprite.setOrigin(cartSprite.getWidth()/2, cartSprite.getHeight()/2);
		
		cartSprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		leftWheel = new CartWheel(world, body, x, y, cartWidth, cartHeight, -1);
		rightWheel = new CartWheel(world, body, x, y, cartWidth, cartHeight, 1);
	}
	
	public void render(SpriteBatch batch) {
		cartSprite.setPosition(	Settings.TO_PIXELS * body.getPosition().x - cartSprite.getWidth()/2,
								Settings.TO_PIXELS * body.getPosition().y - cartSprite.getHeight()/2);
		cartSprite.setRotation((float)(body.getAngle()*180/Math.PI));
		
		batch.begin();
		cartSprite.draw(batch);
		leftWheel.draw(batch);
		rightWheel.draw(batch);
		batch.end();
	}

	public void impuls(float dir) {
		leftWheel.impuls(dir);
		rightWheel.impuls(dir);
	}

	public Body getBody() {
		return body;
	}

	public void dispose() {
		cartSprite.getTexture().dispose();
	}

}
