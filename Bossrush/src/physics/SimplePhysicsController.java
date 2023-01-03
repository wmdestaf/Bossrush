package physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import actor.Actor;
import actor.ActorUtils;
import actor.MovingPlatformActor;
import util.Utils;

public class SimplePhysicsController extends PhysicsController {
	
	private double speed;
	private int xDirection;
	private long lastXTime;
	
	private JUMP_STATE js;
	private double vy0, g0, yticks;
	private int jumpsTaken;
	
	//collision data for moving platforms
	private ArrayList<Actor> previousSticky;
	private HashSet<Actor> excludeCollide;
	private Actor[] lastMPCollide; //Collide detection for last 2 frames on moving platforms
	
	public SimplePhysicsController(double speed) {
		this.speed = speed;
		this.xDirection = 0;
		this.js = JUMP_STATE.GROUNDED;
		
		this.previousSticky = new ArrayList<Actor>();
		this.excludeCollide = new HashSet<Actor>();
		this.lastMPCollide = new Actor[2]; //2 element queue
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
	
	//If we were 'stuck' to a moving platform, match its velocity
	public void firePreviousStickyCorrectionPush(Actor a, Vec2 stageDim) {
		Vec2 in = a.getPos().dup(); //reference;
		for(Actor o : previousSticky) {
			MovingPlatformActor oo = (MovingPlatformActor)o;
			Vec2 d = oo.getLastChange(1);
			in = in.add(d);
		}
		a.getPos().set(in.clamp(Vec2.ZERO, stageDim.sub(a.getSize())));
	}

	public void tick(Actor a, Vec2 stageDim, ArrayList<Actor> actors) {
		Vec2 in = a.getPos().dup(); //reference;
		Vec2 old = in.dup();
		
		//Step our actor's movement
		if(js == JUMP_STATE.JUMPING) {
			in = in.addY(Math.min(20,-2.0 * g0 * yticks + vy0)); //Terrain dim = min of 25, no clipthru
			yticks += 1;
		}
		in = in.addX(xDirection * speed);
		in = in.clamp(Vec2.ZERO, stageDim.sub(a.getSize())); //Clamp to stage boundary
		
		//Consider the case of moving platforms - ticked after us
		previousSticky.clear();
		excludeCollide.clear();
		for(Actor o : actors) {
			if(o == a || !o.isPlatformSticky()) continue;
			MovingPlatformActor oo = (MovingPlatformActor)o;
			
			//Calculate where the platform will be, and where we are now (in the 'pushing' case)
			Vec2 newPlatformPos = oo.getFunction().f(oo.getTick());
			boolean[] mask = ActorUtils.intersect(
				in, a.getSize(), newPlatformPos, oo.getSize(), Utils.itobm(1111, 4)
			);
			
			if(Utils.any(mask)) {
				lastMPCollide[1] = o;
				previousSticky.add(o); //delay handling
				continue;
			}
			
			//Calculate the first pulling case - If we were previously on the platform, and now are not
			boolean[] mask2 = ActorUtils.intersect(old, a.getSize(), o, Utils.itobm(0001, 4));
			if(mask2[3] && js == JUMP_STATE.GROUNDED) {
				in.setY(newPlatformPos.getY() + o.getSize().getY()); //handle immediately
				lastMPCollide[1] = o;
				excludeCollide.add(o);
				continue;
			}
			
			//Calculate the second pulling case - If we were not previously on the platform, and now are
			//We only get here happens because of numerical error...but still, need to handle
			boolean[] mask3 = ActorUtils.intersect(in, a.getSize(), o, Utils.itobm(0001, 4));
			if(mask3[3]) {
				lastMPCollide[1] = o;
				previousSticky.add(o);
			}
		}

		//Handle any static collisions
		Boolean any[] = {false, false, false, false}; //any collisions
		for(Actor o : actors) {
			if(a == o || excludeCollide.contains(o)) continue;
			
			Vec2 op = o.getPos(), os = o.getSize();
			boolean[] collisionMask = ActorUtils.intersect(in, a.getSize(), o, Utils.itobm(0000, 4));
			for(int i = 0; i < 4; ++i) { 
				any[i] |= collisionMask[i];
			}

			boolean collideX = collisionMask[0] || collisionMask[2]; //left, right
			boolean collideY = collisionMask[1] || collisionMask[3]; //up, down
			double eps = Math.min(5,os.getY()*0.05);
			boolean diagonalConditionY = ( //Are we 'attacking' the object from entirely above / below?
				old.getY() + a.getSize().getY() <= op.getY() + eps || 
				old.getY() + eps >= op.getY() + os.getY()
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

		//force stage boundaries
		if(in.getY() == 0) {
			in.setY(0);
			js = JUMP_STATE.GROUNDED;
			this.jumpsTaken = 0;
		}
		//falling off objects
		else if(
				 (js == JUMP_STATE.GROUNDED && !any[3] && excludeCollide.size() == 0) || 
				 (in.getY() == stageDim.getY()-a.getSize().getY())
		) { 
			js = JUMP_STATE.JUMPING;
			this.jumpsTaken = 0; //1;
			
			if(lastMPCollide[0] != null && lastMPCollide[1] == null) { //falling off moving platform
				//Match platform's vertical speed
				double platform_dy = ((MovingPlatformActor)lastMPCollide[0]).getLastChange(0).getY();
				this.yticks = (platform_dy - vy0) / (-2.0 * g0);
			}
			else {
				this.yticks = 1.01 * vy0 / (2.0 * g0); //todo - solve analytically
			}
		}
		
		//reset
		xDirection = 0;
		lastXTime = 0;
		
		//Step the 'queue'
		lastMPCollide[0] = lastMPCollide[1];
		lastMPCollide[1] = null;
		
		//configure actor position
		a.getPos().set(in);
	}
}
