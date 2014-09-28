package cz.roller.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.Array;

import cz.roller.game.cart.Cart;
import cz.roller.game.menu.MainMenu;
import cz.roller.game.person.PersonManager;
import cz.roller.game.util.AssetManager;
import cz.roller.game.world.Settings;
import cz.roller.game.world.Type;

public class MainGame implements Screen,InputProcessor {
	private SpriteBatch batch;
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private Cart cart;
	private Cart cart2;
	private boolean debugRender = true;
	@SuppressWarnings("unused")
	private ShapeRenderer shapeRenderer;
	private MouseJointDef jointDef;
	
	private enum State {START, NORMAL, HIDE}
	private State state;
	
	private Vector3 cameraOffset = new Vector3(0,0,0);
	private Game game;
	private TestStage stage;
	private EndStage endStage;
	private Array<PooledEffect> effects;
	private ParticleEffectPool bombEffectPool;
	private Map map;
	private PersonManager personManager;
	private boolean simulate;
	
	public MainGame(Game game) {
		this.game = game;
	}

	@Override
	public void show() {
		AssetManager.waitToLoaded();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		camera.position.x = Gdx.graphics.getWidth()/2;
		camera.position.x = -Gdx.graphics.getWidth()/2;
		camera.position.y = Gdx.graphics.getHeight()/2;
		
		cameraOffset.x = Gdx.graphics.getWidth()/2;
		cameraOffset.y = Gdx.graphics.getHeight()/2;
//		camera.zoom = 0.2f;
		
		camera.update();
		batch = new SpriteBatch();
		state = State.START;
		
		endStage = new EndStage();
//		endStage.getViewport().setCamera(camera);
		
		world = new World(new Vector2(0, -9.81f), true); 
		debugRenderer = new Box2DDebugRenderer();
		
//		createBodies();
		// *************************** CART
		cart = new Cart(7.2f,11, world);
		cart2 = new Cart(5,11, world);
//		cart2.setTint(new Color(0.5f,0.5f,0.5f,1));
		cart.connect(cart2, world);
		
		Gdx.input.setInputProcessor(this);
		
		map = new Map(world);
		
		createRagdoll();
		
		jointDef = new MouseJointDef();
		jointDef.bodyA = map.getGround();
		jointDef.collideConnected = true;
		jointDef.maxForce = 500;
		
		stage = new TestStage();
		stage.getViewport().setCamera(camera);
		
		effects = new Array<PooledEffect>();
		
		ParticleEffect bombEffect = new ParticleEffect();
		bombEffect.load(Gdx.files.internal("particles/blood.dt"), Gdx.files.internal("particles/"));
		bombEffectPool = new ParticleEffectPool(bombEffect, 1, 2);
		
		PooledEffect effect = bombEffectPool.obtain();
		effect.setPosition(10, 10);
		effect.scaleEffect(0.1f);
		effects.add(effect);
		
		shapeRenderer = new ShapeRenderer();
		
		registerBloodListener();
	}

	private void registerBloodListener() {
		world.setContactListener(new ContactListener() {
			
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				
			}
			
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}
			
			@Override
			public void endContact(Contact contact) {
				
			}
			
			@Override
			public void beginContact(Contact contact) {
				Body cBody = null;
				
				if(contact.getFixtureA().getBody().equals(map.getTrack()))
					cBody = contact.getFixtureB().getBody();
				else if(contact.getFixtureB().getBody().equals(map.getTrack()))	
					cBody = contact.getFixtureA().getBody();
				
				if(contact.getFixtureA().getUserData() != null
						&& contact.getFixtureB().getUserData() != null) {
					if(	contact.getFixtureA().getUserData().equals(Type.TRACK)
							&& contact.getFixtureB().getUserData().equals(Type.PERSON))
						cBody = contact.getFixtureB().getBody();
					if(	contact.getFixtureB().getUserData().equals(Type.TRACK)
							&& contact.getFixtureA().getUserData().equals(Type.PERSON))
						cBody = contact.getFixtureA().getBody();
				}
				
				if(cBody != null && cBody.getLinearVelocity().len() > 5f) {
					System.out.println("Speed: "+cBody.getLinearVelocity().len());
					Vector2 vec = contact.getWorldManifold().getPoints()[0];
			        
					
					
					PooledEffect effect = bombEffectPool.obtain();
					effect.scaleEffect(0.1f);
					effect.reset();
					effect.start();
					effect.setPosition(vec.x*Settings.TO_PIXELS, vec.y*Settings.TO_PIXELS);
					effects.add(effect);
				}
				
			}
		});
	}

	private void createRagdoll() {
		personManager = new PersonManager();
		cart.addPerson(personManager, world);
		cart2.addPerson(personManager, world);
	}
	
	@Override
	public void render(float delta) {
		if(state == State.NORMAL) {
			camera.position.set(cameraOffset.x, cameraOffset.y, 0);
//			cameraOffset.x = 150;
//			cameraOffset.y = 280;
//			camera.zoom = 0.2f;
			Vector2 cartPos = cart.getBody().getPosition();
			Vector3 dist = new Vector3(cameraOffset.x-cartPos.x*Settings.TO_PIXELS, cameraOffset.y-cartPos.y*Settings.TO_PIXELS, 0);
			if( dist.len() > 100 
					&& cameraOffset.x >= Gdx.graphics.getWidth()/2 
					&& cameraOffset.y >= Gdx.graphics.getHeight()/2
					&& cameraOffset.x <= 2400-Gdx.graphics.getWidth()/2) {
				dist.scl(-0.01f);
//				dist.scl(-0.1f);
				cameraOffset.add(dist);
			}
			if(cameraOffset.x < Gdx.graphics.getWidth()/2)
				cameraOffset.x = Gdx.graphics.getWidth()/2;
			else if(cameraOffset.x > 2400-Gdx.graphics.getWidth()/2)
				cameraOffset.x =2400-Gdx.graphics.getWidth()/2;
			if(cameraOffset.y < Gdx.graphics.getHeight()/2)
				cameraOffset.y = Gdx.graphics.getHeight()/2;
		} else if(state == State.START){
			camera.position.x += Gdx.graphics.getWidth()/20f;
			
			if(camera.position.x >= Gdx.graphics.getWidth()/2) {
				state = State.NORMAL;
				cameraOffset.x = Gdx.graphics.getWidth()/2;
				camera.position.x = Gdx.graphics.getWidth()/2;
			}
		} else {
			camera.position.y += Gdx.graphics.getHeight()/20f;
			if(camera.position.y >= 2.0f*Gdx.graphics.getHeight()) {
				game.setScreen(new MainMenu(game));
			}
		}
		
		camera.update();

//		Gdx.gl.glClearColor(0.8f, 1, 1, 1);
//		Gdx.gl.glClearColor(0.0f, 0.5f, 0.5f, 1);
		Gdx.gl.glClearColor(157/255.0f, 249/255.0f, 255/255.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		
		stage.act();
		stage.draw();
		
		cart2.render(batch);
		cart.render(batch);
		
		if(debugRender)
			debugRenderer.render(world, camera.combined.cpy().scl(Settings.TO_PIXELS));
		
		map.draw(batch);
		
		personManager.draw(batch);
		
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
		
		if(simulate)
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
		batch.begin();
		// Update and draw effects:
		for (int i = effects.size - 1; i >= 0; i--) {
		    PooledEffect effect = effects.get(i);
		    effect.draw(batch, delta);
		    if (effect.isComplete()) {
		        effect.free();
		        effects.removeIndex(i);
		    	
//		        Vector2 vec = person.getPosition();
//		        
//		    	effect = bombEffectPool.obtain();
//		    	effect.reset();
//		    	effect.start();
//				effect.setPosition(vec.x, vec.y);
//				effects.add(effect);
//				System.out.println("Blood");
		    }
		}
		batch.end();
		
		if(simulate)
			endStage.act();
		endStage.draw();
	}
	
	@Override
	public void resize (int width, int height) {
		camera.viewportWidth=Gdx.graphics.getWidth();
		camera.viewportHeight=Gdx.graphics.getHeight();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(Keys.A == keycode) {
			cart.impuls(1);
			cart2.impuls(1);
		} else if(Keys.D == keycode) {
			cart.impuls(-1);
			cart2.impuls(-1);
		} else if(Keys.SPACE == keycode) {
		} else if(Keys.K == keycode) {
			debugRender = !debugRender;
		} else if(Keys.Q == keycode) {
			camera.zoom += 0.1f;
		}  else if(Keys.S == keycode) {
			simulate = !simulate;
		} else if(Keys.F == keycode) {
			world.step(1/60f, 8, 3);
		} else if(Keys.E == keycode) {
			camera.zoom -= 0.1f;
		} else if(Keys.ESCAPE == keycode) {
			state = State.HIDE;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		cart.impuls(0);
		cart2.impuls(0);
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
				if(fixture.testPoint(	tmp.x/Settings.TO_PIXELS, 
										tmp.y/Settings.TO_PIXELS)) {
					jointDef.bodyA = map.getGround();
					jointDef.bodyB = fixture.getBody();
					jointDef.target.set(	tmp.x/Settings.TO_PIXELS,
											tmp.y/Settings.TO_PIXELS);
					System.out.println("Target "+jointDef.target);
					joint = (MouseJoint)world.createJoint(jointDef);
					return false;
				}
				return true;
			}
		}, tmp.x/Settings.TO_PIXELS, tmp.y/Settings.TO_PIXELS, tmp.x/Settings.TO_PIXELS, tmp.y/Settings.TO_PIXELS);
		return true;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(joint == null)
			return true;
		
		camera.unproject(tmp.set(screenX,screenY, 0));
		joint.setTarget(tmp2.set(tmp.x/Settings.TO_PIXELS, tmp.y/Settings.TO_PIXELS));
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
		map.dispose();
		personManager.dispose();
		cart.dispose();
		stage.dispose();
	}
	
}
