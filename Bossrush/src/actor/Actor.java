package actor;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import engine.Engine;
import engine.Engine.AFFILIATION;
import physics.PhysicsController;
import physics.SimplePhysicsController;
import physics.Vec2;
import ui.GUIUtils;
import ui.GUIUtils.SCREEN_BEHAVIOR;

public abstract class Actor {

	private Vec2 pos; //bottom left?
	private Vec2 size; 
	private AFFILIATION affiliation;
	private PhysicsController physicsController;
	private String name;
	private boolean platformSticky;
	
	private Vec2 lastSS; 
	private static final int FILTER_LENGTH = 16;
	
	public abstract void tick(Vec2 stageDim, ArrayList<Actor> others);
	public abstract void onKeyPress(KeyEvent e);
	public abstract void onKeyRelease(KeyEvent e);
	
	//Draws a simple bounding box
	public void render(Graphics g, Vec2 screenSize, Vec2 camera_offset, Vec2 scale, Vec2 zoom) {
		g.setColor(Engine.affiliationColor(getAffiliation()));
		
		Vec2 ssStart = GUIUtils.ss2(getPos().addY(getSize()), screenSize, scale, zoom);
		ssStart = ssStart.add(camera_offset);
		Vec2 ssSize = getSize().mul(scale).mul(zoom);

		// Calculate the top-left corner position of the rectangle
		double x = ssStart.getX();
		double y = ssStart.getY();
		double w = ssSize.getX();
		double h = ssSize.getY();
		
		int xx = (int)Math.round(x);
		int yy = (int)Math.round(y);
		Vec2 corrected = Vec2.ZERO.dup();
		
		//compare differential on X and Y - low pass filter
		corrected.setX(Math.abs(xx-lastSS.getX()) < 2 ? lastSS.getX() : xx);
		corrected.setY(Math.abs(yy-lastSS.getY()) < 2 ? lastSS.getY() : yy);
		
		xx = corrected.getXi();
		yy = corrected.getYi();
		
		//Account for zoom
		
		int ww = (int)w;
		int hh = (int)h;
		/*
		int ww = (int)(w * zoom.getX());
		int hh = (int)(h * zoom.getY());
		*/
		
		Graphics2D g2d = (Graphics2D) g;
	    Stroke originalStroke = g2d.getStroke();
	    g2d.setStroke(new BasicStroke(1)); //3

		g.drawRect(xx, yy, ww, hh);
		
		g2d.setStroke(originalStroke);
		/*
		//System.out.printf("%g %g %g %g\n", x,y,w,h);
		System.out.printf("%d %d %d %d %s\n", xx,yy,ww,hh, screenSize);
		
		//Draw text
		char[] pos = getPos().toString().toCharArray();
		//g.drawChars(pos, 0, pos.length, x, (int)(y + ssSize.getYi() * 0.5));
		*/
		
		this.lastSS = corrected;
	}

	public Actor(Vec2 pos, Vec2 size, AFFILIATION a, PhysicsController p, String name) {
		this.pos = pos;
		this.size = size;
		
		this.affiliation = a;
		this.physicsController = p;
		this.setName(name);
		
		this.platformSticky = false;
		this.lastSS = Vec2.ZERO.dup();
	}
	
	//Getters / Setters
	public AFFILIATION getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(AFFILIATION affiliation) {
		this.affiliation = affiliation;
	}
	
	public Vec2 getPos() {
		return this.pos;
	}
	public PhysicsController getPhysicsController() {
		return physicsController;
	}
	public void setPhysicsController(PhysicsController pc) {
		this.physicsController = pc;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Vec2 getSize() {
		return size;
	}
	public void setSize(Vec2 size) {
		this.size = size;
	}
	
	public boolean isPlatformSticky() {
		return platformSticky;
	}
	public void setPlatformSticky(boolean platformSticky) {
		this.platformSticky = platformSticky;
	}
}
