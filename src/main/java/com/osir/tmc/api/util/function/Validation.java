package com.osir.tmc.api.util.function;

@FunctionalInterface
public interface Validation<T, U> {
	boolean test(T t, U u);
}