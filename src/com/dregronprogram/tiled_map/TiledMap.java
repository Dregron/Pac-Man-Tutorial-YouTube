package com.dregronprogram.tiled_map;

import com.fasterxml.jackson.databind.ObjectMapper;


public class TiledMap {

	private Tiled tiledMap;
	
	public TiledMap(String path) {

		long start = System.currentTimeMillis();
		try {
			this.tiledMap = new ObjectMapper().readValue(TiledMap.class.getResource(path), Tiled.class);
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
