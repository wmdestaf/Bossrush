package physics;

import java.util.ArrayList;

import actor.Actor;

/**
 * Doesn't respond to physics input.
 * @author i99sh
 */
public class DummyPhysicsController extends PhysicsController {

	@Override
	public void requestDirection(int i, long t) {}

	@Override
	public void tick(Actor parent, Vec2 stageDim, ArrayList<Actor> others) {}
}
