package ui;

import physics.Vec2;

public class GUIUtils {
	public static enum SCREEN_BEHAVIOR { SCROLL_XY, SCROLL_X, SCROLL_Y, NO_SCROLL };
	
	public static int ssY(double y, int height) {
		return height - (int)y;
	}
	
	public static Vec2 ss(Vec2 ws, Vec2 size) {
		return new Vec2((int)ws.getX(), ssY(ws.getY(), size.getYi()));
	}
	
	public static Vec2 cameraOffsetSS(Vec2 screenSize, Vec2 stageSize, Vec2 playerPos, SCREEN_BEHAVIOR sb) {
		if(sb != SCREEN_BEHAVIOR.NO_SCROLL) throw new IllegalArgumentException("sb!=NoScroll");
		Vec2 rv = screenSize.divY(-6.0);
		rv.setX(0);
		return rv;
	}
}
