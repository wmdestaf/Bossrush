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
	
	public static <T> void print(T[] a, boolean nl) {
		System.out.print("[");
		for(int i = 0; i < a.length; ++i) {
			System.out.print(a[i].toString() + (i == a.length - 1 ? ']' : ','));
		}
		if(nl) System.out.println();
	}
}
