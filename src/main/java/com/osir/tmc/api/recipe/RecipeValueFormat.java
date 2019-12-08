package com.osir.tmc.api.recipe;

import gregtech.api.util.EnumValidationResult;

public class RecipeValueFormat {
	private String name;
	private Class type;
	private Object def;
	private boolean nullable;

	public RecipeValueFormat(String name, Class type) {
		this.name = name;
		this.type = type;
		this.nullable = true;
	}

	public RecipeValueFormat(String name, Class type, boolean nullable, Object def) {
		this(name, type);
		this.nullable = nullable;
		if ((nullable && def == null) || this.validate(def) == EnumValidationResult.VALID) {
			this.def = def;
		} else {
			throw new IllegalStateException("Default object is invalid");
		}
	}

	public boolean match(String str) {
		return this.name.equals(str);
	}

	public EnumValidationResult validate(Object obj) {
		if (obj == null) {
			return this.nullable ? EnumValidationResult.VALID : EnumValidationResult.SKIP;
		}
		return this.type.isInstance(obj) ? EnumValidationResult.VALID : EnumValidationResult.INVALID;
	}

	public Object getDefault() {
		return this.def;
	}

	public String getName() {
		return this.name;
	}

	public Class getType() {
		return this.type;
	}
}