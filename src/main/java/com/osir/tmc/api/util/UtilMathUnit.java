package com.osir.tmc.api.util;

public class UtilMathUnit {
	public static final String[] HIGH = { "k", "M", "G", "T", "P", "E", "Z", "Y", "B", "N", "D" };
	public static final String[] LOW = { "m", "μ", "n", "p", "f", "a", "z", "y" };

	public static String format(double num) {
		if (num == 0) {
			return "0";
		} else {
			String str = "";
			if (num < 0) {
				num = -num;
				str += "-";
			}
			int i;
			if (1 <= num && num < 1000) {
				if ((int) (num * 10 % 10) == 0) {
					str += String.format("%.0f", num);
				} else {
					str += String.format("%.1f", num);
				}
			} else if (num >= 1000) {
				for (i = -1; num >= 1000; i++) {
					num /= 1000;
				}
				if ((int) (num * 10 % 10) == 0) {
					str += String.format("%.0f", num);
				} else {
					str += String.format("%.1f", num);
				}
				str += HIGH[i];
			} else {
				for (i = -1; num < 1; i++) {
					num *= 10;
					num *= 10;
					num *= 10;
				}
				if ((int) (num * 10 % 10) == 0) {
					str += String.format("%.0f", num);
				} else {
					str += String.format("%.1f", num);
				}
				str += LOW[i];
			}
			return str;
		}
	}
}