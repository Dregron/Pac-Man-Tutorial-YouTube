package com.dregronprogram.tiled_map;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TiledMap {

	private Map tiledMap;
	
	public TiledMap(String path) {
		
		long startTime = System.nanoTime();
		try {
			this.tiledMap = new ObjectMapper().readValue(TiledMap.class.getResourceAsStream(path), Map.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.err.println((System.nanoTime()-startTime));
	}
	
	public Map getTiledMap() {
		return tiledMap;
	}
}
