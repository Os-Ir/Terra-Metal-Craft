package com.osir.tmc.api.recipe;

import gregtech.api.unification.material.type.Material;
import gregtech.api.util.GTControlledRegistry;

public class ModRegistry {
	public static final GTControlledRegistry<String, Material> REGISTRY_ORE_MATERIAL = new GTControlledRegistry<String, Material>(
			1000);
}