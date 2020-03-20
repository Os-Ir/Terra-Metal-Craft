package com.osir.tmc.api.recipe;

public enum AnvilWorkType {
	NONE(0), LIGTH_HIT(-3), MEDIUM_HIT(-6), HEAVY_HIT(-9), DRAW(-15), PUNCH(2), BEND(7), UPSET(13), SHRINK(16);

	private int progress;

	AnvilWorkType(int progress) {
		this.progress = progress;
	}

	public int getProgress() {
		return this.progress;
	}
}