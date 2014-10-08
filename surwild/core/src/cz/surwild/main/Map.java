package cz.surwild.main;

import java.awt.Point;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import cz.surwild.util.CustomRect;
import cz.surwild.util.UtilityFunctions;

public class Map {
	
	private static final int MAP_X_TILECOUNT = 100;
	private static final int MAP_Y_TILECOUNT = 100;
	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
	private TiledMapTileLayer layer;

	public Map(SpriteBatch batch, OrthographicCamera camera) {
		tiledMap = new TmxMapLoader().load(Gdx.files.internal("map/map.tmx").path());
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap,batch);
		tiledMapRenderer.setView(camera);
		
		layer = (TiledMapTileLayer)tiledMap.getLayers().get("collision");
	}
	
	public void render() {
		tiledMapRenderer.render();
	}

	public void setProjectionMatrix(OrthographicCamera camera) {
		tiledMapRenderer.setView(camera);
	}
	
//	public boolean terrainCollisionX(Rectangle spriteRect, Vector2 direction) {
//		int xpart = (int)(spriteRect.x+spriteRect.width/2)/16;
//		int ypart = (int)(spriteRect.y+spriteRect.height/2)/16;
//		
//		return terrainCollision(spriteRect, UtilityFunctions.getSurrounding(xpart, ypart, direction));
//	}
//	
//	public boolean terrainCollisionY(Rectangle spriteRect, Vector2 direction) {
//		int xpart = (int)(spriteRect.x+spriteRect.width/2)/16;
//		int ypart = (int)(spriteRect.y+spriteRect.height/2)/16;
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
			boolean collisionTile = layer.getCell(point.x, point.y) != null 
										&& layer.getCell(point.x, point.y).getTile() != null
										&& layer.getCell(point.x, point.y).getTile().getProperties().containsKey("collision");
			return collisionTile 
					&& aabb(heroRect, point.x,point.y);
		} else
			return false;
	}


	private boolean aabb(CustomRect heroRect, int xlocal, int ylocal) {
		CustomRect target = new CustomRect("",xlocal*16, ylocal*16, 16, 16);
		return collisionCheck(heroRect, target);
	}

	private boolean collisionCheck(CustomRect rect1, CustomRect rect2) {
		float lx = Math.abs(rect1.x+rect1.width/2.0f - (rect2.x+rect2.width/2.0f));
		float sumx = (rect1.width / 2.0f) + (rect2.width / 2.0f);

		float ly = Math.abs(rect1.y+rect1.height/2.0f - (rect2.y+rect2.height/2.0f));
		float sumy = (rect1.height / 2.0f) + (rect2.height / 2.0f);

		return (lx <= sumx && ly <= sumy);
	}

	public boolean collisionTile(int x, int y) {
		boolean collisionTile = layer.getCell(x, y) != null 
				&& layer.getCell(x, y).getTile() != null
				&& layer.getCell(x, y).getTile().getProperties().containsKey("collision");
		return collisionTile;
	}

}
