package actor;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import engine.Engine;
import engine.Engine.AFFILIATION;
import physics.PhysicsController;
import physics.SimplePhysicsController;
import physics.Vec2;
import ui.GUIUtils;
import ui.GUIUtils.SCREEN_BEHAVIOR;

public class PlayerActor extends Actor {

	private static final double DEFAULT_DX = 5.0;
	private static final double DEFAULT_VY0 = 16; //12.0;
	private static final double DEFAULT_G0  = 0.5;
	private static final int DEFAULT_MAX_JUMPS = 2; //triple jump
	
	private boolean[] LR_Keys;
	private long[] LR_Keys_time;
	
	public PlayerActor(Vec2 pos, Vec2 size, AFFILIATION a, String name) { 
		super(pos, size, a, null, name);
		super.setPhysicsController(new SimplePhysicsController(DEFAULT_DX));
		
		LR_Keys = new boolean[]{false, false};
		LR_Keys_time = new long[] {0,0};
	}

	@Override
	public void onKeyPress(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:  LR_Keys[0] = true; 
			                        LR_Keys_time[0] = System.currentTimeMillis();
								    break;
			case KeyEvent.VK_RIGHT: LR_Keys[1] = true; 
								    LR_Keys_time[1] = System.currentTimeMillis();
									break;
			case KeyEvent.VK_SPACE: ((SimplePhysicsController) super.getPhysicsController()).requestJump(
					                	DEFAULT_VY0, DEFAULT_G0, DEFAULT_MAX_JUMPS
									);
									break;
		}
	}

	@Override
	public void onKeyRelease(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:  LR_Keys[0] = false; break;
			case KeyEvent.VK_RIGHT: LR_Keys[1] = false; break;
		}
	}

	@Override
	public void render(Graphics g, Vec2 screenSize, Vec2 camera_off, Vec2 scale) {
		super.render(g, screenSize, camera_off, scale);
	}

	@Override
	public void tick(Vec2 stageDim, ArrayList<Actor> others) {
		SimplePhysicsController pc = (SimplePhysicsController) super.getPhysicsController();
		if(LR_Keys[0]) pc.requestDirection(-1, LR_Keys_time[0]);
		if(LR_Keys[1]) pc.requestDirection(+1, LR_Keys_time[1]);
		pc.tick(this, stageDim, others);
	}
}
