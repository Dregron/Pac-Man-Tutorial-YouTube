package com.dregronprogram.tiled_map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.dregronprogram.application.ApplicationResources;
import com.dregronprogram.application.Renderer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TiledMap implements Renderer {

	private Tiled tiledMap;
	private Map<Integer, BufferedImage> spriteSheet = new HashMap<Integer, BufferedImage>();
	
	public TiledMap(String path) {
		
		long startTime = System.nanoTime();
		try {
			this.tiledMap = new ObjectMapper().readValue(TiledMap.class.getResourceAsStream(path), Tiled.class);
			URL resource = TiledMap.class.getResource(getTiledMap().getTilesets()[0].getImage());
			BufferedImage bufferedImage = ImageIO.read(resource);
			int yHeight = getTiledMap().getTilesets()[0].getImageHeight()/getTiledMap().getTileHeight();
			int xWidth = getTiledMap().getTilesets()[0].getImageWidth()/getTiledMap().getTileWidth();
			int count = 1;
			for (int y = 0; y < yHeight; y++) {
				for (int x = 0; x < xWidth; x++) {
					spriteSheet.put(count, bufferedImage.getSubimage(x*getTiledMap().getTileWidth(), y*getTiledMap().getTileHeight(), getTiledMap().getTileWidth(), getTiledMap().getTileHeight()));
					count++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.err.println((System.nanoTime()-startTime));
	}
	
	@Override
	public void update(double delta, ApplicationResources applicationResources) {
		
	}

	@Override
	public void draw(Graphics2D g) {
		
		int count = 0;
		for (int y = 0; y < getTiledMap().getHeight(); y++) {
			for (int x = 0; x < getTiledMap().getWidth(); x++) {
				int tile = getTiledMap().getLayers()[0].getData()[count];
				if (tile == 0) {
					g.setColor(Color.WHITE);
					g.fillRect((x*getTiledMap().getTileWidth())+(getTiledMap().getTileWidth()/2), (y*getTiledMap().getTileHeight())+(getTiledMap().getTileHeight()/2), 5, 5);
				} else {
					g.drawImage(spriteSheet.get(tile), x*getTiledMap().getTileWidth(), y*getTiledMap().getTileHeight(), getTiledMap().getTileWidth(), getTiledMap().getTileHeight(), null);
				}
				count++;
			}
		}
	}
	
	public Tiled getTiledMap() {
		return tiledMap;
	}
}
