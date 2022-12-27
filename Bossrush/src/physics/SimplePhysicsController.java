package physics;

import java.util.ArrayList;
import actor.Actor;
import actor.ActorUtils;

public class SimplePhysicsController extends PhysicsController {
	
	private double speed;
	private int xDirection;
	private long lastXTime;
	
	private JUMP_STATE js;
	private double vy0, g0, yticks;
	private int jumpsTaken;
	
	public SimplePhysicsController(double speed) {
		this.speed = speed;
		this.xDirection = 0;
		this.js = JUMP_STATE.GROUNDED;
	}
	
	public void requestDirection(int dir, long time) {
		if(time < lastXTime) return;
		this.xDirection = dir;
		this.lastXTime = time;
	}
	
	public void requestJump(double vy, double g, int maxJumps) {
		if(this.jumpsTaken < maxJumps) {
			this.vy0 = vy;
			this.g0 = g;
			this.yticks = 0;
			this.js = JUMP_STATE.JUMPING;
			this.jumpsTaken += 1;
			
			if(js == JUMP_STATE.GROUNDED) this.jumpsTaken = 0;
		}
	}
	
	public void tick(Actor a, Vec2 stageDim, ArrayList<Actor> actors) {
		Vec2 in = a.getPos().dup(); //reference;
		Vec2 old = in.dup();
		
		//Calculate movement
		if(js == JUMP_STATE.JUMPING) {
			in = in.addY(-2.0 * g0 * yticks + vy0);
			
			yticks += 1;
		}
		in = in.addX(xDirection * speed);
		in = in.clamp(Vec2.ZERO, stageDim.sub(a.getSize())); //Clamp to stage boundary
		
		//Handle any collisions
		Boolean any[] = {false, false, false, false}; //any collisions
		for(Actor o : actors) {
			if(a == o) continue;
			
			Vec2 op = o.getPos(), os = o.getSize();
			boolean[] collisionMask = ActorUtils.intersect(in, a.getSize(), o);
			boolean anyCollide = false;
			for(int i = 0; i < 4; ++i) { 
				any[i] |= collisionMask[i];
				anyCollide |= collisionMask[i];
			}
			if(!anyCollide) ;// return;

			boolean collideX = collisionMask[0] || collisionMask[2]; //left, right
			boolean collideY = collisionMask[1] || collisionMask[3]; //up, down
			boolean diagonalConditionY = ( //Are we 'attacking' the object from entirely above / below?
				old.getY() + a.getSize().getY() <= op.getY() || old.getY() >= op.getY() + os.getY()
			);
			
			//horizontal collide - or, diagonal at horizontal
			if((collideX && !collideY) || (collideX && collideY && !diagonalConditionY)) {
				if(collisionMask[0]) in.setX(op.getX() + os.getX()); //collision on our left
				else in.setX(op.getX() - a.getSize().getX());        //collision on our right
			}
			//vertical collide - or, diagonal at vertical
			else if((!collideX && collideY) || (collideX && collideY && diagonalConditionY)) {
				if(collisionMask[1]) { //collision on our head
					this.yticks = 1.01 * vy0 / (2.0 * g0);
					in.setY(op.getY() - a.getSize().getY());
				}
				else { //collision on our toes
					js = JUMP_STATE.GROUNDED;
					this.jumpsTaken = 0;
					in.setY(op.getY() + os.getY());
				}
			}
		}
		
		//Utils.print(any, true);
		
		//TODO: refactor this into four boxing terrain objects when collision is fully implemented
		//Update vertical state object-independent (falling when walk off ledge, stage boundary)
		if(in.getY() == 0) {
			in.setY(0);
			js = JUMP_STATE.GROUNDED;
			this.jumpsTaken = 0;
		}
		//falling off objects
		else if((js == JUMP_STATE.GROUNDED && !any[3]) || in.getY() == stageDim.getY()-a.getSize().getY()) { 
			js = JUMP_STATE.JUMPING;
			this.jumpsTaken = 0; //1;
			this.yticks = 1.01 * vy0 / (2.0 * g0);
		}
		
		xDirection = 0;
		lastXTime = 0;
		
		a.getPos().set(in);
	}
	
}
