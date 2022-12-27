package actor;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import engine.Engine;
import engine.Engine.AFFILIATION;
import physics.DummyPhysicsController;
import physics.Vec2;
import ui.GUIUtils;
import ui.GUIUtils.SCREEN_BEHAVIOR;

public class DummyActor extends Actor {

	public DummyActor(Vec2 pos, Vec2 size, AFFILIATION a, String name) {
		super(pos, size, a, new DummyPhysicsController(), name);
	}

	@Override
	public void onKeyPress(KeyEvent e) {
		
	}

	@Override
	public void onKeyRelease(KeyEvent e) {
		
	}

	@Override
	public void render(Graphics g, Vec2 screenSize, Vec2 stageSize, Vec2 playerPos, SCREEN_BEHAVIOR sb) {
		g.setColor(Engine.affiliationColor(getAffiliation()));
		
		Vec2 ssStart = GUIUtils.ss(getPos().addY(getSize()), screenSize);
		ssStart = ssStart.add(GUIUtils.cameraOffsetSS(screenSize, stageSize, playerPos, sb));
		
		//Draw our bounding box
		g.drawRect(ssStart.getXi(), ssStart.getYi(), getSize().getXi(), getSize().getYi());
	}

	@Override
	public void tick(Vec2 stageDim, ArrayList<Actor> others) {

	}
}
