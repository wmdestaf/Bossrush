package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import actor.Actor;
import physics.Vec2;
import ui.GUIUtils;
import ui.GUIUtils.SCREEN_BEHAVIOR;

public class Engine {

	public static enum AFFILIATION { RED, BLUE, TERRAIN };

	/* List of actors. First element *must* be the player. */
	private ArrayList<Actor> actors;
	private Vec2 screen_dim;
	private SCREEN_BEHAVIOR screen_behavior; //scrolling, etc.
	private Vec2 stage_dim;
	private Vec2 scale;
	
	public Engine() {
		actors = new ArrayList<Actor>();
		scale = Vec2.UNIT.dup();
		this.screen_behavior = SCREEN_BEHAVIOR.NO_SCROLL;
	}
	
	public void tick() {
		for(Actor a : actors) {
			a.tick(stage_dim, actors);
		}
	}
	
	public void drawBoundingBox(Graphics g, Vec2 camera_offset) {
		g.setColor(Color.CYAN);

		//draw floor if we can see it. Floor = (0,0) + camera offset
		int floor = GUIUtils.ss(Vec2.ZERO, screen_dim).add(camera_offset).getYi();
		g.drawLine(0, floor, screen_dim.getXi(), floor);
		
		//draw left wall
		int left = GUIUtils.ss(Vec2.ZERO, screen_dim).add(camera_offset).getXi();
		g.drawLine(left, 0, left, screen_dim.getYi());
		
		//draw right wall
		int right = GUIUtils.ss(new Vec2(stage_dim.getX(), 0), screen_dim).add(camera_offset).getXi();
		g.drawLine(right - 1, 0, right - 1, screen_dim.getYi());

		//draw top wall (out of frame)
		int top = GUIUtils.ss(new Vec2(0, stage_dim.getY()), screen_dim).add(camera_offset).getYi();
		g.drawLine(0, top + 1, screen_dim.getXi(), top + 1);
		g.setColor(Color.CYAN);
	}
	
	public void render(Graphics g) {
		Vec2 offs = GUIUtils.cameraOffsetSS(
			screen_dim, stage_dim, actors.get(0).getPos(), actors.get(0).getSize(), screen_behavior
		);
		
		drawBoundingBox(g, offs);
		
		for(Actor a : actors) {
			a.render(g, screen_dim, offs);
		}
	}
	
	public void addActor(Actor a) { 
		this.actors.add(a);
	}
	
	public void onKeyPress(KeyEvent e) {
		for(Actor a : actors) {
			a.onKeyPress(e);
		}
	}
	
	public void onKeyRelease(KeyEvent e) {
		for(Actor a : actors) {
			a.onKeyRelease(e);
		}
	}

	public void configureScreenSize(int width, int height) {
		this.screen_dim = new Vec2(width, height);
	}
	
	public void configureStageSize(int width, int height) {
		this.stage_dim = new Vec2(width, height);
	}

	public void configureScreenBehavior(SCREEN_BEHAVIOR screen_behavior) {
		this.screen_behavior = screen_behavior;
	}
	
	public void configureScale(Double x, Double y) {
		if(x != null) this.scale.setX(x);
		if(y != null) this.scale.setY(y);
	}
	
	public static Color affiliationColor(AFFILIATION a) {
		switch(a) {	
			case BLUE:    return Color.BLUE;
			case RED:  	  return Color.RED;
			case TERRAIN: return Color.WHITE;
			default:      return Color.PINK;
		}
	}
}