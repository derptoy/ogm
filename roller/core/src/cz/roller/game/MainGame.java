package cz.roller.game;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import cz.roller.game.cart.Cart;
import cz.roller.game.level.Level;
import cz.roller.game.menu.LevelSelector;
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
	private boolean debugRender = false;
	@SuppressWarnings("unused")
	private ShapeRenderer shapeRenderer;
	private MouseJointDef jointDef;
	
	private enum State {START, NORMAL, HIDE, END, ENDING}
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
	private LinkedList<Joint> toRemove;
	private HUDStage hud;
	private Level level;
	
	public MainGame(Game game, Level lvl) {
		this.game = game;
		this.level = lvl;
	}

	@Override
	public void show() {
		AssetManager.waitToLoaded();
		System.out.println("Loaded assets");
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.x = -Gdx.graphics.getWidth()/2;
		camera.position.y = Gdx.graphics.getHeight()/2;
		
		cameraOffset.x = Gdx.graphics.getWidth()/2;
		cameraOffset.y = Gdx.graphics.getHeight()/2;
//		camera.zoom = 0.2f;
		
		camera.update();
		batch = new SpriteBatch();
		state = State.START;
		
		endStage = new EndStage(level, new EndGameListener() {
			
			@Override
			public void endGame() {
				state = State.END;
				cart.impuls(0);
				cart2.impuls(0);
			}
		});
//		endStage.getViewport().setCamera(camera);
		
		world = new World(new Vector2(0, -9.81f), true); 
		debugRenderer = new Box2DDebugRenderer();
		
//		createBodies();
		// *************************** CART
		cart = new Cart(7.2f,level.getStarty(), world);
		cart2 = new Cart(5,level.getStarty(), world);
//		cart2.setTint(new Color(0.5f,0.5f,0.5f,1));
		cart.connect(cart2, world);
		
		Gdx.input.setInputProcessor(this);
		
		map = new Map(world, level);
		
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
		bombEffect.scaleEffect(0.1f);
		bombEffectPool = new ParticleEffectPool(bombEffect, 1, 2);
		
		shapeRenderer = new ShapeRenderer();
		
		registerBloodListener();
		
		toRemove = new LinkedList<Joint>();
		hud = new HUDStage(level);
		ScoreManager.reset();
	}

	private void registerBloodListener() {
		world.setContactListener(new ContactListener() {
			
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				
			}
			
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				if(state == State.ENDING)
					return;
					
				Body cBody = null;
				
				if(contact.getFixtureB().getUserData() != null
						&& contact.getFixtureB().getUserData().equals(Type.PERSON)
						&& (contact.getFixtureA().getUserData() == null || !contact.getFixtureA().getUserData().equals(Type.PERSON)))
						cBody = contact.getFixtureB().getBody();
				if(	contact.getFixtureA().getUserData() != null
						&& contact.getFixtureA().getUserData().equals(Type.PERSON)
						&& (contact.getFixtureB().getUserData() == null || !contact.getFixtureB().getUserData().equals(Type.PERSON)))
					cBody = contact.getFixtureA().getBody();
				
				float limit = 1.2f;
				if(cBody != null && impulse.getNormalImpulses()[0] >limit) {
					Vector2 vec = contact.getWorldManifold().getPoints()[0];
					
					BodyDamage dmg = ((BodyDamage)cBody.getUserData());
					for(JointEdge joint:cBody.getJointList()) {
						if(!Type.PERSON_DECAPITATED.equals(joint.joint.getUserData())
								&& dmg.damage < 500) {
							
							// DMG
							float ll = (impulse.getNormalImpulses()[0] - limit) / 0.01f;
							dmg.damage += ll;
							ScoreManager.doneDamage(ll);
							
							toRemove.add(joint.joint);
//							cBody.setUserData(Type.PERSON_DECAPITATED);
							joint.joint.setUserData(Type.PERSON_DECAPITATED);
							
							PooledEffect effect = bombEffectPool.obtain();
							effect.reset();
							effect.start();
							effect.setPosition(vec.x*Settings.TO_PIXELS, vec.y*Settings.TO_PIXELS);
							effects.add(effect);
						}
					}
				}
				
			}
			
			@Override
			public void endContact(Contact contact) {
				
			}
			
			@Override
			public void beginContact(Contact contact) {
				if((contact.getFixtureA().getFilterData().categoryBits == Settings.CATEGORY_END_OF_LEVEL) 
						|| (contact.getFixtureB().getFilterData().categoryBits == Settings.CATEGORY_END_OF_LEVEL)) {
					state = State.ENDING;
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
					&& cameraOffset.x <= level.getLevelWidth() - Gdx.graphics.getWidth()/2) {
				dist.scl(-0.02f);
//				dist.scl(-0.1f);
				cameraOffset.add(dist);
			}
			if(cameraOffset.x < Gdx.graphics.getWidth()/2)
				cameraOffset.x = Gdx.graphics.getWidth()/2;
			else if(cameraOffset.x > level.getLevelWidth() - Gdx.graphics.getWidth()/2)
				cameraOffset.x = level.getLevelWidth() - Gdx.graphics.getWidth()/2;
			if(cameraOffset.y < Gdx.graphics.getHeight()/2)
				cameraOffset.y = Gdx.graphics.getHeight()/2;
		} else if(state == State.START){
			camera.position.x += Gdx.graphics.getWidth()/20f;
			
			if(camera.position.x >= Gdx.graphics.getWidth()/2) {
				state = State.NORMAL;
				simulate = true;
				cameraOffset.x = Gdx.graphics.getWidth()/2;
				camera.position.x = Gdx.graphics.getWidth()/2;
			}
		} else if(state == State.END){
			camera.position.y += Gdx.graphics.getHeight()/20f;
			if(camera.position.y >= 2.0f*Gdx.graphics.getHeight()) {
				game.setScreen(new LevelSelector(game));
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
		
		personManager.draw(batch);
		
		map.draw(batch);
		
		if(simulate)
			world.step(1/60f, 8, 3);
		
		batch.begin();
		// Update and draw effects:
		for (int i = effects.size - 1; i >= 0; i--) {
		    PooledEffect effect = effects.get(i);
		    effect.draw(batch, delta);
		    if (effect.isComplete()) {
		        effect.free();
		        effects.removeIndex(i);
		    }
		}
		batch.end();
		
		if(state == State.ENDING) {
			endStage.act();
		}
		if(state != State.END)
			endStage.draw();
		
		Iterator<Joint> it = toRemove.iterator();
		while(it.hasNext()) {
			Joint j = it.next();
			world.destroyJoint(j);
			it.remove();
			
			ScoreManager.lostLimb();
		}
		
		if(state != State.END) {
			hud.draw();
			hud.update();
		}
		
		if(state != State.END
				&& state != State.ENDING
				&& state != State.START)
			ScoreManager.updateTime(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void resize (int width, int height) {
		camera.viewportWidth=Gdx.graphics.getWidth();
		camera.viewportHeight=Gdx.graphics.getHeight();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(Keys.A == keycode && state == State.NORMAL) {
			cart.impuls(1);
			cart2.impuls(1);
		} else if(Keys.D == keycode && state == State.NORMAL) {
			cart.impuls(-1);
			cart2.impuls(-1);
		} 
		else if(Keys.K == keycode) {
			debugRender = !debugRender;
		} 
		else if(Keys.ESCAPE == keycode) {
			state = State.END;
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
