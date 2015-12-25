package com.dregronprogram.application;

import java.awt.Graphics2D;

public interface Renderer {

	public void update(double delta, ApplicationResources applicationResources);
	public void draw(Graphics2D g);
}
