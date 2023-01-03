package actor;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import engine.Engine.AFFILIATION;
import physics.DummyPhysicsController;
import physics.ParametricFactory;
import physics.Vec2;
import util.Function;

public class MovingPlatformActor extends Actor {

	private double tick;
	private Function platformFn;
	
	public MovingPlatformActor(Vec2 pos, Vec2 size, AFFILIATION a, String name, Function platformFn) {
		super(pos, size, a, new DummyPhysicsController(), name);
		this.tick = 0;
		this.platformFn = platformFn;
		super.setPlatformSticky(true);
	}

	@Override
	public void onKeyPress(KeyEvent e) {
		
	}

	@Override
	public void onKeyRelease(KeyEvent e) {
		
	}

	@Override
	public void render(Graphics g, Vec2 screenSize, Vec2 camera_offset, Vec2 scale) {
		super.render(g, screenSize, camera_offset, scale);
	}

	@Override
	public void tick(Vec2 stageDim, ArrayList<Actor> others) {
		Vec2 old = getPos().dup();
		getPos().set(platformFn.f(tick));
		//System.out.println("expected=" + getPos().sub(old));
		tick++;
	}
	
	public Vec2 getLastChange(int n) {
		Vec2 rv = platformFn.f(tick - n).sub(platformFn.f(tick - n - 1));
		//System.out.println("actual=" + rv);
		return rv;
	}
	
	public Function getFunction() {
		return platformFn;
	}
	
	public double getTick() {
		return tick;
	}
}
