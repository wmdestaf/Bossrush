package actor;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import engine.Engine.AFFILIATION;
import physics.PhysicsController;
import physics.SimplePhysicsController;
import physics.Vec2;
import ui.GUIUtils.SCREEN_BEHAVIOR;

public abstract class Actor {

	private Vec2 pos; //bottom left?
	private Vec2 size; 
	private AFFILIATION affiliation;
	private PhysicsController physicsController;
	private String name;
	
	public abstract void tick(Vec2 stageDim, ArrayList<Actor> others);
	public abstract void onKeyPress(KeyEvent e);
	public abstract void onKeyRelease(KeyEvent e);
	public abstract void render(Graphics g, Vec2 screenSize, Vec2 cam_off);

	public Actor(Vec2 pos, Vec2 size, AFFILIATION a, PhysicsController p, String name) {
		this.pos = pos;
		this.size = size;
		
		this.affiliation = a;
		this.physicsController = p;
		this.setName(name);
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
}
