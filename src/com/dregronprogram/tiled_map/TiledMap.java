package com.dregronprogram.tiled_map;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TiledMap {

	private Tiled tiledMap;
	
	public TiledMap(String path) {
		
		try {
			this.tiledMap = new ObjectMapper().readValue(TiledMap.class.getResourceAsStream(path), Tiled.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Tiled getTiled() {
		return tiledMap;
	}
}
