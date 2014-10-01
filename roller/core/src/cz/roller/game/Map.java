package cz.roller.game;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import cz.roller.game.level.Level;
import cz.roller.game.track.SupportPole;
import cz.roller.game.util.AssetManager;
import cz.roller.game.world.Settings;
import cz.roller.game.world.Type;

public class Map {
	
	private Texture trackTexture;
	private Texture trackSupportTexture;
	private Texture trackSupportPoleTexture;
	private LinkedList<SupportPole> poles;
	private Texture trackSupportPoleBase;
	
	private Vector2[] points;
	private Vector2[] dataSet;
	private Body groundBody;
	private World world;
	private float[] spriteVertices;
	private Fixture track;
	private Level level;
	@SuppressWarnings("unused")
	private Fixture sensorFixture;
	private Texture flagTexture;
	
	public Map(World world, Level level) {
		this.world = world;
		this.level = level;
		prepareDataSet();
		createPhysics();
		createMap();
	}

	private void createMap() {
		trackTexture = AssetManager.getTexture("track/track.png");
		trackTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		trackTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		flagTexture = AssetManager.getTexture("track/flag.png");
		trackSupportTexture = AssetManager.getTexture("track/track.png");
		trackSupportTexture = AssetManager.getTexture("track/support.png");
		trackSupportPoleTexture = AssetManager.getTexture("track/supportPole.png");
		trackSupportPoleBase = AssetManager.getTexture("track/supportBase.png");
		trackSupportPoleBase.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		createSupports();
	}
	
	private void createSupports() {
		poles = new LinkedList<SupportPole>();
		for(int i=0;i<points.length;i+=4) {
			SupportPole pole = new SupportPole(points[i].x, points[i].y, trackSupportTexture, trackSupportPoleTexture, trackSupportPoleBase);
			
			poles.add(pole);
		}
	}

	private void createPhysics() {
		createSplines();
		createGround();
	}
	
	private void createGround() {
		// Create our body definition
		BodyDef groundBodyDef = new BodyDef();  
		// Set its world position
		groundBodyDef.position.set(new Vector2(-1.0f,20 ));
		groundBodyDef.angle=0.0f;

		// Create a body from the defintion and add it to the world
		groundBody = world.createBody(groundBodyDef);  

		// Create a polygon shape
		PolygonShape groundBox = new PolygonShape();  
		// Set the polygon shape as a box which is twice the size of our view port and 20 high
		// (setAsBox takes half-width and half-height as arguments)
		groundBox.setAsBox(1, 40);
		// Create a fixture from our polygon shape and add it to our ground body  
		groundBody.createFixture(groundBox, 0.0f); 
		// Clean up after ourselves
		groundBox.dispose();
		
		groundBodyDef = new BodyDef();  
		// Set its world position
		groundBodyDef.position.set(new Vector2(level.getTrackWidth(),20 ));
		groundBodyDef.angle=0.0f;

		// Create a body from the defintion and add it to the world
		groundBody = world.createBody(groundBodyDef);  
		
		PolygonShape endSensor = new PolygonShape();
		endSensor.setAsBox(10, 40);
		
		FixtureDef sensorDef = new FixtureDef();
		sensorDef.shape = endSensor;
		sensorDef.isSensor = true;
//		sensorDef.filter.maskBits = Settings.CATEGORY_END_OF_LEVEL;
		sensorDef.filter.categoryBits = Settings.CATEGORY_END_OF_LEVEL;
		sensorFixture = groundBody.createFixture(sensorDef);
		
		// Create a polygon shape
		groundBox = new PolygonShape();  
		// Set the polygon shape as a box which is twice the size of our view port and 20 high
		// (setAsBox takes half-width and half-height as arguments)
		groundBox.setAsBox(1, 40);
		// Create a fixture from our polygon shape and add it to our ground body  
		groundBody.createFixture(groundBox, 0.0f); 
		// Clean up after ourselves
		groundBox.dispose();
	}
	
	private void createFloorBoxes(float k) {
		Vector2 lastVec3 = null;
		for(int i = 0; i < k-1; ++i) {
			float x = points[i].x;
			float y = points[i].y;
			float x2 = points[i+1].x;
			float y2 = points[i+1].y;
			BodyDef groundBodyDef = new BodyDef();  
			// Set its world position
			groundBodyDef.position.set(x,y);

			// Create a body from the defintion and add it to the world
			Body groundBody = world.createBody(groundBodyDef);  

			// Create a polygon shape
			PolygonShape groundBox = new PolygonShape();  
			// Set the polygon shape as a box which is twice the size of our view port and 20 high
			// (setAsBox takes half-width and half-height as arguments)
			//		float length = (float)Math.sqrt(Math.pow(x2-x, 2)+Math.pow(y2-y, 2);
			Vector2 direction = new Vector2(x2-x, y2-y);
			direction.nor();
			Vector2 kolm = new Vector2(direction.y, direction.x);
			//		kolm.scl(1f);
			kolm.y *= -1;
			Vector2[] vertices = new Vector2[4];
			vertices[0] = new Vector2(0, 0);
			vertices[1] = new Vector2(x2-x, y2-y);
			vertices[2] = new Vector2(x2-x+kolm.x, y2-y+kolm.y);
			if(lastVec3 == null)
				vertices[3] = new Vector2(0+kolm.x, +kolm.y);
			else
				vertices[3] = lastVec3;
			
			lastVec3 = kolm;
			groundBox.set(vertices);
			// Create a fixture from our polygon shape and add it to our ground body  
			track = groundBody.createFixture(groundBox, 0.0f);
			track.setUserData(Type.TRACK);
			// Clean up after ourselves
			groundBox.dispose();
		}
	}

	private void prepareDataSet() {
		List<Float> data = level.getTrack();
		dataSet = new Vector2[data.size()/2];
		for(int i=0;i<dataSet.length;i++) {
			dataSet[i] = new Vector2(data.get(2*i), data.get(2*i+1));
		}
	}

	private void createSplines() {
		float k = level.getTrackSplines(); //increase k for more fidelity to the spline
		points = new Vector2[(int)k];
		CatmullRomSpline<Vector2> myCatmull = new CatmullRomSpline<Vector2>(dataSet, false);
		for(int i = 0; i < k; ++i) {
			points[i] = new Vector2();
			myCatmull.valueAt(points[i], i/k);
		}
		
		int kk = (int)k;
		spriteVertices = new float[kk*5*4];
		Vector2 p1,p2,kolm,previousKolm = null;
		for(int i = 0; i < kk-1; ++i) {
			p1 = points[i];
			p2 = points[i+1];
			kolm = new Vector2(p1.y-p2.y, p2.x-p1.x);
			kolm.nor();
			if(previousKolm == null)
				previousKolm = kolm;
			spriteVertices[20*i]   = Settings.TO_PIXELS * p1.x;
			spriteVertices[20*i+1] = Settings.TO_PIXELS * p1.y;
			spriteVertices[20*i+2] = Color.toFloatBits(255, 255, 255, 255);
			spriteVertices[20*i+3] = p1.x;
			spriteVertices[20*i+4] = 0;
			
			spriteVertices[20*i+5] = Settings.TO_PIXELS * (p1.x-previousKolm.x);
			spriteVertices[20*i+6] = Settings.TO_PIXELS * (p1.y-previousKolm.y);
			spriteVertices[20*i+7] = Color.toFloatBits(255, 255, 255, 255);
			spriteVertices[20*i+8] = p1.x;
			spriteVertices[20*i+9] = 1;
			
			spriteVertices[20*i+10] = Settings.TO_PIXELS * (p2.x-kolm.x);
			spriteVertices[20*i+11] = Settings.TO_PIXELS * (p2.y-kolm.y);
			spriteVertices[20*i+12] = Color.toFloatBits(255, 255, 255, 255);
			spriteVertices[20*i+13] = p2.x;
			spriteVertices[20*i+14] = 1;
			
			spriteVertices[20*i+15] = Settings.TO_PIXELS * p2.x;
			spriteVertices[20*i+16] = Settings.TO_PIXELS * p2.y;
			spriteVertices[20*i+17] = Color.toFloatBits(255, 255, 255, 255);
			spriteVertices[20*i+18] = p2.x;
			spriteVertices[20*i+19] = 0;
			
			previousKolm = kolm;
		}
		
		createFloorBoxes(k);
	}

	public Body getGround() {
		return groundBody;
	}

	public void draw(SpriteBatch batch) {
		batch.begin();
		batch.draw(trackTexture, spriteVertices, 0, spriteVertices.length);
		for(SupportPole pole:poles)
			pole.draw(batch);
		batch.draw(flagTexture,level.getFlagX()*Settings.TO_PIXELS, level.getFlagY()*Settings.TO_PIXELS);
		batch.end();
	}

	public void dispose() {
		trackTexture.dispose();
	}

	public Body getTrack() {
		return track.getBody();
	}

}
