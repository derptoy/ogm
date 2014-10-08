package cz.surwild.main;

import java.awt.Point;
import java.awt.geom.Point2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import cz.surwild.util.CustomRect;
import cz.surwild.util.Direction;

public class HeroController {
	
	private enum State {READY, MOVING}
	private State state = State.READY;
	
	private Sprite sprite;
	private Point direction = new Point();
	private Point target = new Point();
	private Point looking = new Point();
	private Map map;
	private Texture[] textures;
	
	public HeroController(Map map) {
		this.map = map;
		textures = new Texture[4];
		textures[0] = new Texture(Gdx.files.internal("hero/hero_0002_up.png"));
		textures[1] = new Texture(Gdx.files.internal("hero/hero_0003_down.png"));
		textures[2] = new Texture(Gdx.files.internal("hero/hero_0001_left.png"));
		textures[3] = new Texture(Gdx.files.internal("hero/hero_0000_right.png"));
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
				&& !looking.equals(direction)) {
			looking = new Point(direction);
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
					target.setLocation(xpart+direction.x, ypart);
				}
				
			} else if( direction.y != 0) {
				Rectangle rect = sprite.getBoundingRectangle();
				CustomRect spriteRect = new CustomRect("", (int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
				int xpart = (int)(spriteRect.x+spriteRect.width/2)/16;
				int ypart = (int)(spriteRect.y+spriteRect.height/2)/16;
				if( !map.collisionTile(xpart, ypart+direction.y)) {
					state = State.MOVING;
					target.setLocation(xpart, ypart+direction.y);
				}
			}
		}
			
//		direction.nor();
		
//		Rectangle rect = sprite.getBoundingRectangle();
//		CustomRect spriteRect = new CustomRect("", (int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
//		spriteRect.width=14;
//		spriteRect.height=14;
//		spriteRect.x += direction.x+1;
//		spriteRect.y += direction.y+1;
		
//		int xpart = (int)(spriteRect.x+spriteRect.width/2)/16;
//		int ypart = (int)(spriteRect.y+spriteRect.height/2)/16;
//		if( direction.x != 0
//				&& !map.terrainCollision(spriteRect, UtilityFunctions.getSurroundingX(xpart, ypart, direction.x)) ) {
////			sprite.setPosition(spriteRect.x+direction.x, rect.y);
//			sprite.translateX(direction.x);
//			System.out.println("X: "+(spriteRect.x+direction.x));
//		}
//		
//		rect = sprite.getBoundingRectangle();
//		spriteRect = new CustomRect("", (int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
//		
//		if( direction.y != 0
//				&& !map.terrainCollision(spriteRect, UtilityFunctions.getSurroundingY(xpart, ypart, direction.y)) ) {
////			sprite.setPosition(rect.x, spriteRect.y+direction.y);
//			sprite.translateY(direction.y);
//			System.out.println("Y: "+(spriteRect.y+direction.y));
//		}
		
//		System.out.println(sprite.getBoundingRectangle());
	}

}
