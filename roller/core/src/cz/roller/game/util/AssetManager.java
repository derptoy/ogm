package cz.roller.game.util;

import com.badlogic.gdx.graphics.Texture;

public class AssetManager {
	
	private static com.badlogic.gdx.assets.AssetManager assets = new com.badlogic.gdx.assets.AssetManager();

	public static void init() {
		assets.load("cart/cart.png", Texture.class);
		assets.load("cart/connector.png", Texture.class);
		assets.load("cart/wheel.png", Texture.class);
		assets.load("person/head.png", Texture.class);
		assets.load("person/body.png", Texture.class);
		assets.load("person/leg.png", Texture.class);
		assets.load("person/legback.png", Texture.class);
		assets.load("person/arm.png", Texture.class);
		assets.load("person/armback.png", Texture.class);
		
		assets.load("track/track.png", Texture.class);
		assets.load("track/flag.png", Texture.class);
		assets.load("track/support.png", Texture.class);
		assets.load("track/supportPole.png", Texture.class);
		assets.load("track/supportBase.png", Texture.class);
		
		assets.load("result/star.png", Texture.class);
		assets.load("result/starEarned.png", Texture.class);
	}
	
	public static Texture getTexture(String string) {
		return assets.get(string, Texture.class);
	}

	public static void waitToLoaded() {
		assets.finishLoading();
	}

}
