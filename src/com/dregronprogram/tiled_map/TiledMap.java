package com.dregronprogram.tiled_map;

import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class TiledMap {

	private Tiled tiledMap;
	
	public TiledMap(String path) {

		long start = System.currentTimeMillis();
		try {
			JsonReader reader = new JsonReader(new FileReader(TiledMap.class.getResource(path).getPath()));
			this.tiledMap = new Gson().fromJson(reader, Tiled.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis() - start;
		System.err.println("Time took: " + end);
	}
	
	
	
	public Tiled getTiled() {
		return tiledMap;
	}
}
