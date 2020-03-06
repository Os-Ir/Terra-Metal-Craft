package com.osir.tmc.api.util;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

public class NonNullPair {
	private NonNullPair() {

	}

	public static <L, R> Pair<L, R> of(L left, R right) {
		Validate.notNull(left);
		Validate.notNull(right);
		return Pair.of(left, right);
	}
}