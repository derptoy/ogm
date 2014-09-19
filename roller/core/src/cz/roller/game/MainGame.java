package cz.roller.game;

import java.util.LinkedList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

import cz.roller.game.person.Person;
import cz.roller.game.track.SupportPole;

public class MainGame implements Screen,InputProcessor {
	private SpriteBatch batch;
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private Cart cart;
	private boolean debugRender = true;
	private ShapeRenderer shapeRenderer;
	private Vector2[] points;
	private Vector2[] dataSet;
	private CatmullRomSpline spline;
	private Person person;
	private MouseJointDef jointDef;
	private Body groundBody;
	
	private Texture trackTexture;
	private Texture trackSupportTexture;
	private Texture trackSupportPoleTexture;
	private LinkedList<SupportPole> poles;
	
	private float[] spriteVertices;
	private enum State {START, NORMAL}
	private State state;
	
	private Vector3 cameraOffset = new Vector3(0,0,0);
	private Game game;
	
	public MainGame(Game game) {
		this.game = game;
	}

	@Override
	public void show() {
		camera = new OrthographicCamera(Gdx.graphics.getWidth() / 25, Gdx.graphics.getHeight() / 25);
		camera.position.y = -Gdx.graphics.getHeight() / 25;
		camera.update();
		batch = new SpriteBatch();
		state = State.START;
		
		world = new World(new Vector2(0, -9.81f), true); 
		debugRenderer = new Box2DDebugRenderer();
		
//		createBodies();
		createSplines();
		createGround();
		createRagdoll();
		cart = new Cart(1,2, world);
		Gdx.input.setInputProcessor(this);
		
		trackTexture = new Texture(Gdx.files.internal("track/track.png"));
		trackTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		trackTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		trackSupportTexture = new Texture(Gdx.files.internal("track/track.png"));
		trackSupportTexture = new Texture(Gdx.files.internal("track/support.png"));
		trackSupportPoleTexture = new Texture(Gdx.files.internal("track/supportPole.png"));
		
		createSupports();
		
		jointDef = new MouseJointDef();
		jointDef.bodyA = groundBody;
		jointDef.collideConnected = true;
		jointDef.maxForce = 500;
	}

	private void createSupports() {
		poles = new LinkedList<SupportPole>();
		for(int i=0;i<points.length;i+=4) {
			SupportPole pole = new SupportPole(points[i].x, points[i].y, trackSupportTexture, trackSupportPoleTexture);
			
			poles.add(pole);
		}
	}

	private void createRagdoll() {
		person = new Person(-5,5,0.5f,world);
	}

	private void createGround() {
//		// Create our body definition
//		BodyDef groundBodyDef = new BodyDef();  
//		// Set its world position
//		groundBodyDef.position.set(new Vector2(camera.viewportWidth/4, -6));
//		groundBodyDef.angle=0.0f;
//
//		// Create a body from the defintion and add it to the world
//		groundBody = world.createBody(groundBodyDef);  
//
//		// Create a polygon shape
//		PolygonShape groundBox = new PolygonShape();  
//		// Set the polygon shape as a box which is twice the size of our view port and 20 high
//		// (setAsBox takes half-width and half-height as arguments)
//		groundBox.setAsBox(camera.viewportWidth/4, 1.0f);
//		// Create a fixture from our polygon shape and add it to our ground body  
//		groundBody.createFixture(groundBox, 0.0f); 
//		// Clean up after ourselves
//		groundBox.dispose();
//		
//		BodyDef groundBodyDef2 = new BodyDef();  
//		// Set its world position
//		groundBodyDef2.position.set(new Vector2(-camera.viewportWidth/4, -6.6f));
//		groundBodyDef2.angle=0.1f;
//
//		// Create a body from the defintion and add it to the world
//		groundBody = world.createBody(groundBodyDef2);  
//		
//		groundBox = new PolygonShape();  
//		// Set the polygon shape as a box which is twice the size of our view port and 20 high
//		// (setAsBox takes half-width and half-height as arguments)
//		groundBox.setAsBox(camera.viewportWidth/4, 1.0f);
//		// Create a fixture from our polygon shape and add it to our ground body  
//		groundBody.createFixture(groundBox, 0.0f); 
//		// Clean up after ourselves
//		groundBox.dispose();
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
			groundBody.createFixture(groundBox, 0.0f); 
			// Clean up after ourselves
			groundBox.dispose();
		}
	}

	private void createBodies() {
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
		bodyDef.type = BodyType.DynamicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(5, 10);

		// Create our body in the world using our body definition
		Body body = world.createBody(bodyDef);

		// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(1f);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f; // Make it bounce a little bit

		// Create our fixture and attach it to the body
		Fixture fixture = body.createFixture(fixtureDef);

		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		circle.dispose();
	}

	private void createSplines() {
		float k = 40; //increase k for more fidelity to the spline
		points = new Vector2[(int)k];
		dataSet = new Vector2[6];
		dataSet[0] = new Vector2(-10,0);
		dataSet[1] = new Vector2(-10,5);
		dataSet[2] = new Vector2(0,0);
		dataSet[3] = new Vector2(5,5);
		dataSet[4] = new Vector2(10,8);
		dataSet[5] = new Vector2(-10,0);
		/*init()*/
		CatmullRomSpline<Vector2> myCatmull = new CatmullRomSpline<Vector2>(dataSet, false);
		Vector2 out = new Vector2();
		float step = 1/(float)k;
		for(int i = 0; i < k; ++i) {
			points[i] = new Vector2();
			myCatmull.valueAt(points[i], i/k);
		}
		
		int kk = (int)k;
//		int kk = 4;
		spriteVertices = new float[kk*5*4];
		Vector2 p1,p2,kolm,previousKolm = null;
		for(int i = 0; i < kk-1; ++i) {
			p1 = points[i];
			p2 = points[i+1];
			kolm = new Vector2(p1.y-p2.y, p2.x-p1.x);
			kolm.nor();
			if(previousKolm == null)
				previousKolm = kolm;
			spriteVertices[20*i]   = p1.x;
			spriteVertices[20*i+1] = p1.y;
			spriteVertices[20*i+2] = Color.toFloatBits(255, 255, 255, 255);
			spriteVertices[20*i+3] = i;
			spriteVertices[20*i+4] = 0;
			
			spriteVertices[20*i+5] = p1.x-previousKolm.x;
			spriteVertices[20*i+6] = p1.y-previousKolm.y;
			spriteVertices[20*i+7] = Color.toFloatBits(255, 255, 255, 255);
			spriteVertices[20*i+8] = i;
			spriteVertices[20*i+9] = 1;
			
			spriteVertices[20*i+10] = p2.x-kolm.x;
			spriteVertices[20*i+11] = p2.y-kolm.y;
			spriteVertices[20*i+12] = Color.toFloatBits(255, 255, 255, 255);
			spriteVertices[20*i+13] = i+1;
			spriteVertices[20*i+14] = 1;
			
			spriteVertices[20*i+15] = p2.x;
			spriteVertices[20*i+16] = p2.y;
			spriteVertices[20*i+17] = Color.toFloatBits(255, 255, 255, 255);
			spriteVertices[20*i+18] = i+1;
			spriteVertices[20*i+19] = 0;
			
			previousKolm = kolm;
		}
		
//		spriteVertices[4*0]   = 0;
//		spriteVertices[4*0+1] = 1.0f;
//		spriteVertices[4*0+2] = Color.toFloatBits(255, 255, 255, 255);
//		spriteVertices[4*0+3] = 0;
//		spriteVertices[4*0+4] = 0;
//		
//		spriteVertices[5*1]   = 0;
//		spriteVertices[5*1+1] = 0.0f;
//		spriteVertices[5*1+2] = Color.toFloatBits(255, 255, 255, 255);
//		spriteVertices[5*1+3] = 0;
//		spriteVertices[5*1+4] = 1;
//		
//		spriteVertices[5*2]   = 10;
//		spriteVertices[5*2+1] = 0.0f;
//		spriteVertices[5*2+2] = Color.toFloatBits(255, 255, 255, 255);
//		spriteVertices[5*2+3] = 10;
//		spriteVertices[5*2+4] = 1;
//		
//		spriteVertices[5*3]   = 10;
//		spriteVertices[5*3+1] = 1.0f;
//		spriteVertices[5*3+2] = Color.toFloatBits(255, 255, 255, 255);
//		spriteVertices[5*3+3] = 10;
//		spriteVertices[5*3+4] = 0;
//		
//		int o = 4;
//		
//		spriteVertices[5*o]   = 10;
//		spriteVertices[5*o+1] = 0.0f;
//		spriteVertices[5*o+2] = Color.toFloatBits(255, 255, 255, 255);
//		spriteVertices[5*o+3] = 10;
//		spriteVertices[5*o+4] = 1;
//		
//		o++;
//		spriteVertices[5*o]   = 10;
//		spriteVertices[5*o+1] = 1.0f;
//		spriteVertices[5*o+2] = Color.toFloatBits(255, 255, 255, 255);
//		spriteVertices[5*o+3] = 10;
//		spriteVertices[5*o+4] = 0;
//		
//		o++;
//		spriteVertices[5*o]   = 11;
//		spriteVertices[5*o+1] = 2.0f;
//		spriteVertices[5*o+2] = Color.toFloatBits(255, 255, 255, 255);
//		spriteVertices[5*o+3] = 11;
//		spriteVertices[5*o+4] = 0;
//		
//		o++;
//		spriteVertices[5*o]   = 11;
//		spriteVertices[5*o+1] = 1.0f;
//		spriteVertices[5*o+2] = Color.toFloatBits(255, 255, 255, 255);
//		spriteVertices[5*o+3] = 11;
//		spriteVertices[5*o+4] = 1;
		
		createFloorBoxes(k);

		shapeRenderer = new ShapeRenderer();
	}
	
	@Override
	public void render(float delta) {
		if(state == State.NORMAL) {
			camera.position.set(cameraOffset.x, cameraOffset.y, 0);
			
			Vector2 cartPos = cart.getBody().getPosition();
			Vector3 dist = new Vector3(cameraOffset.x-cartPos.x, cameraOffset.y-cartPos.y, 0);
			if( dist.len() > 5 ) {
				dist.scl(-0.01f);
				cameraOffset.add(dist);
			}
		} else {
			camera.position.y += 0.5f;
			
			if(camera.position.y >= 0) {
				state = State.NORMAL;
				camera.position.y = 0;
			}
		}
		
		camera.update();

//		Gdx.gl.glClearColor(0.8f, 1, 1, 1);
//		Gdx.gl.glClearColor(0.0f, 0.5f, 0.5f, 1);
		Gdx.gl.glClearColor(157/255.0f, 249/255.0f, 255/255.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		
		cart.render(batch);
		
		if(debugRender)
			debugRenderer.render(world, camera.combined);
		
		batch.begin();
		batch.draw(trackTexture, spriteVertices, 0, spriteVertices.length);
		for(SupportPole pole:poles)
			pole.draw(batch);
		batch.end();
		
		batch.begin();
		person.draw(batch);
		batch.end();
		
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
		
		world.step(1/60f, 8, 3);
		
//		shapeRenderer.setProjectionMatrix(camera.combined);
//		shapeRenderer.setColor(1, 1, 1, 1);
//		shapeRenderer.begin(ShapeType.Line);
//		for(int i=0;i<points.length-1;i++)
//			shapeRenderer.line(points[i], points[i+1]);
//		shapeRenderer.end();
		
//		shapeRenderer.setProjectionMatrix(camera.combined);
//		shapeRenderer.setColor(1, 0, 0, 1);
//		shapeRenderer.begin(ShapeType.Filled);
//		for(int i=0;i<dataSet.length;i++)
//			shapeRenderer.circle(dataSet[i].x, dataSet[i].y, 0.3f);
//		shapeRenderer.end();
//		shapeRenderer.setProjectionMatrix(camera.combined);
//		shapeRenderer.setColor(1, 0, 0, 1);
//		shapeRenderer.begin(ShapeType.Point);
//		for(int i=0;i<points.length;i++)
//			shapeRenderer.point(points[i].x, points[i].y,0);
//		shapeRenderer.end();
	}
	
	@Override
	public void resize (int width, int height) {
		camera.viewportWidth=Gdx.graphics.getWidth() / 25;
		camera.viewportHeight=Gdx.graphics.getHeight() / 25;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(Keys.A == keycode) {
			cart.impuls(1);
		} else if(Keys.D == keycode) {
			cart.impuls(-1);
		} else if(Keys.SPACE == keycode) {
		} else if(Keys.K == keycode) {
			debugRender = !debugRender;
		} else if(Keys.Q == keycode) {
			camera.zoom += 0.1f;
		} else if(Keys.E == keycode) {
			camera.zoom -= 0.1f;
		} else if(Keys.L == keycode) {
			person.testLimb();
		} else if(Keys.ESCAPE == keycode) {
			game.setScreen(new MainMenu(game));
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		cart.impuls(0);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	private Vector3 tmp = new Vector3();
	protected MouseJoint joint;
	private Vector2 tmp2 = new Vector2();

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(joint != null) {
			world.destroyJoint(joint);
			joint = null;
		}
		
		camera.unproject(tmp.set(screenX, screenY, 0));
		world.QueryAABB(new QueryCallback() {
			
			@Override
			public boolean reportFixture(Fixture fixture) {
				if(fixture.testPoint(tmp.x, tmp.y)) {
					jointDef.bodyB = fixture.getBody();
					jointDef.target.set(tmp.x,tmp.y);
					joint = (MouseJoint)world.createJoint(jointDef);
					return false;
				}
				return true;
			}
		}, tmp.x, tmp.y, tmp.x, tmp.y);
		return true;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(joint == null)
			return true;
		
		camera.unproject(tmp.set(screenX,screenY, 0));
		joint.setTarget(tmp2.set(tmp.x, tmp.y));
		return true;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(joint != null) {
			world.destroyJoint(joint);
			joint = null;
		}
		return true;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		world.dispose();
		trackTexture.dispose();
		person.dispose();
		cart.dispose();
	}
	
}
