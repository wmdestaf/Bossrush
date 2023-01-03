package util;

public class Utils<T> {
	
	/**
	 * Clamps a value to the given range.
	 *
	 * @param value the value to clamp
	 * @param min the minimum value of the range
	 * @param max the maximum value of the range
	 * @return the clamped value
	 */
	public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }
	
	public static int signum(double x) {
		if(x == 0) return 0;
		return x < 0 ? -1 : 1;
	}

	/**
	 * int to bool
	 * @param i int
	 * @return true if i != 0, else false
	 */
	public static boolean _ib(int i) {
		return i != 0;
	}
	
	/**
	 * double to bool
	 * @param d double
	 * @param eps epsilon
	 * @return Math.abs(d) > eps
	 */
	public static boolean _db(double d, double eps) {
		return Math.abs(d) > eps;
	}
	
	/**
	 * bool to int
	 * @param b bool
	 * @return 1 if b == true, else 0
	 */
	public static int _bi(boolean b) {
		return b ? 1 : 0;
	}
	
	public static void print(boolean[] mask, boolean nl) {
		System.out.print("[");
		for(int i = 0; i < mask.length; ++i) {
			System.out.print("" + mask[i]+ (i == mask.length - 1 ? ']' : ','));
		}
		if(nl) System.out.println();
	}

	public static int _sbi(boolean b) {
		return b ? 1 : -1;
	}

	public static boolean any(boolean[] mask) {
		for(int i = 0; i < mask.length; ++i) {
			if(mask[i]) return true;
		}
		return false;
	}
	
	public static boolean[] itobm(int i, int j) {
		char[] ii = String.format("%0" + ("" + j) + "d", i).toCharArray();
		int len = ii.length;
		boolean[] rv = new boolean[len];
		for(int k = 0; k < len; ++k) {
			rv[k] = ii[k] == '1' ? true : false;
		}
		return rv;
	}
	
	public static double fpart(double x) {
		return x - Math.floor(x);
	}
}
