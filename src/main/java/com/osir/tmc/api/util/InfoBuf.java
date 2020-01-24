package com.osir.tmc.api.util;

import net.minecraft.util.text.TextFormatting;

public class InfoBuf {
	protected String inf, unit;
	protected int accuracy;
	protected double value;
	protected TextFormatting formatValue;

	public InfoBuf(String inf, int value, String unit) {
		this(inf, value, TextFormatting.RESET, unit);
	}

	public InfoBuf(String inf, double value, TextFormatting formatValue, String unit) {
		this.inf = inf;
		this.value = value;
		this.unit = unit;
		this.formatValue = formatValue;
	}

	public InfoBuf setAccuracy(int accuracy) {
		this.accuracy = accuracy;
		return this;
	}

	public String build() {
		return TextFormatting.YELLOW + this.inf + ": " + this.formatValue
				+ String.format("%." + this.accuracy + "f", this.value) + TextFormatting.GREEN + this.unit;
	}
}