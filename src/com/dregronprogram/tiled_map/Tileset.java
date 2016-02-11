package com.dregronprogram.tiled_map;

import java.util.Map;

public class Tileset {

	private Map<Integer, Map<String, String>> tileproperties;

	public Map<Integer, Map<String, String>> getTileproperties() {
		return tileproperties;
	}
	
	public void setTileproperties(
			Map<Integer, Map<String, String>> tileproperties) {
		this.tileproperties = tileproperties;
	}
}
