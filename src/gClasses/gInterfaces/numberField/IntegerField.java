package gClasses.gInterfaces.numberField;

public class IntegerField extends NumberField<Integer> {

	public IntegerField() {
		this.setMaxDigit(9);
		this.setMaxValue(Integer.MAX_VALUE);
		this.setMinValue(Integer.MIN_VALUE);
	}
	
	@Override
	public void setMaxDigit(int maxDigit) {
		super.setMaxDigit(Math.min(9, maxDigit));
	}
	
	@Override
	public String formatString(String str) {
		boolean isNegative = str.length() != 0 && str.charAt(0) == '-';
		boolean negativeAllowed = this.getMinValue() < 0;
		if (isNegative && !negativeAllowed) {
			str = str.substring(1, str.length());
		}
		return formatIntegerString(str);
	}

	public static String formatIntegerString(String str) {
		boolean isNegative = str.length() != 0 && str.charAt(0) == '-';
		int i = isNegative ? 1 : 0;
		while (i < str.length()) {
			char c = str.charAt(i);
			boolean charValid = false;
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
	protected Integer parseT(String str) {
		return Integer.parseInt(str);
	}

	@Override
	protected boolean isToHight(Integer n) {
		return n > this.getMaxValue();
	}

	@Override
	protected boolean isToLow(Integer n) {
		return n < this.getMinValue();
	}

}
