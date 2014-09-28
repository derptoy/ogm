package cz.roller.game.cart;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import cz.roller.game.person.PersonManager;
import cz.roller.game.util.AssetManager;
import cz.roller.game.world.Settings;

@SuppressWarnings("unused")
public class Cart {

	private Body body;
	private Fixture fixture;
	private Sprite cartSprite;
	
	private float cartWidth = 1.6f;
	private float cartHeight = 1;
	
	private float supWidth = 0.3f*cartWidth;
	private float supHeight = 0.1f*cartHeight;
	
	private float conWidth = 0.6f*cartWidth;
	private float conHeight = 0.1f*cartHeight;
	
	private CartWheel leftWheel;
	private CartWheel rightWheel;
	private Body bodySide;
	private Fixture fixtureSide;
	private Fixture fixtureSide2;
	private Fixture fixtureSeat;
	private Fixture connector;
	private RevoluteJoint connectorJoint1;
	private RevoluteJoint connectorJoint2;
	private Color tint = Color.WHITE;
	private Body connectorBody;
	private Fixture support;
	private Sprite cartConnectorSprite;
	private RevoluteJoint supportJoint;
	private Sprite supportSprite;

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
		
		// SUPPORT
		BodyDef bodyDef2 = new BodyDef();
		bodyDef2.type = BodyType.DynamicBody;
		bodyDef2.position.set(x+0.1f*cartWidth, y+0.4f*cartHeight);
		
		PolygonShape supportShape = new PolygonShape();
		supportShape.setAsBox(supWidth/2, supHeight/2);
		
		Body supportBody = world.createBody(bodyDef2);
		
		FixtureDef fixtureDefSide3 = new FixtureDef();
		fixtureDefSide3.shape = supportShape;
		fixtureDefSide3.density = 8f; 
		fixtureDefSide3.friction = 0.4f;
		fixtureDefSide3.restitution = 0.3f; // Make it bounce a little bit
		support = supportBody.createFixture(fixtureDefSide3);
		
		RevoluteJointDef supportJointDef = new RevoluteJointDef();
		supportJointDef.bodyA = body;
		supportJointDef.bodyB = supportBody;
		supportJointDef.type = JointType.RevoluteJoint;
		supportJointDef.collideConnected = false;
		supportJointDef.localAnchorA.set(0.1f*cartWidth+supWidth/2, 0.4f*cartHeight);
		supportJointDef.localAnchorB.set(supWidth/2, 0);
		supportJointDef.lowerAngle = -30 * MathUtils.degreesToRadians;
		supportJointDef.upperAngle = 30 * MathUtils.degreesToRadians;
		supportJoint = (RevoluteJoint)world.createJoint(supportJointDef);
		supportJoint.enableLimit(true);
		
		// Sprite
		cartSprite = new Sprite(AssetManager.getTexture("cart/cart.png"));
		cartSprite.setSize(cartWidth*Settings.TO_PIXELS, cartHeight*Settings.TO_PIXELS);
		cartSprite.setOrigin(cartSprite.getWidth()/2, cartSprite.getHeight()/2);
		
		cartSprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		cartConnectorSprite = new Sprite(AssetManager.getTexture("cart/connector.png"));
		cartConnectorSprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		cartConnectorSprite.setSize(conWidth*Settings.TO_PIXELS, conHeight*Settings.TO_PIXELS);
		cartConnectorSprite.setOrigin(cartConnectorSprite.getWidth()/2, cartConnectorSprite.getHeight()/2);
		
		supportSprite = new Sprite(AssetManager.getTexture("cart/connector.png"));
		supportSprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		supportSprite.setSize(supWidth*Settings.TO_PIXELS, supHeight*Settings.TO_PIXELS);
		supportSprite.setOrigin(supportSprite.getWidth()/2, supportSprite.getHeight()/2);
		
		leftWheel = new CartWheel(world, body, x, y, cartWidth, cartHeight, -1);
		rightWheel = new CartWheel(world, body, x, y, cartWidth, cartHeight, 1);
	}
	
	public void render(SpriteBatch batch) {
		cartSprite.setPosition(	Settings.TO_PIXELS * body.getPosition().x - cartSprite.getWidth()/2,
								Settings.TO_PIXELS * body.getPosition().y - cartSprite.getHeight()/2);
		cartSprite.setRotation((float)(body.getAngle()*180/Math.PI));
		
		supportSprite.setPosition(	Settings.TO_PIXELS * support.getBody().getPosition().x - supportSprite.getWidth()/2,
									Settings.TO_PIXELS * support.getBody().getPosition().y - supportSprite.getHeight()/2);
		supportSprite.setRotation((float)(support.getBody().getAngle()*180/Math.PI));
		
		batch.begin();
		cartSprite.setColor(tint);
		cartSprite.draw(batch);
		leftWheel.draw(batch);
		rightWheel.draw(batch);
		supportSprite.draw(batch);
		if(connector != null) {
			cartConnectorSprite.setPosition(	Settings.TO_PIXELS * connector.getBody().getPosition().x - cartConnectorSprite.getWidth()/2,
												Settings.TO_PIXELS * connector.getBody().getPosition().y - cartConnectorSprite.getHeight()/2);
			cartConnectorSprite.setRotation((float)(connector.getBody().getAngle()*180/Math.PI));
			
			cartConnectorSprite.draw(batch);
		}
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

	public void connect(Cart cart2, World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(body.getPosition().x-0.4f*cartWidth-conWidth/2, body.getPosition().y-0.1f*cartHeight-conHeight/4);
		connectorBody = world.createBody(bodyDef);
		PolygonShape con = new PolygonShape();
		con.setAsBox(conWidth/2, conHeight/2);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = con;
		fixtureDef.density = 8f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.3f; // Make it bounce a little bit
		connector = connectorBody.createFixture(fixtureDef);
		con.dispose();
		
		RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
		revoluteJointDef.bodyA = body;
		revoluteJointDef.bodyB = connectorBody;
		revoluteJointDef.type = JointType.RevoluteJoint;
		revoluteJointDef.collideConnected = false;
		revoluteJointDef.localAnchorA.set(-0.4f*cartWidth,-0.1f*cartHeight);
		revoluteJointDef.localAnchorB.set(conWidth/2, conHeight/2);
		revoluteJointDef.enableMotor = false;
		connectorJoint1 = (RevoluteJoint)world.createJoint(revoluteJointDef);
		
		RevoluteJointDef revoluteJointDef2 = new RevoluteJointDef();
		revoluteJointDef.bodyA = cart2.getBody();
		revoluteJointDef.bodyB = connector.getBody();
		revoluteJointDef.type = JointType.RevoluteJoint;
		revoluteJointDef.collideConnected = false;
		revoluteJointDef.localAnchorA.set(0.4f*cartWidth,-0.1f*cartHeight);
		revoluteJointDef.localAnchorB.set(-conWidth/2, conHeight/4);
		revoluteJointDef.enableMotor = false;
		connectorJoint2 = (RevoluteJoint)world.createJoint(revoluteJointDef);
	}

	public void setTint(Color color) {
		this.tint  = color;
	}

	public void addPerson(PersonManager personManager, World world) {
		personManager.createPerson(	body.getPosition().x - 0.17f*cartWidth,
									body.getPosition().y + 0.35f*cartHeight, 0.5f,world);
	}

}
