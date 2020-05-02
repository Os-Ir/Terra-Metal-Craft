package com.osir.tmc.api.recipe;

import java.util.function.Predicate;

public class RecipeValueFormat {
	private String name;
	private Predicate<Object> predicate;
	private Object def;

	public RecipeValueFormat(String name, Predicate<Object> predicate) {
		this(name, predicate, null);
	}

	public RecipeValueFormat(String name, Predicate<Object> predicate, Object def) {
		this.name = name;
		this.predicate = predicate;
		if (this.validate(def)) {
			this.def = def;
		} else {
			throw new IllegalArgumentException("Default object is invalid");
		}
	}

	public boolean match(String str) {
		return this.name.equals(str);
	}

	public boolean validate(Object obj) {
		return this.predicate.test(obj);
	}

	public Object getDefault() {
		return this.def;
	}

	public String getName() {
		return this.name;
	}
}