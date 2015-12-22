package com.dregronprogram.tiled_map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TiledMap {

	public TiledMap() {
		
		long startTime = System.nanoTime();
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(TiledMap.class.getResource("/com/dregronprogram/tiled_map/Level1_Map.json").getFile()));
			//			byte[] data = Files.readAllBytes(path);
//			String json = new String(data, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.err.println((System.nanoTime()-startTime));
	}
}
