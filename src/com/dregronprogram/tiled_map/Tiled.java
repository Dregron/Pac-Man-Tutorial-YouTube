package com.dregronprogram.tiled_map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Tiled {

	private int width;
	private int height;
	@JsonProperty("tilewidth")
	private int tileWidth;
	@JsonProperty("tileheight")
	private int tileHeight;
	
	private Layers[] layers;
	
	private Tileset[] tilesets;
	
	public Tileset[] getTilesets() {
		return tilesets;
	}
	
	public void setTilesets(Tileset[] tilesets) {
		this.tilesets = tilesets;
	}
	
	public Layers[] getLayers() {
		return layers;
	}
	
	public void setLayers(Layers[] layers) {
		this.layers = layers;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getTileWidth() {
		return tileWidth;
	}
	
	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}
	
	public int getTileHeight() {
		return tileHeight;
	}
	
	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}
}
