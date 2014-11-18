package cz.fred.main.hero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import cz.fred.main.graphics.Drawable;
import cz.fred.main.map.Map;
import cz.fred.main.physics.FixtureDefFactory;
import cz.fred.main.physics.SpriteBody;
import cz.fred.main.util.Direction;
import cz.fred.main.util.Point2D;

public class Hero implements InputProcessor, Drawable {
	
	private HeroGraphics graphics;
	private HeroData data;
	private Map map;
	
	private SpriteBody body;
	private World world;
	
	public Hero(World world, Map map) {
		this.map = map;
		this.world = world;
		
		data = new HeroData();
		graphics = new HeroGraphics(data);
		
		body = new SpriteBody(world, FixtureDefFactory.getHeroDef());
		data.setBody(body);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		graphics.draw(batch);
	}

	public void setPosition(int x, int y) {
		body.setPosition(x+graphics.getWidth()/2f, y+graphics.getHeight()/2f - 20);
	}

	public void update() {
		graphics.update();
		
		map.attractGems(this);
		
		processKey();
	}

	private void processKey() {
		Vector2 move = Direction.NONE.cpy();
		if(Gdx.input.isKeyPressed(Keys.W)) {
			move.add(Direction.UP);
		} else if(Gdx.input.isKeyPressed(Keys.S)) {
			move.add(Direction.DOWN);
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			move.add(Direction.LEFT);
		} else if(Gdx.input.isKeyPressed(Keys.D)) {
			move.add(Direction.RIGHT);
		}
		move.nor();
		
		body.applyForce(move);
		
		data.setMove(move);
		
		if( !move.equals(Direction.NONE))
			data.setDirection(move);
	}
	
	public float getX() {
		return body.getPosition().x * Map.TO_PIXEL;
	}

	public float getY() {
		return body.getPosition().y * Map.TO_PIXEL;
	}

	@Override
	public boolean keyDown(int keycode) {
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int compareTo(Drawable o) {
		float diff = getComparingY() - o.getComparingY();
		if(diff > 0)
			return 1;
		else if( diff < 0)
			return -1;
		else return 0;
	}

	@Override
	public float getComparingY() {
		return body.getPosition().y;
	}
}
