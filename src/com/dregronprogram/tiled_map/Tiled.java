package com.dregronprogram.tiled_map;

public class Tiled {

	private int width;
	private int height;
	private int tilewidth;
	private int tileheight;
	
	private Layer[] layers;
	
	private Tileset[] tilesets;
	
	public Tileset[] getTilesets() {
		return tilesets;
	}
	
	public void setTilesets(Tileset[] tilesets) {
		this.tilesets = tilesets;
	}
	
	public Layer[] getLayers() {
		return layers;
	}
	
	public Layer getLayer(String layerName) {
		for (Layer layer : layers) {
			if (layerName.equals(layer.getName())) {
				return layer;
			}
		}
		return null;
		
	}
	
	public void setLayers(Layer[] layers) {
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

	public int getTileheight() {
		return tileheight;
	}
	
	public void setTileheight(int tileheight) {
		this.tileheight = tileheight;
	}
	
	public int getTilewidth() {
		return tilewidth;
	}
	
	public void setTilewidth(int tilewidth) {
		this.tilewidth = tilewidth;
	}
}
