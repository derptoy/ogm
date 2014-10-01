package cz.roller.game.person;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

public class PersonManager {
	
	private LinkedList<Person> persons = new LinkedList<Person>();

	public void createPerson(float x, float y, float size, World world) {
		Person person = new Person(x, y, size, world);
		persons.add(person);
	}

	public void draw(SpriteBatch batch) {
		batch.begin();
		for(Person person:persons)
			person.draw(batch);
		batch.end();
	}

	public void dispose() {
		for(Person person:persons)
			person.dispose();
	}

	public void looseLimb() {
		for(Person person:persons)
			person.testLimb();
	}

}
