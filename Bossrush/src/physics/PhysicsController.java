package physics;

import java.util.ArrayList;

import actor.Actor;

public abstract class PhysicsController {

	public static enum JUMP_STATE {GROUNDED, JUMPING};
	
	public abstract void requestDirection(int i, long lR_Keys_time);

	public abstract void tick(Actor parent, Vec2 stageDim, ArrayList<Actor> others);	
}
