package cz.fred.main;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import cz.fred.main.physics.FixtureDefFactory;

public class Assets {
	private static AssetManager assets;
	private static Skin skin;
	
	public static void init() {
		assets = new AssetManager();
		
//		skin = new Skin(Gdx.files.internal("ui/default/uiskin.json"), new TextureAtlas(Gdx.files.internal("ui/default/uiskin.atlas")));
		
		assets.load("objects/gem_0000_shadow.png", Texture.class);
		assets.load("objects/gem_0001_gem.png", Texture.class);
		assets.load("objects/gem_0002_gemHero.png", Texture.class);
		
		assets.load("hero/hero.txt", TextureAtlas.class);
		assets.load("enemy/enemy.txt", TextureAtlas.class);
		
		assets.finishLoading();
		
		Array<TextureAtlas> reg = new Array<TextureAtlas>();
		assets.getAll(TextureAtlas.class, reg);
		System.out.println(reg.size);
	}
	
	public static Skin getUISkin() {
		return skin;
	}
	
	public static Texture getTexture(String url) {
		return assets.get(url);
	}
	
	public static TextureAtlas getTextureAtlas(String url) {
		return assets.get(url);
	}

	public static void dispose() {
		assets.dispose();
		FixtureDefFactory.dispose();
	}
}
