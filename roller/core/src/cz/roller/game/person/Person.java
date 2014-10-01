package cz.roller.game.person;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import cz.roller.game.BodyDamage;
import cz.roller.game.util.AssetManager;
import cz.roller.game.world.Settings;
import cz.roller.game.world.Type;

@SuppressWarnings("unused")
public class Person {
	
	private Fixture torso;
	private Fixture neck;
	private RevoluteJoint neckJoint;
	private Fixture head;
	private RevoluteJoint headJoint;
	
	private float torsoWidth = 0.5f;
	private float torsoLength = 1.0f;
	private float headSize = 0.6f;
	private Sprite headSprite;
	private Sprite torsoSprite;
	private Sprite armSprite;
	private Sprite armBackSprite;
	
	private PersonLeg legFront;
	private PersonLeg legBack;
	
	private PersonArm armFront;
	private PersonArm armBack;

	public Person(float x, float y, float size, World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		
		torsoLength = size;
		torsoWidth = size/2;
		headSize = 0.6f * size;
		
		Body body = world.createBody(bodyDef);
		body.setUserData(new BodyDamage());
		body.getPosition().set(x, y);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(torsoWidth/2, torsoLength/2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 5; 
		fixtureDef.friction = PersonPhysics.FRICTION;
		fixtureDef.restitution = PersonPhysics.RESTITUTION;
		fixtureDef.filter.categoryBits = Settings.CATEGORY_CENTER;
		fixtureDef.filter.maskBits = Settings.CATEGORY_CENTER | Settings.CATEGORY_WORLD;
		torso = body.createFixture(fixtureDef);
		
		shape.dispose();
		
		legFront = new PersonLeg(size, world, body, Settings.CATEGORY_RIGHT_SIDE, AssetManager.getTexture("person/leg.png"));
		legBack = new PersonLeg(size, world, body, Settings.CATEGORY_LEFT_SIDE, AssetManager.getTexture("person/legback.png"));
		
		// Head
		BodyDef headDef = new BodyDef();
		headDef.type = BodyType.DynamicBody;
		headDef.position.set(x, y+0.6f * torsoLength + 0.5f*headSize);
		Body headBody = world.createBody(headDef);
		headBody.setUserData(new BodyDamage());

		CircleShape circle = new CircleShape();
		circle.setRadius(headSize/2.0f);

		FixtureDef fixtureHeadDef = new FixtureDef();
		fixtureHeadDef.shape = circle;
		fixtureHeadDef.density = 1; 
		fixtureHeadDef.friction = PersonPhysics.FRICTION;
		fixtureHeadDef.restitution = PersonPhysics.RESTITUTION; // Make it bounce a little bit
		fixtureHeadDef.filter.categoryBits = Settings.CATEGORY_WORLD;
		fixtureHeadDef.filter.maskBits = Settings.CATEGORY_CENTER | Settings.CATEGORY_WORLD;

		head = headBody.createFixture(fixtureHeadDef);
		head.setUserData(Type.PERSON);
//		headBody.setFixedRotation(true);
		
		RevoluteJointDef headJointDef = new RevoluteJointDef();
		headJointDef.bodyA = body;
		headJointDef.bodyB = headBody;
		headJointDef.type = JointType.RevoluteJoint;
//		headJointDef.length = 0.1f * torsoLength + 0.5f * headSize;
//		headJointDef.localAnchorA.set(0, 0.6f * torsoLength);
//		headJointDef.localAnchorB.set(0, 0.5f * headSize);
		headJointDef.localAnchorA.set(0, 0.6f * torsoLength);
		headJointDef.localAnchorB.set(0, -0.5f* headSize);
//		headJointDef.lowerAngle = 0.0f;
//		headJointDef.upperAngle = 0.0f;
		headJointDef.collideConnected = true;
		headJoint = (RevoluteJoint)world.createJoint(headJointDef);

		circle.dispose();
		
		armFront = new PersonArm(size, world, body, Settings.CATEGORY_RIGHT_SIDE, AssetManager.getTexture("person/arm.png"));
		armBack = new PersonArm(size, world, body, Settings.CATEGORY_LEFT_SIDE, AssetManager.getTexture("person/armback.png"));
		
		loadTextures();
	}
	
	private void loadTextures() {
		headSprite = new Sprite(AssetManager.getTexture("person/head.png"));
		headSprite.setSize(headSize*Settings.TO_PIXELS, headSize*Settings.TO_PIXELS);
		headSprite.setOrigin(headSize*Settings.TO_PIXELS/2, headSize*Settings.TO_PIXELS/2);
		headSprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		torsoSprite = new Sprite(AssetManager.getTexture("person/body.png"));
		torsoSprite.setSize(torsoWidth*Settings.TO_PIXELS, torsoLength*Settings.TO_PIXELS);
		torsoSprite.setOrigin(torsoWidth*Settings.TO_PIXELS/2, torsoLength*Settings.TO_PIXELS/2);
		torsoSprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	public void draw(SpriteBatch batch) {
		armBack.draw(batch);
		legBack.draw(batch);
		
		torsoSprite.setPosition(	Settings.TO_PIXELS*torso.getBody().getPosition().x-torsoSprite.getWidth()/2, 
									Settings.TO_PIXELS*torso.getBody().getPosition().y-torsoSprite.getHeight()/2);
		torsoSprite.setRotation((float)(torso.getBody().getAngle()*MathUtils.radiansToDegrees));
		torsoSprite.draw(batch);
		
		headSprite.setPosition(	Settings.TO_PIXELS*head.getBody().getPosition().x-headSprite.getWidth()/2, 
								Settings.TO_PIXELS*head.getBody().getPosition().y-headSprite.getHeight()/2);
		headSprite.setRotation((float)(head.getBody().getAngle()*MathUtils.radiansToDegrees));
		headSprite.draw(batch);

		legFront.draw(batch);
		armFront.draw(batch);
	}

	public void dispose() {
		torsoSprite.getTexture().dispose();
		headSprite.getTexture().dispose();
		
		legFront.dispose();
		armFront.dispose();
		armBack.dispose();
		legBack.dispose();
	}

	public void testLimb() {
		legFront.loseLimb();
	}

	public Vector2 getPosition() {
		return torso.getBody().getPosition().cpy().scl(Settings.TO_PIXELS);
	}


}
