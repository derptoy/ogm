package cz.roller.game.person;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import cz.roller.game.world.Settings;

@SuppressWarnings("unused")
public class PersonArm {
	
	private float armLength = 0.8f;
	private float armHeight = 0.24f;
	private float torsoLength = 1.0f;
	
	private Fixture arm;
	private Fixture arm2;
	private RevoluteJoint armJoint;
	private RevoluteJoint armJoint2;
	
	private Sprite armSprite;
	private Sprite arm2Sprite;

	public PersonArm(float size, World world, Body body, short category, String url) {
		torsoLength = size;
		armHeight = 0.24f * size;
		armLength = 0.8f * size;
		
		createArms(world, body, category);
		createTextures(url);
	}
	
	private void createTextures(String url) {
		armSprite = new Sprite(new Texture(Gdx.files.internal(url)));
		armSprite.setSize(Settings.TO_PIXELS*armLength, Settings.TO_PIXELS*armHeight);
		armSprite.setOrigin(Settings.TO_PIXELS*armLength/2, Settings.TO_PIXELS*armHeight/2);
		armSprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		arm2Sprite = new Sprite(new Texture(Gdx.files.internal(url)));
		arm2Sprite.setSize(Settings.TO_PIXELS*armLength, Settings.TO_PIXELS*armHeight);
		arm2Sprite.setOrigin(Settings.TO_PIXELS*armLength/2, Settings.TO_PIXELS*armHeight/2);
		arm2Sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	private void createArms(World world, Body body, short category) {
		// Arm
		BodyDef armBodyDef = new BodyDef();
		armBodyDef.type = BodyType.DynamicBody;
		armBodyDef.position.set(body.getPosition());
		Body bodyArm = world.createBody(armBodyDef);
		
		PolygonShape shapeArm = new PolygonShape();
		shapeArm.setAsBox(armLength/2, armHeight/2);

		FixtureDef fixtureDefArm = new FixtureDef();
		fixtureDefArm.shape = shapeArm;
		fixtureDefArm.density = 2; 
		fixtureDefArm.friction = 0.0f;
		fixtureDefArm.restitution = 0.1f;
		fixtureDefArm.filter.categoryBits = category;
		fixtureDefArm.filter.maskBits = (short) (category | Settings.CATEGORY_WORLD);

		arm = bodyArm.createFixture(fixtureDefArm);
		
		RevoluteJointDef armJointDef = new RevoluteJointDef();
		armJointDef.bodyA = body;
		armJointDef.bodyB = bodyArm;
		armJointDef.type = JointType.RevoluteJoint;
		armJointDef.collideConnected = false;
		armJointDef.localAnchorA.set(0, 0.8f*torsoLength/2);
		armJointDef.localAnchorB.set(armLength/2, 0);
//		armJointDef.lowerAngle = -90 * MathUtils.degreesToRadians;
//		armJointDef.upperAngle = 90 * MathUtils.degreesToRadians;
		armJoint = (RevoluteJoint)world.createJoint(armJointDef);
//		armJoint.enableLimit(true);
		
		shapeArm.dispose();
		
		// Arm 2
		BodyDef armBodyDef2 = new BodyDef();
		armBodyDef2.type = BodyType.DynamicBody;
		armBodyDef2.position.set(body.getPosition());
		Body bodyArm2 = world.createBody(armBodyDef2);

		PolygonShape shapeArm2 = new PolygonShape();
		shapeArm2.setAsBox(armLength/2, armHeight/2);

		FixtureDef fixtureDefArm2 = new FixtureDef();
		fixtureDefArm2.shape = shapeArm2;
		fixtureDefArm2.density = 2; 
		fixtureDefArm2.friction = 0.0f;
		fixtureDefArm2.restitution = 0.1f;
		fixtureDefArm2.filter.categoryBits = category;
		fixtureDefArm2.filter.maskBits = (short) (category | Settings.CATEGORY_WORLD);

		arm2 = bodyArm2.createFixture(fixtureDefArm2);

		RevoluteJointDef armJointDef2 = new RevoluteJointDef();
		armJointDef2.bodyA = bodyArm;
		armJointDef2.bodyB = bodyArm2;
		armJointDef2.type = JointType.RevoluteJoint;
		armJointDef2.collideConnected = false;
		armJointDef2.localAnchorA.set(-armLength/2, 0);
		armJointDef2.localAnchorB.set(armLength/2, 0);
//		armJointDef2.lowerAngle = -90 * MathUtils.degreesToRadians;
//		armJointDef2.upperAngle = 90 * MathUtils.degreesToRadians;
		armJoint2 = (RevoluteJoint)world.createJoint(armJointDef2);
//		armJoint2.enableLimit(true);

		shapeArm2.dispose();
	}
	
	public void draw(SpriteBatch batch) {
		armSprite.setPosition(	Settings.TO_PIXELS*arm.getBody().getPosition().x-armSprite.getWidth()/2, 
								Settings.TO_PIXELS*arm.getBody().getPosition().y-armSprite.getHeight()/2);
		armSprite.setRotation((float)(arm.getBody().getAngle()*MathUtils.radiansToDegrees));
		arm2Sprite.setPosition(	Settings.TO_PIXELS*arm2.getBody().getPosition().x-arm2Sprite.getWidth()/2, 
								Settings.TO_PIXELS*arm2.getBody().getPosition().y-arm2Sprite.getHeight()/2);
		arm2Sprite.setRotation((float)(arm2.getBody().getAngle()*MathUtils.radiansToDegrees));
	
	
		armSprite.draw(batch);
		arm2Sprite.draw(batch);
	}

	public void dispose() {
		armSprite.getTexture().dispose();
	}

}
