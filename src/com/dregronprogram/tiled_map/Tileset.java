package com.dregronprogram.tiled_map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Tileset {

	private String name;
	private String img;
	private int imageWidth;
	private int imageHeight;
	private int tiledWidth;
	private int tiledHeight;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getImg() {
		return img;
	}
	
	public void setImg(String img) {
		this.img = img;
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
}
