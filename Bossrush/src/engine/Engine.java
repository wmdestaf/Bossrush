package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import actor.Actor;
import actor.PlayerActor;
import physics.Vec2;
import ui.GUI;
import ui.GUIUtils;
import ui.GUIUtils.SCREEN_BEHAVIOR;
import util.Utils;

public class Engine {

	public static enum AFFILIATION { RED, BLUE, TERRAIN };
	private static final int ZOOM_GRANULARITY = 9;
	
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
		zoom  = -3 * ZOOM_GRANULARITY / 4;
		minimapEnabled = false;
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
		int floor = GUIUtils.ss(Vec2.ZERO, screen_dim, scale, zoom).add(camera_offset).getYi();
		g.drawLine(0, floor, screen_dim.getXi(), floor);
		
		//draw left wall
		int left = GUIUtils.ss(Vec2.ZERO, screen_dim, scale, zoom).add(camera_offset).getXi();
		g.drawLine(left, 0, left, screen_dim.getYi());
		
		//draw right wall
		int right = GUIUtils.ss(new Vec2(stage_dim.getX(), 0), screen_dim, scale, zoom).add(camera_offset).getXi();
		g.drawLine(right - 1, 0, right - 1, screen_dim.getYi());

		//draw top wall (out of frame)
		int top = GUIUtils.ss(new Vec2(0, stage_dim.getY()), screen_dim, scale, zoom).add(camera_offset).getYi();
		g.drawLine(0, top + 1, screen_dim.getXi(), top + 1);
		g.setColor(Color.CYAN);
	}
	
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
        g.fillRect(0, 0, screen_dim.getXi(), screen_dim.getYi());
		
        Vec2 zoom = getInterpolatedZoom();
		Vec2 offs = GUIUtils.cameraOffsetSS(
			screen_dim, stage_dim, actors.get(0).getPos(), actors.get(0).getSize(), scale, zoom, screen_behavior
		);
		
		drawBoundingBox(g, offs, zoom);
		
		for(Actor a : actors) {
			a.render(g, screen_dim, offs, scale, zoom);
		}
		
		//HUD drawing code goes here
		//...
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
		this.zoom = (int) Utils.clamp(zoom - Utils.signum(d), -ZOOM_GRANULARITY,  ZOOM_GRANULARITY);
	}
	
	public void resetZoom() {
		this.zoom = 0;
	}

	/**
	 * The maximum allowed zoom is such that the maximum dimension of the player rectangle is 50% of the screen size
	 * The minimum allowed zoom is such that the minimum dimension of the stage fits inside of the camrea
	 * TODO: lerp
	 */
	public Vec2 getInterpolatedZoom() {
		//Calculate the dimension of the stage with respect to the screen size (fit on minimum dimension)
		//Calculate the dimension of the player with respect to the screen size (fit on 0.85 maximum dimension)
		//ss = worldspace * scale * zoom => zoom = ss / worldspace * scale
		Vec2 min = Vec2.fromScalar(screen_dim.mulY(5./6.).div(stage_dim.mul(scale)).max());
		Vec2 max = Vec2.fromScalar(screen_dim.mulY(5./6. * (0.50)).div(actors.get(0).getSize().mul(scale)).min());
		double lerp = (zoom + ZOOM_GRANULARITY) / (2. * ZOOM_GRANULARITY);
		return min.add(max.mul(lerp));
	}

	public void configureMinimapEnabled(boolean b) {
		this.minimapEnabled = b;
	}

	public void configureActorPosition(int i, Vec2 vec2) {
		actors.get(i).getPos().set(vec2);
	}
}