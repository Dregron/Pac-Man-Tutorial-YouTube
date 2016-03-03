package com.dregronprogram.menu_state;

import com.dregronprogram.display.Display;
import com.dregronprogram.state.State;
import com.dregronprogram.state.StateMachine;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MenuState extends State implements KeyListener {

    private Font menuFont = new Font("Serif", Font.TRUETYPE_FONT, 102);
    private Font startFont = new Font("Serif", Font.TRUETYPE_FONT, 48);
    private String pacman = "Pac-Man", start = "press enter";

    public MenuState(StateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void init(Canvas canvas) {

    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.YELLOW);
        g.setFont(menuFont);
        g.drawString(pacman, (Display.WIDTH/2)-(g.getFontMetrics().stringWidth(pacman)/2), g.getFontMetrics().getHeight());

        g.setColor(Color.WHITE);
        g.setFont(startFont);
        g.drawString(start, (Display.WIDTH/2)-(g.getFontMetrics().stringWidth(start)/2), g.getFontMetrics().getHeight()+250);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
