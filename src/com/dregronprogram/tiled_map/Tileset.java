package com.dregronprogram.tiled_map;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Tileset {

	private String name;
	private String image;
	@JsonProperty("imagewidth")
	private int imageWidth;
	@JsonProperty("imageheight")
	private int imageHeight;
	private int tiledWidth;
	private int tiledHeight;
	private Map<Integer, Property> tileproperties;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String img) {
		this.image = img;
	}
	
	public int getImageWidth() {
		return imageWidth;
	}
	
	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}
	
	public int getImageHeight() {
		return imageHeight;
	}
	
	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}
	
	public int getTiledWidth() {
		return tiledWidth;
	}
	
	public void setTiledWidth(int tiledWidth) {
		this.tiledWidth = tiledWidth;
	}
	
	public int getTiledHeight() {
		return tiledHeight;
	}
	
	public void setTiledHeight(int tiledHeight) {
		this.tiledHeight = tiledHeight;
	}

	public Map<Integer, Property> getTileproperties() {
		return tileproperties;
	}
	
	public void setTileproperties(Map<Integer, Property> tileproperties) {
		this.tileproperties = tileproperties;
	}
}
