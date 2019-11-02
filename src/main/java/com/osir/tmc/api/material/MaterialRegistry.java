package com.osir.tmc.api.material;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.osir.tmc.api.heat.HeatMaterial;

public class MaterialRegistry {
	private static final Map<String, Map<String, HeatMaterial>> REGISTRY = new HashMap<String, Map<String, HeatMaterial>>();

	public static void addMaterial(String modid, String name, HeatMaterial material) {
		if (!REGISTRY.containsKey(modid)) {
			REGISTRY.put(modid, new HashMap<String, HeatMaterial>());
		}
		if (hasMaterial(name)) {
			throw new IllegalArgumentException("Material [" + name + "] has been registered");
		}
		Map<String, HeatMaterial> reg = REGISTRY.get(modid);
		if (!reg.containsKey(name)) {
			reg.put(name, material);
		}
	}

	public static boolean hasMaterial(String name) {
		return getMaterial(name) != null;
	}

	public static HeatMaterial getMaterial(String name) {
		Iterator<Entry<String, Map<String, HeatMaterial>>> iterator = REGISTRY.entrySet().iterator();
		while (iterator.hasNext()) {
			Map<String, HeatMaterial> reg = iterator.next().getValue();
			if (reg.containsKey(name)) {
				return reg.get(name);
			}
		}
		return null;
	}
}