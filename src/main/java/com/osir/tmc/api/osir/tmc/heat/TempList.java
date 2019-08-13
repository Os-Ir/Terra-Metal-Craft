package com.osir.tmc.api.osir.tmc.heat;

import net.minecraft.util.text.TextFormatting;

public enum TempList {
	       WARMING(       "warming", TextFormatting.GRAY    ,   20),
	           HOT(           "hot", TextFormatting.GRAY    ,   80),
	      VERY_HOT(       "veryHot", TextFormatting.GRAY    ,  210),
	     FAINT_RED(      "faintRed", TextFormatting.DARK_RED,  480),
	      DARK_RED(       "darkRed", TextFormatting.DARK_RED,  580),
	    BRIGHT_RED(     "brightRed", TextFormatting.RED     ,  730),
	        ORANGE(        "orange", TextFormatting.GOLD    ,  930),
	        YELLOW(        "yellow", TextFormatting.YELLOW  , 1100),
	  YELLOW_WHITE(   "yellowWhite", TextFormatting.YELLOW  , 1300),
	         WHITE(         "white", TextFormatting.WHITE   , 1400),
	BRILLIANTWHITE("brilliantWhite", TextFormatting.WHITE   , 1500);

	private String name;
	private TextFormatting color;
	private int temp;

	private TempList(String name, TextFormatting color, int temp) {
		this.name = name;
		this.color = color;
		this.temp = temp;
	}

	public int getTemp() {
		return this.temp;
	}

	public TextFormatting getColor() {
		return this.color;
	}
}