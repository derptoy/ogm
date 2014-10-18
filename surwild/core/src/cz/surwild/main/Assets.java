package cz.surwild.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	private static AssetManager assets;
	private static Skin skin;
	
	public static void init() {
		assets = new AssetManager();
		
		skin = new Skin(Gdx.files.internal("ui/default/uiskin.json"), new TextureAtlas(Gdx.files.internal("ui/default/uiskin.atlas")));
	}
	
	public static Skin getUISkin() {
		return skin;
	}
}
