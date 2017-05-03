package gClasses.gInterfaces.numberField;

public class DoubleField extends NumberField<Double> {

	public DoubleField() {
		this.setMaxValue(Double.POSITIVE_INFINITY);
		this.setMinValue(Double.NEGATIVE_INFINITY);
	}
	
	@Override
	public String formatString(String str) {
		boolean isNegative = str.length() != 0 && str.charAt(0) == '-';
		boolean negativeAllowed = this.getMinValue() < 0d;
		if (isNegative && !negativeAllowed) {
			str = str.substring(1, str.length());
		}
		return formatDoubleString(str);
	}

	public static String formatDoubleString(String str) {
		int firstDot = str.indexOf('.');
		int lastDot = str.lastIndexOf('.');
		while (lastDot != firstDot) {

			str = str.substring(0, lastDot) + str.substring(lastDot + 1, str.length());

			firstDot = str.indexOf('.');
			lastDot = str.lastIndexOf('.');
		}

		boolean isNegative = str.length() != 0 && str.charAt(0) == '-';
		int i = isNegative ? 1 : 0;
		while (i < str.length()) {
			char c = str.charAt(i);
			boolean charValid = false;
			charValid |= c == '.';
			for (int j = 0; j <= 9; j++) {
				charValid |= c == Integer.toString(j).charAt(0);
			}
			if (!charValid) {
				str = str.substring(0, i) + str.substring(i + 1, str.length());
			} else {
				i++;
			}
		}

		return str;
	}

	@Override
	protected Double parseT(String str) {
		return Double.parseDouble(str);
	}

	@Override
	protected boolean isToHight(Double n) {
		return n > this.getMaxValue();
	}

	@Override
	protected boolean isToLow(Double n) {
		return n < this.getMinValue();
	}

}
