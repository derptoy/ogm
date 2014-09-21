package cz.roller.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.uwsoft.editor.renderer.DefaultAssetManager;
import com.uwsoft.editor.renderer.Overlap2DStage;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.data.SceneVO;


public class TestStage extends Overlap2DStage {
	
	public TestStage() {
		DefaultAssetManager assetManager = new DefaultAssetManager();
		assetManager.loadData();
		for(Texture tex : assetManager.getAtlas().getTextures()) {
			tex.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		}
		sceneLoader = new SceneLoader(assetManager);
		SceneVO vo = sceneLoader.loadScene(Gdx.files.internal("scenes/MainScene.dt"));
		setAmbienceInfo(vo);
		
		CompositeItem sceneActor =  sceneLoader.sceneActor;
		addActor(sceneActor);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}

}
