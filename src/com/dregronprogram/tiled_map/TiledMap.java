package com.dregronprogram.tiled_map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TiledMap {

	public TiledMap() {
		
		long startTime = System.nanoTime();
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(TiledMap.class.getResource("/com/dregronprogram/tiled_map/Level1_Map.json").getFile()));
			String json = "";
			while(bufferedReader.ready()) {
				json += bufferedReader.readLine();
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.err.println((System.nanoTime()-startTime));
	}
}
