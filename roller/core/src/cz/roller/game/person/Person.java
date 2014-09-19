package cz.roller.game.person;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import cz.roller.game.world.Settings;

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
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(torsoWidth/2, torsoLength/2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 2; 
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 0.1f;
		fixtureDef.filter.categoryBits = Settings.CATEGORY_CENTER;
		fixtureDef.filter.maskBits = Settings.CATEGORY_CENTER | Settings.CATEGORY_WORLD;
		torso = body.createFixture(fixtureDef);
		
		shape.dispose();
		
		legFront = new PersonLeg(size, world, body, Settings.CATEGORY_RIGHT_SIDE, "person/leg.png");
		legBack = new PersonLeg(size, world, body, Settings.CATEGORY_LEFT_SIDE, "person/legback.png");
		
		// Neck
//		BodyDef bodyDefNeck = new BodyDef();
//		bodyDefNeck.type = BodyType.DynamicBody;
//
//		Body bodyNeck = world.createBody(bodyDefNeck);
//
//		PolygonShape shapeNeck = new PolygonShape();
//		shapeNeck.setAsBox(0.05f, 0.1f);
//
//		FixtureDef fixtureDefNeck = new FixtureDef();
//		fixtureDefNeck.shape = shapeLeg;
//		fixtureDefNeck.density = 0.5f; 
//		fixtureDefNeck.friction = 0.0f;
//		fixtureDefNeck.restitution = 0.1f;
//
//		neck = bodyNeck.createFixture(fixtureDefNeck);
//
//		RevoluteJointDef neckJointDef = new RevoluteJointDef();
//		neckJointDef.bodyA = body;
//		neckJointDef.bodyB = bodyNeck;
//		neckJointDef.type = JointType.RevoluteJoint;
//		neckJointDef.localAnchorA.set(0, 0.5f);
//		neckJointDef.lowerAngle = 0.0f;
//		neckJointDef.upperAngle = 0.0f;
//		neckJointDef.collideConnected = false;
//		neckJointDef.localAnchorB.set(0, -0.1f);
//		neckJoint = (RevoluteJoint)world.createJoint(neckJointDef);
//
//		shapeNeck.dispose();
		
		// Head
		BodyDef headDef = new BodyDef();
		headDef.type = BodyType.DynamicBody;
		Body headBody = world.createBody(headDef);

		CircleShape circle = new CircleShape();
		circle.setRadius(headSize/2.0f);

		FixtureDef fixtureHeadDef = new FixtureDef();
		fixtureHeadDef.shape = circle;
		fixtureHeadDef.density = 2; 
		fixtureHeadDef.friction = 0.0f;
		fixtureHeadDef.restitution = 0.1f; // Make it bounce a little bit
		fixtureHeadDef.filter.categoryBits = Settings.CATEGORY_CENTER;
		fixtureHeadDef.filter.maskBits = Settings.CATEGORY_CENTER | Settings.CATEGORY_WORLD;

		head = headBody.createFixture(fixtureHeadDef);
		
		RevoluteJointDef headJointDef = new RevoluteJointDef();
		headJointDef.bodyA = body;
		headJointDef.bodyB = headBody;
		headJointDef.type = JointType.RevoluteJoint;
		headJointDef.localAnchorA.set(0, 0.6f * torsoLength);
		headJointDef.localAnchorB.set(0, 0.5f * headSize);
//		headJointDef.lowerAngle = 0.0f;
//		headJointDef.upperAngle = 0.0f;
		headJointDef.collideConnected = true;
		headJoint = (RevoluteJoint)world.createJoint(headJointDef);

		circle.dispose();
		
		armFront = new PersonArm(size, world, body, Settings.CATEGORY_RIGHT_SIDE, "person/arm.png");
		armBack = new PersonArm(size, world, body, Settings.CATEGORY_LEFT_SIDE, "person/armback.png");
		
		loadTextures();
	}
	
	private void loadTextures() {
		headSprite = new Sprite(new Texture(Gdx.files.internal("person/head.png")));
		headSprite.setSize(headSize, headSize);
		headSprite.setOrigin(headSize/2, headSize/2);
		headSprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		torsoSprite = new Sprite(new Texture(Gdx.files.internal("person/body.png")));
		torsoSprite.setSize(torsoWidth, torsoLength);
		torsoSprite.setOrigin(torsoWidth/2, torsoLength/2);
		torsoSprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	public void draw(SpriteBatch batch) {
		armBack.draw(batch);
		legBack.draw(batch);
		
		torsoSprite.setPosition(	torso.getBody().getPosition().x-torsoSprite.getWidth()/2, 
									torso.getBody().getPosition().y-torsoSprite.getHeight()/2);
		torsoSprite.setRotation((float)(torso.getBody().getAngle()*MathUtils.radiansToDegrees));
		torsoSprite.draw(batch);
		
		headSprite.setPosition(	head.getBody().getPosition().x-headSprite.getWidth()/2, 
								head.getBody().getPosition().y-headSprite.getHeight()/2);
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


}
