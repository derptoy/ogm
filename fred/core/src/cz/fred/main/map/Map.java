package cz.fred.main.map;

import java.awt.Point;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import cz.fred.main.graphics.Drawable;
import cz.fred.main.hero.Hero;
import cz.fred.main.objects.Gem;
import cz.fred.main.objects.Gem.State;
import cz.fred.main.util.CustomRect;
import cz.fred.main.util.Settings;

public class Map {
	
	public static final int MAP_X_TILECOUNT = 100;
	public static final int MAP_Y_TILECOUNT = 100;
	public static final int TILE_SIZE = 32;
	
	public static final float TO_PIXEL = 32f;
	
	private static boolean DEBUG = false;
	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
	private TiledMapTileLayer collisionLayer;
	private TiledMapTileLayer groundLayer;
	private Array<Gem> gems;
	private World world;
	private Array<Drawable> drawingArray;
	
	private ShapeRenderer shaper;

	public Map(World world, SpriteBatch batch, OrthographicCamera camera) {
		this.world = world;
		gems = new Array<Gem>();
		
		tiledMap = new TmxMapLoader().load(Gdx.files.internal("map/mapa.tmx").path());
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap,batch);
		tiledMapRenderer.setView(camera);
		
		collisionLayer = (TiledMapTileLayer)tiledMap.getLayers().get("collision");
		groundLayer = (TiledMapTileLayer)tiledMap.getLayers().get("floor");
		
		shaper = new ShapeRenderer();
		shaper.setProjectionMatrix(camera.combined);
		
		createCollisions();
		
		drawingArray = new Array<Drawable>();
		
		Gem gem = new Gem(world, 96, 96);
		gems.add(gem);
		addDrawable(gem);
		
		Gem gem2 = new Gem(world, 186, 96);
		gems.add(gem2);
		addDrawable(gem2);
	}
	
	private void createCollisions() {
		MapLayer layer = tiledMap.getLayers().get("box2dCollisions");
		for(MapObject object:layer.getObjects()) {
			if(object instanceof PolylineMapObject) {
				PolylineMapObject polyLine = (PolylineMapObject)object;
				float[] vertices = polyLine.getPolyline().getVertices();
				for(int i=0;i<vertices.length-2;i+=2) {
					createLine(	polyLine.getPolyline().getX()+vertices[i], (polyLine.getPolyline().getY()+vertices[i+1]),
								polyLine.getPolyline().getX()+vertices[i+2], (polyLine.getPolyline().getY()+vertices[i+3]));
				}
			}
		}
	}

	//	public void render() {
//		tiledMapRenderer.render();
//	}
	
	private void createLine(float x1, float y1, float x2, float y2) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		EdgeShape shape = new EdgeShape();
		shape.set(x1/Map.TO_PIXEL, y1/Map.TO_PIXEL, x2/Map.TO_PIXEL, y2/Map.TO_PIXEL);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		Body body = world.createBody(bodyDef);
		Fixture fixture = body.createFixture(fixtureDef);
	}

	public void renderGround(SpriteBatch batch) {
		tiledMapRenderer.renderTileLayer(groundLayer);
	}
	
	public void renderCollision() {
		if(DEBUG)
			tiledMapRenderer.renderTileLayer(collisionLayer);
	}

	public void setProjectionMatrix(OrthographicCamera camera) {
		tiledMapRenderer.setView(camera);
	}
	
//	public boolean terrainCollisionX(Rectangle spriteRect, Vector2 direction) {
//		int xpart = (int)(spriteRect.x+spriteRect.width/2)/TILE_SIZE;
//		int ypart = (int)(spriteRect.y+spriteRect.height/2)/TILE_SIZE;
//		
//		return terrainCollision(spriteRect, UtilityFunctions.getSurrounding(xpart, ypart, direction));
//	}
//	
//	public boolean terrainCollisionY(Rectangle spriteRect, Vector2 direction) {
//		int xpart = (int)(spriteRect.x+spriteRect.width/2)/TILE_SIZE;
//		int ypart = (int)(spriteRect.y+spriteRect.height/2)/TILE_SIZE;
//		
//		return terrainCollision(spriteRect, UtilityFunctions.getSurrounding(xpart, ypart));
//	}
	
	public boolean terrainCollision(CustomRect heroRect, Point[] points) {
		for(Point p: points) {
			if(testCollide(heroRect, p))
				return true;
		}
		
		return false;
	}

	private boolean testCollide(CustomRect heroRect, Point point) {
		if(point.x >= 0 && point.x < MAP_X_TILECOUNT && point.y >= 0 && point.y < MAP_Y_TILECOUNT) {
//			boolean collisionTile = layer.getCell(point.x, point.y).getTile().getProperties().get("collision").toString().compareTo("true")==0;
			boolean collisionTile = collisionLayer.getCell(point.x, point.y) != null 
										&& collisionLayer.getCell(point.x, point.y).getTile() != null
										&& collisionLayer.getCell(point.x, point.y).getTile().getProperties().containsKey("collision");
			return collisionTile 
					&& aabb(heroRect, point.x,point.y);
		} else
			return false;
	}


	private boolean aabb(CustomRect heroRect, int xlocal, int ylocal) {
		CustomRect target = new CustomRect("",xlocal*TILE_SIZE, ylocal*TILE_SIZE, TILE_SIZE, TILE_SIZE);
		return collisionCheck(heroRect, target);
	}

	private boolean collisionCheck(CustomRect rect1, CustomRect rect2) {
		float lx = Math.abs(rect1.x+rect1.width/2.0f - (rect2.x+rect2.width/2.0f));
		float sumx = (rect1.width / 2.0f) + (rect2.width / 2.0f);

		float ly = Math.abs(rect1.y+rect1.height/2.0f - (rect2.y+rect2.height/2.0f));
		float sumy = (rect1.height / 2.0f) + (rect2.height / 2.0f);

		return (lx <= sumx && ly <= sumy);
	}

	public boolean collisionTile(int indexX, int indexY) {
		boolean collisionTile = collisionLayer.getCell(indexX, indexY) != null 
				&& collisionLayer.getCell(indexX, indexY).getTile() != null
				&& collisionLayer.getCell(indexX, indexY).getTile().getProperties().containsKey("collision");
		return collisionTile;
	}

	public void toggleDebug() {
		DEBUG = !DEBUG;
	}

	public void update() {
		for(Gem gem:gems)
			gem.update();
	}

	public void renderObjects(SpriteBatch batch) {
//		for(Gem gem:gems)
//			gem.draw(batch);
		drawingArray.sort(Collections.reverseOrder());
		for(Drawable drawable:drawingArray)
			drawable.draw(batch);
	}
	
	public void renderDebug() {
		if(Settings.BOX2D_DEBUG) {
			for(Gem gem:gems) {
				CustomRect rectangle = gem.getPickRectangle();
				shaper.setColor(Color.GRAY);
				shaper.begin(ShapeType.Line);
				shaper.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
				shaper.end();
				shaper.setColor(Color.WHITE);
			}
		}
	}

	public void addDrawable(Drawable value) {
		drawingArray.add(value);
	}

	public void attractGems(Hero hero) {
		for(Gem gem:gems) {
			float gemX = gem.getX();
			float gemY = gem.getY();
			
			float heroX = hero.getX();
			float heroY = hero.getY();
			
			Vector2 distance = new Vector2(heroX - gemX, heroY - gemY);
			distance.scl(0.8f, 1f);
			if(distance.len() < 50 && distance.len() > 32) {
				gem.addForce(distance.scl(0.1f));
				gem.setState(State.HERO);
			} else if(distance.len() >= 50) {
				gem.setState(State.INDEPENDENT);
			}
		}
	}

	public Gem getPressedGem(int screenX, int screenY) {
		Gem pressedGem = null;
		for(Gem gem:gems) {
			CustomRect rectangle = gem.getPickRectangle();
			if(rectangle.contains(screenX, screenY)) {
				if(pressedGem == null || gem.getComparingY() < pressedGem.getComparingY())
				pressedGem = gem;
			}
		}
		return pressedGem;
	}

}
