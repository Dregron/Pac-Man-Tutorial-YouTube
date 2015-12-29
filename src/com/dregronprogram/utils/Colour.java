package com.dregronprogram.utils;

import java.awt.Color;

public class Colour extends Color {

	public final static Color ALPHA = new Color(0, 0, 0, 0);
	
	public Colour(int r, int g, int b, int a) {
		super(r, g, b, a);
	}

	private static final long serialVersionUID = 1L;

}
