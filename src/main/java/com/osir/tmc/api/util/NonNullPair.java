package com.osir.tmc.api.util;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class NonNullPair {
	private NonNullPair() {

	}

	public static <L, R> ImmutablePair<L, R> of(L left, R right) {
		Validate.notNull(left);
		Validate.notNull(right);
		return ImmutablePair.of(left, right);
	}
}