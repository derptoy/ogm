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
public class PersonLeg {
	private float legWidth = 0.3f;
	private float legLength = 0.8f;
	
	private float torsoLength = 1.0f;
	
	private Sprite legSprite;
	private Sprite leg2Sprite;
	
	private Fixture leg1;
	private Fixture leg2;
	private RevoluteJoint legJoint1;
	private RevoluteJoint legJoint2;
	private World world;
	
	public PersonLeg(float size, World world,Body body, short category, String url) {
		this.world = world;
		torsoLength = size;
		legWidth = 0.3f * size;
		legLength = 0.8f * size;
		
		createLegs(world, body, category);
		createTextures(url);
	}
	
	private void createTextures(String url) {
		legSprite = new Sprite(new Texture(Gdx.files.internal(url)));
		legSprite.setSize(legWidth, legLength);
		legSprite.setOrigin(legWidth/2, legLength/2);
		legSprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		leg2Sprite = new Sprite(new Texture(Gdx.files.internal(url)));
		leg2Sprite.setSize(legWidth, legLength);
		leg2Sprite.setOrigin(legWidth/2, legLength/2);
		leg2Sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	private void createLegs(World world, Body body, short category) {
		BodyDef bodyDefLeg = new BodyDef();
		bodyDefLeg.type = BodyType.DynamicBody;
		Body bodyLeg = world.createBody(bodyDefLeg);
		
		PolygonShape shapeLeg = new PolygonShape();
		shapeLeg.setAsBox(legWidth/2, legLength/2);

		FixtureDef fixtureDefLeg = new FixtureDef();
		fixtureDefLeg.shape = shapeLeg;
		fixtureDefLeg.density = 2; 
		fixtureDefLeg.friction = 0.0f;
		fixtureDefLeg.restitution = 0.1f;
		fixtureDefLeg.filter.categoryBits = category;
		fixtureDefLeg.filter.maskBits = (short) (category | Settings.CATEGORY_WORLD);
		leg1 = bodyLeg.createFixture(fixtureDefLeg);
		
		RevoluteJointDef legJointDef1 = new RevoluteJointDef();
		legJointDef1.bodyA = body;
		legJointDef1.bodyB = bodyLeg;
		legJointDef1.type = JointType.RevoluteJoint;
		legJointDef1.collideConnected = false;
		legJointDef1.localAnchorA.set(0, -0.9f*torsoLength/2);
		legJointDef1.localAnchorB.set(0, legLength/2);
//		legJointDef1.lowerAngle = -170 * MathUtils.degreesToRadians;
//		legJointDef1.upperAngle = 170 * MathUtils.degreesToRadians;
		legJoint1 = (RevoluteJoint)world.createJoint(legJointDef1);
//		legJoint1.enableLimit(true);
		
		shapeLeg.dispose();
		
		// Leg2
		BodyDef bodyDefLeg2 = new BodyDef();
		bodyDefLeg2.type = BodyType.DynamicBody;
		Body bodyLeg2 = world.createBody(bodyDefLeg2);
		
		PolygonShape shapeLeg2 = new PolygonShape();
		shapeLeg2.setAsBox(legWidth/2, legLength/2);

		FixtureDef fixtureDefLeg2 = new FixtureDef();
		fixtureDefLeg2.shape = shapeLeg;
		fixtureDefLeg2.density = 2f; 
		fixtureDefLeg2.friction = 0.0f;
		fixtureDefLeg2.restitution = 0.0f;
		fixtureDefLeg2.filter.categoryBits = category;
		fixtureDefLeg2.filter.maskBits = (short) (category | Settings.CATEGORY_WORLD);
		leg2 = bodyLeg2.createFixture(fixtureDefLeg2);
		
		RevoluteJointDef legJointDef2 = new RevoluteJointDef();
		legJointDef2.bodyA = bodyLeg;
		legJointDef2.bodyB = bodyLeg2;
		legJointDef2.type = JointType.RevoluteJoint;
		legJointDef2.collideConnected = false;
		legJointDef2.localAnchorA.set(0, -legLength/2);
		legJointDef2.localAnchorB.set(0, legLength/2);
		legJointDef2.lowerAngle = -170 * MathUtils.degreesToRadians;
		legJointDef2.upperAngle = 10 * MathUtils.degreesToRadians;
		legJoint2 = (RevoluteJoint)world.createJoint(legJointDef2);
		legJoint2.enableLimit(true);
		
		shapeLeg2.dispose();
	}
	
	public void draw(SpriteBatch batch) {
		legSprite.setPosition(	leg1.getBody().getPosition().x-legSprite.getWidth()/2, 
								leg1.getBody().getPosition().y-legSprite.getHeight()/2);
		legSprite.setRotation((float)(leg1.getBody().getAngle()*MathUtils.radiansToDegrees));
		leg2Sprite.setPosition(	leg2.getBody().getPosition().x-leg2Sprite.getWidth()/2, 
								leg2.getBody().getPosition().y-leg2Sprite.getHeight()/2);
		leg2Sprite.setRotation((float)(leg2.getBody().getAngle()*MathUtils.radiansToDegrees));
	
	
		legSprite.draw(batch);
		leg2Sprite.draw(batch);
	}

	public void dispose() {
		legSprite.getTexture().dispose();
	}

	public void loseLimb() {
		world.destroyJoint(legJoint2);
	}

}
