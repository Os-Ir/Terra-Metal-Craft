package com.osir.tmc.api.recipe;

public enum AnvilRecipeType {
	WELD(2), TWINE(1), BEND(1);

	private int inputLength;

	AnvilRecipeType(int inputLength) {
		this.inputLength = inputLength;
	}

	public int getInputLength() {
		return this.inputLength;
	}
}