package actor;

import physics.Vec2;

public class ActorUtils {
	
	public static boolean[] intersect(Actor a, Actor b) {
		  return intersect(a.getPos(), a.getSize(), b.getPos(), b.getSize());
	}
	
	public static boolean[] intersect(Actor a, Vec2 posB, Vec2 sizeB) {
		return intersect(a.getPos(), a.getSize(), posB, sizeB);
	}
	
	public static boolean[] intersect(Vec2 posA, Vec2 sizeA, Actor b) {
		return intersect(posA, sizeA, b.getPos(), b.getSize());
	}
	
	/**
	 * Returns if Actors 'A' and 'B' intersect
	 * @return A boolean mask, representing intersect on 'left', 'top', 'right', 'bottom' of 'a'
	 */
	public static boolean[] intersect(Vec2 posA, Vec2 sizeA, Vec2 posB, Vec2 sizeB) {		  
		  boolean[] rv = new boolean[4];
		  rv[0] = ( //left - conservative on y, specific on x w.r.t x  bX + bSizeX
				  posA.getY() + sizeA.getY() > posB.getY() && posA.getY() < posB.getY() + sizeB.getY() &&
				  posA.getX()                < posB.getX() + sizeB.getX() && 
				  posA.getX() + sizeA.getX() > posB.getX() + sizeB.getX()
		  );
		  rv[1] = ( //up - conservative on x, specific on y w.r.t y  bY
				  posA.getX() + sizeA.getX() > posB.getX() && posA.getX() < posB.getX() + sizeB.getX() &&
				  posA.getY()                < posB.getY() && 
				  posA.getY() + sizeA.getY() > posB.getY() 
		  );
		  rv[2] = ( //right - conservative on y, specific on x w.r.t x  bX
				  posA.getY() + sizeA.getY() > posB.getY() && posA.getY() < posB.getY() + sizeB.getY() &&
				  posA.getX()                < posB.getX() && 
				  posA.getX() + sizeA.getX() > posB.getX() 
		  );	  
		  rv[3] = ( //down - conservative on x, specific on y w.r.t y  bY + bSizeY
				  posA.getX() + sizeA.getX() > posB.getX() && posA.getX() < posB.getX() + sizeB.getX() &&
				  posA.getY()                < posB.getY() + sizeB.getY() && 
				  posA.getY() + sizeA.getY() > posB.getY() + sizeB.getY()
		  );
		  return rv;
	}
}
