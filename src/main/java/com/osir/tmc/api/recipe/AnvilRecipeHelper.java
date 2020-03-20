package com.osir.tmc.api.recipe;

import java.util.Random;

import gregtech.api.unification.ore.OrePrefix;

public class AnvilRecipeHelper {
	public static int buildWorkInfo(int progress) {
		return buildWorkInfo(progress, 0, 0, 0);
	}

	public static int buildWorkInfo(int progress, int a, int b, int c) {
		return progress << 12 | a << 8 | b << 4 | c;
	}

	public static int progressHash(OrePrefix prefix) {
		Random rand = new Random(prefix.name().hashCode());
		return Math.abs(rand.nextInt() ^ rand.nextInt()) % 110 + 20;
	}

	public static int getProgress(int info) {
		return (info & 0xff000) >> 12;
	}
}