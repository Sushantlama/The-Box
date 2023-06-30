package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Map {
	int tempSize;
	List<List<String>> mapList;
	int maxWorldCol = 0;
	int maxWorldRow = 0;
	int level = 0;
	
	public Map(int level) {
		this.level = level;
	}
	
	public File getFile() {
		File file;
		if(level == 0) {
			file = new File("src/res/tiles/level1.csv");
			
		}
		else if(level == 1) {
			file = new File("src/res/tiles/level2.csv");
		}
		else {
			file = null;
//			gameFinished();
		}
		return file;
	}
	
	public List<List<String>> GetMap() {
		
		File file = getFile();
		if(file == null) {
			return null;
		}
		
		List<List<String>> list = csvReader(file);
		
		if(mapList!=null) {
			if(!mapList.isEmpty() && !list.isEmpty()) {
//				character.worldy = character.worldy + tempSize * tileSize;
			}
		}
		mapList = new ArrayList<>();
		mapList.addAll(list);
		maxWorldCol = mapList.get(0).size();
		maxWorldRow = mapList.size();
		return mapList;
	}
	
	public List<List<String>> csvReader(File file) {
		List<List<String>> array = new ArrayList<>();
		try {
	         FileReader fr = new FileReader(file);
	         BufferedReader br = new BufferedReader(fr);
	         String line = "";
	         while((line = br.readLine()) != null) {
	            String[] temp = line.split(",");
	            array.add(Arrays.asList(temp));
	         }
	         File file2  = new File("src/res/tiles/anim.csv");
	         FileReader fr2 = new FileReader(file2);
	         BufferedReader br2 = new BufferedReader(fr2);
	         String line2 = "";
	         while((line2 = br2.readLine()) != null) {
	            String[] temp2 = line2.split(",");
	            array.add(0,Arrays.asList(temp2));
	         }
	         tempSize = array.size();
	         br.close();
	    }
		catch(IOException ioe) {
	            ioe.printStackTrace();
	    }
		return array;
	}
}
