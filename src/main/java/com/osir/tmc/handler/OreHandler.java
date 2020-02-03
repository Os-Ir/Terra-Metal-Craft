package com.osir.tmc.handler;

import java.util.function.Predicate;

import com.osir.tmc.api.recipe.ModRegistry;

import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.type.Material;

public class OreHandler {
	public static final Predicate<Material> PREDICATE_ORE = (Material material) -> {
		return ModRegistry.REGISTRY_ORE_MATERIAL.getNameForObject(material) != null;
	};

	public static void register() {
		ModRegistry.REGISTRY_ORE_MATERIAL.register(0, "malachite", Materials.Malachite);
		ModRegistry.REGISTRY_ORE_MATERIAL.register(1, "chalcopyrite", Materials.Chalcopyrite);
		ModRegistry.REGISTRY_ORE_MATERIAL.register(2, "cassiterite", Materials.Cassiterite);
		ModRegistry.REGISTRY_ORE_MATERIAL.register(3, "galena", Materials.Galena);
	}
}