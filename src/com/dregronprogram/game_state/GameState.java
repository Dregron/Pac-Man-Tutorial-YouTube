package com.dregronprogram.game_state;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.dregronprogram.display.Display;
import com.dregronprogram.game_state.level.LevelHandler;
import com.dregronprogram.state.State;
import com.dregronprogram.state.StateMachine;
import com.dregronprogram.tiled_map.TiledMap;
import com.dregronprogram.utils.TickTimer;

public class GameState extends State {

	private Font startFont = new Font(Font.SANS_SERIF, Font.ITALIC, 24);
	private String ready = "Ready!";
	private Map<Integer, BufferedImage> spriteSheet = new HashMap<Integer, BufferedImage>();
	private LevelHandler levelHandler;
	private Player player;
	private TickTimer startGameTimer;
	public final static boolean debugMode = false;
	
	public GameState(StateMachine stateMachine) {
		super(stateMachine);

		this.loadSpriteSheet();
		this.player = new Player(Arrays.asList(spriteSheet.get(6), spriteSheet.get(7), spriteSheet.get(8)));
		this.levelHandler = new LevelHandler(spriteSheet, player);
		this.startGameTimer = new TickTimer(300);
		getLevelHandler().getCurrentLevel().beginLevel();	
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
			if (getLevelHandler().getCurrentLevel().isComplete()) {
				System.err.println("Level Won!");
			} else if (getLevelHandler().getCurrentLevel().isGameOver()) {
				System.err.println("Game Over!");
			}
		} else {
			startGameTimer.tick(delta);
			if (startGameTimer.isEventReady()) {
			
				getLevelHandler().getCurrentLevel().startLevel();
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if (!getLevelHandler().levelReady()) {
			g.setColor(Color.YELLOW);
	        g.setFont(startFont);
	        g.drawString(ready, (Display.WIDTH/2)-(g.getFontMetrics().stringWidth(ready)/2), (Display.HEIGHT/2)+55);
		}
		
		getLevelHandler().draw(g);
	}

	@Override
	public void init(Canvas canvas) {
		canvas.addKeyListener(getPlayer());
	}
	
	public LevelHandler getLevelHandler() {
		return levelHandler;
	}
	
	public Player getPlayer() {
		return player;
	}
}
