package cz.roller.game.level;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class LevelsData {

	private static Levels levelData;
	private static Gson gson;
	
	public static void init() {
		gson = new Gson();
		try {
			BufferedReader r = new BufferedReader(new FileReader("conf/level.json"));
//			r.readLine();
			levelData = gson.fromJson(r, Levels.class);
			r.close();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void save() {
		try(BufferedWriter bw = new BufferedWriter(new FileWriter("conf/level.json"))) {
			String str = gson.toJson(levelData);
			bw.write(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<Level> getLevels() {
		return levelData.getLevels();
	}
}
