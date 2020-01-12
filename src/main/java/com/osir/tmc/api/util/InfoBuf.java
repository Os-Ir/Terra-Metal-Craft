package com.osir.tmc.api.util;

import net.minecraft.util.text.TextFormatting;

public class InfoBuf {
	protected String inf, unit;
	protected int accuracy;
	protected double value;
	protected TextFormatting formatValue, formatUnit;

	public InfoBuf(String inf, int value, String unit) {
		this(inf, value, TextFormatting.RESET, unit, TextFormatting.RESET);
	}

	public InfoBuf(String inf, double value, TextFormatting formatValue, String unit, TextFormatting formatUnit) {
		this.inf = inf;
		this.value = value;
		this.unit = unit;
		this.formatValue = formatValue;
		this.formatUnit = formatUnit;
	}

	public InfoBuf setAccuracy(int accuracy) {
		this.accuracy = accuracy;
		return this;
	}

	public String build() {
		return TextFormatting.RESET + this.inf + ": " + this.formatValue
				+ String.format("%." + this.accuracy + "f", this.value) + this.formatUnit + this.unit;
	}
}