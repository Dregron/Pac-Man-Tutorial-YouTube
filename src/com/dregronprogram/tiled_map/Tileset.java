package com.dregronprogram.tiled_map;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Tileset {

	private Map<Integer, Property> tileproperties;
	
	public Map<Integer, Property> getTileproperties() {
		return tileproperties;
	}
	
	public void setTileproperties(Map<Integer, Property> tileproperties) {
		this.tileproperties = tileproperties;
	}
}
