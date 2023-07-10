package projectspack;

import static projectspack.Util.round2NearestInterval;

import java.text.DecimalFormat;

public class Util {
	public static final DecimalFormat df = new DecimalFormat("0.00000000");
	public static final String[] specialfunctions = { "plusminus", "asin", "acos", "atan", "sin", "cos", "tan", "floor",
			"sum", "abs" };

	public static boolean isNum(char a) {
		if (a == '.' || a == 'x' || a == 'E') { // including Periods
			return true;
		}
		return a >= '0' && a <= '9';
	}

	public static boolean isBasicOperator(char a) {
		return (a == '^' || a == '+' || a == '-' || a == '%' || a == '*' || a == '/');
	}

	public static double pow(double base, double expo) { // Because Math.pow failed me
		double result = 0;
		if (base < 0) {
			result = Math.pow(base, (int) -((-expo) - (-expo) % 1));

			expo = -((-expo) % 1);
			if (expo == 0) {
				return result;
			}

			int gcd = findGCD(expo);
			int b = (int) (1 / ((double) (720720 / gcd) * expo));
			if (b % 2 == 0) {
				return Double.NaN;
			}
			int a = 720720 / gcd;

			return result * Math.pow(-base, expo) * Math.pow(-1, a);

		}
		return Math.pow(base, expo);
	}

	private static int findGCD(double dec) {
		dec = round2NearestInterval(dec, 0.001);
		int scale = (int) ((double) 720720 / dec);// 720720 highly composite
		return findGCD(720720, scale);

	}

	private static int findGCD(int a, int b) {
		while (b != 0) {
			if (a > b) {
				a = a - b;
			} else {
				b = b - a;
			}
		}
		return a;
	}

	public static double round2NearestInterval(double n, double interval) {
		return Double.parseDouble(df.format(n));
	}

	public static boolean hasTwoAnswers(double a) {
		if (a == a)
			return false;
		a = round2NearestInterval(a, 0.0001);
		a = a % 1;
		if (a == 0)
			return false;
		a = 1 / a;
		return getLastDigit(a) % 2 == 0;

	}
	private static int getLastDigit(double a) {
		a =round2NearestInterval(a,0.0001);
		String temp =(a+"");
		int c =temp.length()-1;
		while(c>=0) {
			if(temp.charAt(c) == '0' ||temp.charAt(c) == '.' ||temp.charAt(c) == '-' ) {
				c--;
				continue;
			}else {
				return Integer.parseInt(temp.charAt(c) +""); 
			}
		}
		return 0;

	}
	public static String formatToScript(String stored) throws Exception {
		stored = stored.replaceAll(" ", "");
		while (stored.contains("^")) {
			int index = stored.indexOf("^");
			int replacedBottom = index;
			int replacedTop = index;
			if (stored.charAt(index - 1) == ')') {// If it's (Equation )^123
				int startBracket = index - 2;// Index of (
				while (startBracket > 0 && stored.charAt(startBracket) != '(') {
					startBracket--;
				}
				replacedBottom = startBracket;
			} else {
				int startBracket = index;// index of first number
				while (startBracket > 0 && isNum(stored.charAt(startBracket - 1))) {
					startBracket--;
				}
				replacedBottom = startBracket;

			}
			if (stored.charAt(index + 1) == '(') {// If it's Equation^(123
				int startBracket = index + 2;// Index of (
				while (startBracket < stored.length() && stored.charAt(startBracket) != ')') {
					startBracket++;
				}
				replacedTop = startBracket;
			} else {
				int startBracket = index;// index of first number
				while (startBracket + 1 < stored.length() && isNum(stored.charAt(startBracket + 1))) {
					startBracket++;
				}
				replacedTop = startBracket;

			}

			String temp = "(double)Math.pow(" + stored.substring(replacedBottom, index) + ", "
					+ stored.substring(index + 1, replacedTop + 1) + ")";
			stored = stored.substring(0, replacedBottom) + temp + stored.substring(replacedTop + 1);
		}
		while (stored.contains("!")) {// Factorial
			int index = stored.indexOf("!");

			int replacedBottom = index;
			if (stored.charAt(index - 1) == ')') {// If it's (Equation )^123
				int startBracket = index - 2;// Index of (
				while (startBracket > 0 && stored.charAt(startBracket) != '(') {
					startBracket--;
				}
				replacedBottom = startBracket;
			} else {
				int startBracket = index;// index of first number
				while (startBracket > 0 && isNum(stored.charAt(startBracket - 1))) {
					startBracket--;
				}
				replacedBottom = startBracket;

			}

			String temp = "factorial(" + stored.substring(replacedBottom, index) + ")";
			stored = stored.substring(0, replacedBottom) + temp + stored.substring(index + 1);
		}
		while (stored.contains("|")) {// Factorial
			int index = stored.indexOf("|");

			int top = index;

			// If it's (Equation )^123
			int startBracket = index + 1;// Index of (
			while (startBracket < stored.length() && stored.charAt(startBracket) != '|') {
				startBracket++;
			}
			top = startBracket;

			String temp = "abs(" + stored.substring(index + 1, top) + ")";
			stored = stored.substring(0, index) + temp + stored.substring(top + 1);

		}

		stored = stored.replaceAll("sqrt", "(double)Math.sqrt");
		stored = stored.replaceAll("floor", "(double)Math.floor");
		stored = stored.replaceAll("abs", "(double)Math.abs");

		for (int i = 0; i < stored.length(); i++) {
			if (stored.charAt(i) == '(' || stored.charAt(i) == 'x') {
				if (i != 0) {
					if (isNum(stored.charAt(i - 1)) || stored.charAt(i - 1) == ')') {
						stored = stored.substring(0, i) + "*" + stored.substring(i);
					}

				}
			}
			if (stored.charAt(i) == ')' || stored.charAt(i) == 'x') {
				if (i != stored.length() - 1) {
					if (isNum(stored.charAt(i + 1)) || stored.charAt(i + 1) == '(') {
						stored = stored.substring(0, i + 1) + "*" + stored.substring(i + 1);
					}

				}
			}
		}

		return stored;
	}
}
