package actor;

import physics.Vec2;

public class ActorUtils {
	
	public static boolean[] intersect(Actor a, Actor b, boolean[] eq_mask) {
		  return intersect(a.getPos(), a.getSize(), b.getPos(), b.getSize(), eq_mask);
	}
	
	public static boolean[] intersect(Actor a, Vec2 posB, Vec2 sizeB, boolean[] eq_mask) {
		return intersect(a.getPos(), a.getSize(), posB, sizeB, eq_mask);
	}
	
	public static boolean[] intersect(Vec2 posA, Vec2 sizeA, Actor b, boolean[] eq_mask) {
		return intersect(posA, sizeA, b.getPos(), b.getSize(), eq_mask);
	}
	
	/**
	 * Returns if Actors 'A' and 'B' intersect
	 * @return A boolean mask, representing intersect on 'left', 'top', 'right', 'bottom' of 'a'
	 */
	public static boolean[] intersect(Vec2 posA, Vec2 sizeA, Vec2 posB, Vec2 sizeB, boolean[] eq_mask) {		  
	  
		boolean[] rv = new boolean[4];
		if(eq_mask[0]) {
		rv[0] = ( //left - conservative on y, specific on x w.r.t x = bX + bSizeX
		posA.getY() + sizeA.getY() >= posB.getY() && posA.getY() <= posB.getY() + sizeB.getY() &&
		posA.getX()                <= posB.getX() + sizeB.getX() && 
		posA.getX() + sizeA.getX() >= posB.getX() + sizeB.getX()
		);
		}
		else {
		rv[0] = ( //left - conservative on y, specific on x w.r.t x = bX + bSizeX
		posA.getY() + sizeA.getY() > posB.getY() && posA.getY() < posB.getY() + sizeB.getY() &&
		posA.getX()                < posB.getX() + sizeB.getX() && 
		posA.getX() + sizeA.getX() > posB.getX() + sizeB.getX()
		);
		}
		  
		if(eq_mask[1]) {
		rv[1] = ( //up - conservative on x, specific on y w.r.t y = bY
		posA.getX() + sizeA.getX() >= posB.getX() && posA.getX() <= posB.getX() + sizeB.getX() &&
		posA.getY()                <= posB.getY() && 
		posA.getY() + sizeA.getY() >= posB.getY() 
		);
		}
		else {
		rv[1] = ( //up - conservative on x, specific on y w.r.t y = bY
		posA.getX() + sizeA.getX() > posB.getX() && posA.getX() < posB.getX() + sizeB.getX() &&
		posA.getY()                < posB.getY() && 
		posA.getY() + sizeA.getY() > posB.getY() 
		);
		}
		
		if(eq_mask[2]) {
		rv[2] = ( //right - conservative on y, specific on x w.r.t x = bX
		posA.getY() + sizeA.getY() >= posB.getY() && posA.getY() <= posB.getY() + sizeB.getY() &&
		posA.getX()                <= posB.getX() && 
		posA.getX() + sizeA.getX() >= posB.getX() 
		);	 	
		}
		else {
		rv[2] = ( //right - conservative on y, specific on x w.r.t x = bX
		posA.getY() + sizeA.getY() > posB.getY() && posA.getY() < posB.getY() + sizeB.getY() &&
		posA.getX()                < posB.getX() && 
		posA.getX() + sizeA.getX() > posB.getX() 
		);
		}
			 
		if(eq_mask[3]) {
		rv[3] = ( //down - conservative on x, specific on y w.r.t y = bY + bSizeY
		posA.getX() + sizeA.getX() >= posB.getX() && posA.getX() <= posB.getX() + sizeB.getX() &&
		posA.getY()                <= posB.getY() + sizeB.getY() && 
		posA.getY() + sizeA.getY() >= posB.getY() + sizeB.getY()
		);	
		}
		else {
		rv[3] = ( //down - conservative on x, specific on y w.r.t y = bY + bSizeY
		posA.getX() + sizeA.getX() > posB.getX() && posA.getX() < posB.getX() + sizeB.getX() &&
		posA.getY()                < posB.getY() + sizeB.getY() && 
		posA.getY() + sizeA.getY() > posB.getY() + sizeB.getY()
		);	
		}
		
		return rv;
	}
}
