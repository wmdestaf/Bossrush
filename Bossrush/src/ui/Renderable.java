package ui;

import java.awt.Graphics;

import physics.Vec2;
import ui.GUIUtils.SCREEN_BEHAVIOR;

public interface Renderable {
	public void render(Graphics g, Vec2 screenSize, Vec2 stageDim, Vec2 playerPos, SCREEN_BEHAVIOR sb);
}
