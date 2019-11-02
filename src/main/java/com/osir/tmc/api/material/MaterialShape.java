package com.osir.tmc.api.material;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

public class MaterialShape {
	public static final Map<String, MaterialShape> REGISTRY = new HashMap<String, MaterialShape>();
	private ResourceLocation texture;
	private int unit;

	public MaterialShape(ResourceLocation texture, int unit) {
		this.texture = texture;
		this.unit = unit;
	}

	public static MaterialShape get(String name) {
		if (REGISTRY.containsKey(name)) {
			return REGISTRY.get(name);
		}
		return null;
	}

	public void register(String name) {
		REGISTRY.put(name, this);
	}

	public ResourceLocation getTexture() {
		return this.texture;
	}

	public int getUnit() {
		return this.unit;
	}
}