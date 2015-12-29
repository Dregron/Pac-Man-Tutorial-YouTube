package com.dregronprogram.tiled_map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Layer {

	private String name;
	private int[] data;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int[] getData() {
		return data;
	}
	
	public void setData(int[] data) {
		this.data = data;
	}
}
