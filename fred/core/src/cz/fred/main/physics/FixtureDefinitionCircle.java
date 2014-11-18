package cz.fred.main.physics;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class FixtureDefinitionCircle  implements FixtureDefinition {
	private FixtureDef fixtureDef;
	private float width;
	private float height;
	private CircleShape shape;
	
	public FixtureDefinitionCircle(float width, float height, float density, float friction, float restitution) {
		this.width = width;
		this.height = height;
		
		shape = new CircleShape();
		shape.setRadius(width/2f);
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = density; 
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution; // Make it bounce a little bit
		//TODO: dispose
	}
	
	public FixtureDef getFixtureDef() {
		return fixtureDef;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void dispose() {
		shape.dispose();
	}
}