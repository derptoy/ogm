package cz.surwild.weapon;

import com.badlogic.gdx.audio.Sound;

import cz.surwild.main.SoundManager;

public class Weapon {
	
	private Sound fireSound;
	private Sound reloadSound;

	public Weapon() {
		fireSound = SoundManager.getSound("shotgun_fire");
		reloadSound = SoundManager.getSound("shotgun_reload");
	}

	public void fire() {
		fireSound.play();
	}

}
