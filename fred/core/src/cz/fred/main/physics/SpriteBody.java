package cz.fred.main.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import cz.fred.main.map.Map;
import cz.fred.main.util.Point2D;

public class SpriteBody {
	
	private Body body;
	@SuppressWarnings("unused")
	private Fixture fixture;
	@SuppressWarnings("unused")
	private FixtureDefinition definition;

	public SpriteBody(World world, FixtureDefinition definition) {
		this.definition = definition;
		BodyDef bodyDef = new BodyDef();
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = 2.5f;
		bodyDef.type = BodyType.DynamicBody;
		
		body = world.createBody(bodyDef);
		FixtureDef fixtureDef = definition.getFixtureDef();
		fixture = body.createFixture(fixtureDef);
	}
	
	public void setPosition(float x, float y) {
//		body.getPosition().set(	x/Map.TO_PIXEL + definition.getWidth()/2f,
//								y/Map.TO_PIXEL + definition.getHeight()/2f);
		body.setTransform(	x/Map.TO_PIXEL ,
							y/Map.TO_PIXEL , 0);
//		body.applyAngularImpulse(10, true);
		System.out.println("G: "+body.getPosition());
	}
	
	public Vector2 getPosition() {
		return body.getPosition();
	}

	public void applyForce(Vector2 move) {
		body.applyForceToCenter(move.cpy().scl(20), true);
//		body.setLinearVelocity(new Vector2(2*move.x, 2*move.y));
	}

	public Fixture getFixture() {
		return fixture;
	}
}
