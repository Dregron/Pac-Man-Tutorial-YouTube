package com.dregronprogram.game_state;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.dregronprogram.game_state.level.LevelHandler;
import com.dregronprogram.state.State;
import com.dregronprogram.state.StateMachine;
import com.dregronprogram.tiled_map.TiledMap;

public class GameState extends State implements KeyListener {

	private Map<Integer, BufferedImage> spriteSheet = new HashMap<Integer, BufferedImage>();
	private LevelHandler levelHandler;
	private Player player;
	private boolean beginLevel;
	public final static boolean debugMode = false;
	
	public GameState(StateMachine stateMachine) {
		super(stateMachine);

		this.loadSpriteSheet();
		this.player = new Player(Arrays.asList(spriteSheet.get(6), spriteSheet.get(7), spriteSheet.get(8)));
		this.levelHandler = new LevelHandler(spriteSheet, player);
	}
	
	private void loadSpriteSheet() {
		try { 
			URL resource = TiledMap.class.getResource("../images/spriteSheet.png");
			setupSpriteSheet(ImageIO.read(resource));
		} catch (IOException e) {e.printStackTrace();}
	}
	
	private void setupSpriteSheet(BufferedImage bufferedImage) {
		int width = 32;
		int height = 30;
		int yHeight = bufferedImage.getHeight()/height;
		int xWidth = bufferedImage.getWidth()/width;
		int counter = 0;
		for (int y = 0; y < yHeight; y++) {
			for (int x = 0; x < xWidth; x++) {
				spriteSheet.put(counter, bufferedImage.getSubimage(x*width, y*height, width, height));
				counter++;
			}
		}
	}

	@Override
	public void update(double delta) {
		
		if (getLevelHandler().levelReady()) {
			
			getLevelHandler().update(delta);
		} else if (isLevelBegining()) {
							
			setBeginLevel(false);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if (!getLevelHandler().levelReady()) return;
		
		getLevelHandler().draw(g);
	}

	@Override
	public void init(Canvas canvas) {
		canvas.addKeyListener(this);
		canvas.addKeyListener(getPlayer());
	}
	
	public LevelHandler getLevelHandler() {
		return levelHandler;
	}
	
	public Player getPlayer() {
		return player;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			getLevelHandler().getCurrentLevel().beginLevel();	
			setBeginLevel(true);
		}
	}
	
	public void setBeginLevel(boolean beginLevel) {
		this.beginLevel = beginLevel;
	}
	
	public boolean isLevelBegining() {
		return beginLevel;
	}
}
