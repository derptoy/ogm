package cz.surwild.main;

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import cz.surwild.harvestable.Tree;
import cz.surwild.util.CustomRect;

public class Map {
	
	public static final int MAP_X_TILECOUNT = 100;
	public static final int MAP_Y_TILECOUNT = 100;
	public static final int TILE_SIZE = 32;
	
	private static boolean DEBUG = true;
	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
	private TiledMapTileLayer collisionLayer;
	private TiledMapTileLayer groundLayer;
	private TiledMapTileLayer folliageLayer;
	private TiledMapTileLayer walkableLayer;
	private MapLayer treeLayer;
	
	private LinkedList<Tree> treeList;
	private TiledMapTileLayer groundLayer2;
	private Object getLayers;
	private TiledMapTileLayer folliageLayer2;
	private TiledMapTileLayer walkableLayer2;

	public Map(SpriteBatch batch, OrthographicCamera camera) {
		tiledMap = new TmxMapLoader().load(Gdx.files.internal("map/map2.tmx").path());
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap,batch);
		tiledMapRenderer.setView(camera);
		
		collisionLayer = (TiledMapTileLayer)tiledMap.getLayers().get("collision");
		groundLayer = (TiledMapTileLayer)tiledMap.getLayers().get("ground");
		groundLayer2 = (TiledMapTileLayer)tiledMap.getLayers().get("ground2");
		folliageLayer = (TiledMapTileLayer)tiledMap.getLayers().get("folliage");
		folliageLayer2 = (TiledMapTileLayer)tiledMap.getLayers().get("folliage2");
		walkableLayer = (TiledMapTileLayer)tiledMap.getLayers().get("walkable");
		walkableLayer2 = (TiledMapTileLayer)tiledMap.getLayers().get("walkable2");
		
		treeLayer = tiledMap.getLayers().get("trees");
		treeList = new LinkedList<Tree>();
		for(MapObject object : treeLayer.getObjects()) {
			int gid = (Integer)object.getProperties().get("gid");
			float x = (Float)object.getProperties().get("x");
			float y = (Float)object.getProperties().get("y");
			Tree tree = new Tree(tiledMap.getTileSets().getTile(gid).getTextureRegion(), (int)x, (int)y);
			treeList.add(tree);
		}
	}
	
//	public void render() {
//		tiledMapRenderer.render();
//	}
	
	public void renderGround(SpriteBatch batch) {
		tiledMapRenderer.renderTileLayer(groundLayer);
		tiledMapRenderer.renderTileLayer(groundLayer2);
		tiledMapRenderer.renderTileLayer(folliageLayer);
		tiledMapRenderer.renderTileLayer(folliageLayer2);
		Iterator<Tree> it = treeList.iterator();
		while(it.hasNext()) {
			Tree tree = it.next();
			tree.render(batch);
			
			if(tree.isDead()) {
				collisionLayer.getCell(tree.getPosition().x/TILE_SIZE, tree.getPosition().y/TILE_SIZE).setTile(null);
				it.remove();
			}
		}
	}
	
	public void renderWalkable() {
		tiledMapRenderer.renderTileLayer(walkableLayer);
		tiledMapRenderer.renderTileLayer(walkableLayer2);
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

	public Tree getTree(int checkX, int checkY) {
		for(Tree tree: treeList) {
			if((tree.getPosition().x+TILE_SIZE/2)/TILE_SIZE == checkX
					&& (tree.getPosition().y+TILE_SIZE/2)/TILE_SIZE == checkY)
				return tree;
		}
		
		return null;
	}

	public void toggleDebug() {
		DEBUG = !DEBUG;
	}

}
