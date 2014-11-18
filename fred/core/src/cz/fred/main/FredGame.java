package cz.fred.main;

import java.util.ArrayList;

import sun.tracing.MultiplexProviderFactory;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

import cz.fred.main.enemies.Enemy;
import cz.fred.main.enemies.EnemyManager;
import cz.fred.main.hero.Hero;
import cz.fred.main.map.Map;
import cz.fred.main.objects.Gem;
import cz.fred.main.objects.Gem.State;
import cz.fred.main.physics.FixtureDefFactory;
import cz.fred.main.util.Direction;
import cz.fred.main.util.Settings;

public class FredGame extends ApplicationAdapter  implements InputProcessor {
	SpriteBatch batch;
	private OrthographicCamera camera;
	private cz.fred.main.map.Map map;
	private Hero hero;
	private ShapeRenderer shaper;
	
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private Matrix4 matrix;
	private Body body;
	private Fixture fixture;
	
	private ArrayList<Enemy> enemies;
	private EnemyManager enemyManager;
	private Vector3 tmp = new Vector3();
	
	private boolean aiming = false;
	private Vector2 pressedPoint = new Vector2();
	private Gem pressedGem = null;
	private Vector2 actualAimPoint = new Vector2();
	
	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();;
		
		Assets.init();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false,w,h);
//		camera.position.x = 1300;
//		camera.position.y = 2200;
		camera.zoom = 1.0f;
		camera.update();
		
		world = new World(new Vector2(0, 0f), true);
		debugRenderer = new Box2DDebugRenderer();
		matrix = camera.combined.cpy().scl(32);
		FixtureDefFactory.init();
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		
		map = new Map(world, batch, camera);
		
		hero = new Hero(world,map);
		hero.setPosition(150,100);
		shaper = new ShapeRenderer();
		shaper.setProjectionMatrix(camera.combined);
		
		enemies = new ArrayList<Enemy>();
		Enemy enemy = new Enemy(world, map);
		enemy.setPosition(200, 70);
		enemies.add(enemy);
		enemyManager = new EnemyManager(hero, enemies, world);
		
		map.addDrawable(hero);
		map.addDrawable(enemy);
		
		Gdx.input.setInputProcessor(new InputMultiplexer(hero,this));
		
//		BodyDef bodyDef = new BodyDef();
//		bodyDef.type = BodyType.StaticBody;
//		EdgeShape shape = new EdgeShape();
//		shape.set(50/Map.TO_PIXEL, 50/Map.TO_PIXEL, 50/Map.TO_PIXEL, 500/Map.TO_PIXEL);
//		FixtureDef fixtureDef = new FixtureDef();
//		fixtureDef.shape = shape;
//		fixtureDef.density = 10f; 
//		fixtureDef.friction = 0.3f;
//		fixtureDef.restitution = 0; // Make it bounce a little bit
//		body = world.createBody(bodyDef);
//		fixture = body.createFixture(fixtureDef);
	}

	@Override
	public void render () {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		map.renderGround(batch);
		map.renderCollision();
		map.renderObjects(batch);
		batch.end();
		
		map.renderDebug();
		
		if(aiming) {
			Gdx.gl20.glLineWidth(4 / camera.zoom);
			shaper.setColor(Color.GREEN);
			pressedPoint.set(pressedGem.getX(), pressedGem.getY());
			shaper.begin(ShapeType.Line);
			shaper.line(pressedPoint, actualAimPoint);
			shaper.end();
			Gdx.gl20.glLineWidth(1 / camera.zoom);
		}
		
//		batch.begin();
//		hero.draw(batch);
//		batch.end();
		
		if(Settings.BOX2D_DEBUG)
			debugRenderer.render(world, matrix);
		
		world.step(1/60f, 8, 3);
		
		hero.update();
		map.update();
		
		enemyManager.update();
//		shaper.setColor(Color.BLUE);
//		shaper.begin(ShapeType.Line);
//		shaper.rect(hero.getX()+16,hero.getY()+8,32,4);
//		shaper.end();
	}
	
	@Override
	public void dispose() {
		Assets.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.K)
			Settings.BOX2D_DEBUG = !Settings.BOX2D_DEBUG;
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == 0) {
			camera.unproject(tmp.set(screenX, screenY, 0));
			Gem gem = map.getPressedGem((int)tmp.x, (int)tmp.y);
			System.out.println("Gem "+gem);
			if(gem != null && gem.getState() == State.HERO) {
				aiming = true;
				pressedGem = gem;
				pressedPoint.set(gem.getX(), gem.getY());
				actualAimPoint.set(gem.getX(), gem.getY());
			}
		} else if(button == 1) {
			aiming = false;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(aiming) {
			pressedPoint.set(pressedGem.getX(), pressedGem.getY());
			pressedPoint.sub(actualAimPoint);
			pressedPoint.nor();
			pressedGem.addForce(pressedPoint.scl(800));
		}
		aiming = false;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(aiming) {
			camera.unproject(tmp.set(screenX, screenY, 0));
			actualAimPoint.set(tmp.x, tmp.y);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
