package ui;

import physics.Vec2;

public class GUIUtils {
	public static enum SCREEN_BEHAVIOR { SCROLL_XY, SCROLL_X, SCROLL_Y, NO_SCROLL };
	
	public static int ssY(double y, int height, Vec2 scale) {
		return height - (int)(y * scale.getY());
	}
	
	public static Vec2 ss(Vec2 ws, Vec2 size, Vec2 scale) {
		return new Vec2((int)(ws.getX() * scale.getX()), ssY(ws.getY(), size.getYi(), scale));
	}
	
	public static double ssY2(double y, double height, Vec2 scale) {
		return height - (y * scale.getY());
	}
	
	public static Vec2 ss2(Vec2 ws, Vec2 size, Vec2 scale) {
		return new Vec2(ws.getX() * scale.getX(), ssY(ws.getY(), size.getYi(), scale));
	}
	
	public static Vec2 cameraOffsetSS(Vec2 screenSize, Vec2 stageSize,  Vec2 playerPos,
									  Vec2 playerSize, Vec2 scale,  SCREEN_BEHAVIOR sb) {
		if(sb == SCREEN_BEHAVIOR.NO_SCROLL) {
			Vec2 rv = screenSize.divY(-6.0);
			rv.setX(0);
			return rv;
		}
		else if(sb == SCREEN_BEHAVIOR.SCROLL_X) { 
			Vec2 playerCenter = playerPos.add(playerSize.div(2));
			Vec2 rv = screenSize.divY(-6.0);
			rv.setX(-playerCenter.getX() * scale.getX()); //Player is now centered on left side of screen

			//Move the player in 'X' to center of screen
			double horizOffset = screenSize.getX() / 2;
			horizOffset = Math.min(horizOffset, playerCenter.getX() * scale.getX()); //left side
			horizOffset += Math.max(0, //right side
					(screenSize.getX() / 2) - scale.getX() * (stageSize.getX() - playerCenter.getX())
			);
			rv = rv.addX(horizOffset);
			

			return rv;
		}
		else if(sb == SCREEN_BEHAVIOR.SCROLL_Y) {
			Vec2 playerCenter = playerPos.add(playerSize.div(2));
			Vec2 rv = screenSize.divY(-6.0);
			rv.setX(0);
			rv = rv.addY(playerCenter.getY() * scale.getY()); //Player is now centered at the bottom of the screen
			
			double vertiOffset = screenSize.getY() / 2;
			vertiOffset = Math.min(vertiOffset, playerCenter.getY() * scale.getY()); //bottom side
			vertiOffset += Math.max(0, 
					(screenSize.getY() / 2 + screenSize.divY(-6.0).getY()) - 
					scale.getY() * (stageSize.getY() - playerCenter.getY())
			);
			rv = rv.subY(vertiOffset);
			
			return rv;
		}
		else if(sb == SCREEN_BEHAVIOR.SCROLL_XY) {
			Vec2 x = GUIUtils.cameraOffsetSS(
				screenSize, stageSize, playerPos, playerSize, scale, SCREEN_BEHAVIOR.SCROLL_X
			);
			Vec2 y = GUIUtils.cameraOffsetSS(
				screenSize, stageSize, playerPos, playerSize, scale, SCREEN_BEHAVIOR.SCROLL_Y
			);
			return new Vec2(x.getX(), y.getY());
		}
		
		throw new UnsupportedOperationException("sb!=NoScroll & sb!=ScrollX & sb!=ScrollY & sb!=ScrollXY");
	}
}
