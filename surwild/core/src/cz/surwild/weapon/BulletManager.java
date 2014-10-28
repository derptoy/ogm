package cz.surwild.weapon;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import cz.surwild.main.Map;
import cz.surwild.util.Point2D;

public class BulletManager {
	
	private LinkedList<Bullet> pool = new LinkedList<Bullet>();
	private LinkedList<Bullet> fired = new LinkedList<Bullet>();
	private Map map;
	private Texture texture;
	
	public BulletManager(Map map) {
		this.map = map;
		
		texture = new Texture(Gdx.files.internal("weapon/bullet.png"));
	}
	
	public synchronized void fireBullet(Point2D direction, Vector2 origin) {
		Bullet bullet;
		if(pool.size() == 0) {
			bullet = new Bullet(texture);
		} else
			bullet = pool.poll();
		
		bullet.setPosition(origin);
		bullet.setDirection(direction);
		
		fired.add(bullet);
	}
	
	public synchronized void update() {
		Iterator<Bullet> iter = fired.iterator();
		Bullet bullet;
		while(iter.hasNext()) {
			bullet = iter.next();
			bullet.update();
			
			if(bullet.getPosition().x < 0
					|| bullet.getPosition().x >= Map.MAP_X_TILECOUNT*Map.TILE_SIZE
					|| bullet.getPosition().y < 0
					|| bullet.getPosition().y >= Map.MAP_Y_TILECOUNT*Map.TILE_SIZE) {
				iter.remove();
				pool.add(bullet);
			} else if(map.collisionTile(bullet.getIndexX(), bullet.getIndexY())) {
				iter.remove();
				pool.add(bullet);
			}
			
		}
	}

	public synchronized void render(SpriteBatch batch) {
		for(Bullet bullet:fired)
			bullet.render(batch);
	}

}
