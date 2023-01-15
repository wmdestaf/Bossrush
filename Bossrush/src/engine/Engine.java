package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import actor.Actor;
import actor.PlayerActor;
import physics.Vec2;
import ui.GUIUtils;
import ui.GUIUtils.SCREEN_BEHAVIOR;
import util.Utils;

public class Engine {

	public static enum AFFILIATION { RED, BLUE, TERRAIN };

	/* List of actors. First element *must* be the player. */
	private ArrayList<Actor> actors;
	private Vec2 screen_dim;
	private SCREEN_BEHAVIOR screen_behavior; //scrolling, etc.
	private Vec2 stage_dim;
	private Vec2 scale;
	private int zoom;
	private boolean minimapEnabled;
	
	public Engine() {
		actors = new ArrayList<Actor>();
		scale = Vec2.UNIT.dup();
		zoom  = 0;
		this.screen_behavior = SCREEN_BEHAVIOR.NO_SCROLL;
	}
	
	public void tick() {
		for(Actor a : actors) {
			a.tick(stage_dim, actors);
		}
		((PlayerActor)actors.get(0)).handleAfter(stage_dim);
	}
	
	public void drawBoundingBox(Graphics g, Vec2 camera_offset, Vec2 zoom) {
		g.setColor(Color.CYAN);

		//draw floor if we can see it. Floor = (0,0) + camera offset
		int floor = GUIUtils.ss(Vec2.ZERO, screen_dim, scale.mul(zoom)).add(camera_offset).getYi();
		g.drawLine(0, floor, screen_dim.getXi(), floor);
		
		//draw left wall
		int left = GUIUtils.ss(Vec2.ZERO, screen_dim, scale.mul(zoom)).add(camera_offset).getXi();
		g.drawLine(left, 0, left, screen_dim.getYi());
		
		//draw right wall
		int right = GUIUtils.ss(new Vec2(stage_dim.getX(), 0), screen_dim, scale.mul(zoom)).add(camera_offset).getXi();
		g.drawLine(right - 1, 0, right - 1, screen_dim.getYi());

		//draw top wall (out of frame)
		int top = GUIUtils.ss(new Vec2(0, stage_dim.getY()), screen_dim, scale.mul(zoom)).add(camera_offset).getYi();
		g.drawLine(0, top + 1, screen_dim.getXi(), top + 1);
		g.setColor(Color.CYAN);
	}
	
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
        g.fillRect(0, 0, screen_dim.getXi(), screen_dim.getYi());
		
		Vec2 offs = GUIUtils.cameraOffsetSS(
			screen_dim, stage_dim, actors.get(0).getPos(), actors.get(0).getSize(), scale, getInterpolatedZoom(), screen_behavior
		);
		
		drawBoundingBox(g, offs, getInterpolatedZoom());
		
		for(Actor a : actors) {
			a.render(g, screen_dim, offs, scale, getInterpolatedZoom());
		}
		
		//HUD drawing code goes here
	}
	
	public void addActor(Actor a) { 
		this.actors.add(a);
	}
	
	public void onKeyPress(KeyEvent e) {
		//catch a 'z'
		switch(e.getKeyCode()) {
			case 'z': resetZoom(); return;
		}
		
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
	
	public Vec2 getScreenSize() {
		return this.screen_dim;
	}
	
	public Vec2 getStageSize() {
		return this.stage_dim;
	}
	
	public Vec2 getScale() {
		return this.scale;
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

	public void changeZoom(int d) {
		this.zoom = (int) Utils.clamp(zoom - Utils.signum(d), -9, 9);
	}
	
	public void resetZoom() {
		this.zoom = 0;
	}

	public Vec2 getInterpolatedZoom() {
		Vec2 rv = new Vec2(1.0 + zoom * 0.1, 1.0 + zoom * 0.1);
		System.out.println(zoom + "," + rv);
		return rv;
	}

	public void configureMinimapEnabled(boolean b) {
		this.minimapEnabled = b;
	}

	public void configureActorPosition(int i, Vec2 vec2) {
		actors.get(i).getPos().set(vec2);
	}
}