package cz.roller.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;

import cz.roller.game.world.Settings;

@SuppressWarnings("unused")
public class CartWheel {
	
	private float wheelSize = 0.5f;
	private Body wheel;
	
	private Fixture fixtureWheel;
	private WheelJoint wheelJoint;
	private Sprite wheelSprite;
	
	float speed = 40;
	
	public CartWheel(World world, Body body, float x, float y, float cartWidth, float cartHeight, float offsetDir) {
		CircleShape circle1 = new CircleShape();
		circle1.setRadius(wheelSize/2);
		
		BodyDef bodyDefWheel1 = new BodyDef();
		bodyDefWheel1.type = BodyType.DynamicBody;
		bodyDefWheel1.position.set(x + offsetDir*(cartWidth/2 - wheelSize), y - cartHeight/2);
		wheel = world.createBody(bodyDefWheel1);
		
		FixtureDef fixtureDef1 = new FixtureDef();
		fixtureDef1.shape = circle1;
		fixtureDef1.density =		15f; 
		fixtureDef1.friction = 		200f;
		fixtureDef1.restitution = 	0.1f; // Make it bounce a little bit
		fixtureWheel = wheel.createFixture(fixtureDef1);
		
		circle1.dispose();
		
		WheelJointDef wheelJoint1Def = new WheelJointDef();
		wheelJoint1Def.bodyA = body;
		wheelJoint1Def.bodyB = wheel;
		wheelJoint1Def.type = JointType.WheelJoint;
		wheelJoint1Def.collideConnected = false;
		wheelJoint1Def.localAnchorA.set(offsetDir*(cartWidth/2 - wheelSize/2), - cartHeight/2);
		wheelJoint1Def.localAnchorB.set(0, 0.0f);
		wheelJoint1Def.enableMotor = false;
		wheelJoint1Def.frequencyHz	=	50;
		wheelJoint1Def.dampingRatio = 	0.1f;
		wheelJoint1Def.localAxisA.set(Vector2.Y);
		wheelJoint = (WheelJoint)world.createJoint(wheelJoint1Def);
		
		wheelJoint.setMotorSpeed(0);
		wheelJoint.setMaxMotorTorque(1*speed);
		
		wheelSprite = new Sprite(new Texture(Gdx.files.internal("cart/wheel.png")));
		wheelSprite.setSize(wheelSize*Settings.TO_PIXELS, wheelSize*Settings.TO_PIXELS);
		wheelSprite.setOrigin(wheelSprite.getWidth()/2, wheelSprite.getHeight()/2);
		
		wheelSprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	public void draw(SpriteBatch batch) {
		wheelSprite.setPosition(	Settings.TO_PIXELS * wheel.getPosition().x-wheelSprite.getWidth()/2, 
									Settings.TO_PIXELS * wheel.getPosition().y-wheelSprite.getHeight()/2);
		wheelSprite.setRotation((float)(wheel.getAngle()*MathUtils.radiansToDegrees));
		
		wheelSprite.draw(batch);
	}

	public void impuls(float dir) {
		if(dir == 0) {
			wheelJoint.enableMotor(false);
			wheelJoint.setMotorSpeed(0);
		} else {
			wheelJoint.enableMotor(true);
			wheelJoint.setMotorSpeed(dir * speed);
		}
		
		System.out.println("Impuls " +wheelJoint.getMotorSpeed());
	}

}
