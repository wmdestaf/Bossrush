package physics;

import util.Function;
import util.Utils;

public class ParametricFactory {
	public static Function createOscillation(Vec2 min, Vec2 max, double period, boolean startAtMax) {
		Vec2 min_ = min.dup(), max_ = max.dup();
		int sSAM = Utils._sbi(startAtMax); //signed start at max
		double period_factor = (2 * Math.PI) / period; 
		
		return new Function() {
			@Override
			public Vec2 f(double t) {
				return min_.add(max_.sub(min).mul(0.5)).add(new Vec2(
					0.5 * (max_.getX() - min_.getX()) * sSAM * Math.cos(t * period_factor),
					0.5 * (max_.getY() - min_.getY()) * sSAM * Math.cos(t * period_factor)
				));
			}
		};
	}
	
	public static Vec2 derivative_FD(Function f, double t) {
		return f.f(t + 1e-8).sub(f.f(t - 1e-8)).div(2 * 1e-8);
	}
}
