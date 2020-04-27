package utils.math;
public enum Fractions {
		highest(0.9), threeQuarters(3.0 / 4.0), twoThirds(2.0 / 3.0), oneHalf(1.0 / 2.0), oneThird(1.0 / 3.0),
		oneQuarter(1.0 / 4.0), zero(0);

		public final float val;

		private Fractions(double fraction) {
			val = (float) fraction;
		}
	}