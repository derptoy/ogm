package cz.surwild.main;

import java.awt.Point;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import cz.surwild.harvestable.Tree;
import cz.surwild.util.CustomRect;
import cz.surwild.util.Direction;
import cz.surwild.util.Point2D;
import cz.surwild.weapon.BulletManager;

public class HeroController {
	
	private enum State {READY, MOVING, HARVEST}
	private State state = State.READY;
	private float harvestTimer;
	
	private Sprite sprite;
	private Point2D direction = new Point2D();
	private Point2D target = new Point2D();
	private Point2D looking = new Point2D();
	private Map map;
	private Texture[] textures;
	private Tree harvest;
	private BulletManager bulletManager;
	private long lastFire;
	private Inventory inventory;
	
	public HeroController(Map map, BulletManager bulletManager, Inventory inventory) {
		this.map = map;
		this.bulletManager = bulletManager;
		this.inventory = inventory;
		
		textures = new Texture[5];
		textures[0] = new Texture(Gdx.files.internal("hero/hero_0002_up.png"));
		textures[1] = new Texture(Gdx.files.internal("hero/hero_0003_down.png"));
		textures[2] = new Texture(Gdx.files.internal("hero/hero_0001_left.png"));
		textures[3] = new Texture(Gdx.files.internal("hero/hero_0000_right.png"));
		textures[4] = new Texture(Gdx.files.internal("hero/hero_harvest.png"));
		sprite = new Sprite(textures[1]);
		looking.y = -1;
		sprite.setPosition(41*16, 65*16);
	}
	
	public void render(SpriteBatch batch) {
		sprite.draw(batch);
	}
	
	public void update() {
		if(state == State.MOVING) {
			sprite.translate(looking.x, looking.y);
			
			if(Point.distance(sprite.getX(), sprite.getY(), target.x*16, target.y*16) <= 1) {
				state = State.READY;
				sprite.setPosition(target.x*16, target.y*16);
				direction = Direction.NONE;
			}
		} else if(state == State.HARVEST) {
			harvestTimer += Gdx.graphics.getDeltaTime();
			if(harvestTimer > 5) {
				// harvested
				harvestTimer = 0;
				
				harvest.destroy();
				inventory.addItem(harvest);
				SoundManager.getSound("cutting_tree").stop();
				SoundManager.getSound("falling_tree").play();
				
				state = State.READY;
				System.out.println("State "+state);
				if(looking.y == 1)
					sprite.setTexture(textures[0]);
				else if(looking.y == -1)
					sprite.setTexture(textures[1]);
				else if(looking.x == -1)
					sprite.setTexture(textures[2]);
				else if(looking.x == 1)
					sprite.setTexture(textures[3]);
			}
		}
	}

	public void processKey() {
		if(Gdx.input.isKeyPressed(Keys.W)) {
			direction = Direction.UP;
		} else if(Gdx.input.isKeyPressed(Keys.S)) {
			direction = Direction.DOWN;
		} else if(Gdx.input.isKeyPressed(Keys.A)) {
			direction = Direction.LEFT;
		} else if(Gdx.input.isKeyPressed(Keys.D)) {
			direction = Direction.RIGHT;
		} else {
			direction = Direction.NONE;
		}
		
		if(state == State.READY
				&& Gdx.input.isKeyPressed(Keys.SPACE)
				&& (System.currentTimeMillis()-lastFire) > 1000) {
			bulletManager.fireBullet(looking, new Vector2(sprite.getX()+8, sprite.getY()+8));
			lastFire = System.currentTimeMillis();
		} 
		
		if(state == State.READY
				&& !looking.equals(direction)
				&& (direction.x != 0 || direction.y != 0)) {
			looking = new Point2D(direction);
			if(looking.y == 1)
				sprite.setTexture(textures[0]);
			else if(looking.y == -1)
				sprite.setTexture(textures[1]);
			else if(looking.x == -1)
				sprite.setTexture(textures[2]);
			else if(looking.x == 1)
				sprite.setTexture(textures[3]);
		}
		
		if(state == State.READY) {
			if(direction.x != 0) {
				Rectangle rect = sprite.getBoundingRectangle();
				CustomRect spriteRect = new CustomRect("", (int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
				int xpart = (int)(spriteRect.x+spriteRect.width/2)/16;
				int ypart = (int)(spriteRect.y+spriteRect.height/2)/16;
				if( !map.collisionTile(xpart+direction.x, ypart)) {
					state = State.MOVING;
					target.set(xpart+direction.x, ypart);
				}
				
			} else if( direction.y != 0) {
				Rectangle rect = sprite.getBoundingRectangle();
				CustomRect spriteRect = new CustomRect("", (int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
				int xpart = (int)(spriteRect.x+spriteRect.width/2)/16;
				int ypart = (int)(spriteRect.y+spriteRect.height/2)/16;
				if( !map.collisionTile(xpart, ypart+direction.y)) {
					state = State.MOVING;
					target.set(xpart, ypart+direction.y);
				}
			}
		}
		
		if(state == State.READY 
				&& Gdx.input.isKeyPressed(Keys.F)) {
			Rectangle rect = sprite.getBoundingRectangle();
			int xpart = (int)(rect.x+rect.width/2)/16;
			int ypart = (int)(rect.y+rect.height/2)/16;
			
			int checkX = (int)(xpart + looking.x);
			int checkY = (int)(ypart + looking.y);
			
			Tree tree = map.getTree(checkX, checkY);
			
			if(tree != null) {
				state = State.HARVEST;
				harvest = tree;
				SoundManager.getSound("cutting_tree").play();
				System.out.println("State "+state);
				sprite.setTexture(textures[4]);
			}
		}
		
	}

}
