package cz.surwild.main;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	
	private static HashMap<String,Sound> sounds = new HashMap<String, Sound>();
	
	public static void init() {
		sounds.put("cutting_tree", Gdx.audio.newSound(Gdx.files.internal("sound/tree/cutting_tree_limb.mp3")));
		sounds.put("falling_tree", Gdx.audio.newSound(Gdx.files.internal("sound/tree/tree_fall_down.mp3")));
	}
	
	public static Sound getSound(String key) {
		return sounds.get(key);
	}
	
}
