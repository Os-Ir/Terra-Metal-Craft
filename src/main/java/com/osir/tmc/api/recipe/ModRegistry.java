package com.osir.tmc.api.recipe;

import com.osir.tmc.api.heat.HeatMaterial;

import gregtech.api.unification.material.type.Material;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.util.GTControlledRegistry;

public class ModRegistry {
	public static final GTControlledRegistry<String, Material> REGISTRY_ORE_MATERIAL = new GTControlledRegistry<String, Material>(
			1000);
	public static final GTControlledRegistry<Material, HeatMaterial> REGISTRY_HEATABLE_MATERIAL = new GTControlledRegistry<Material, HeatMaterial>(
			1000);
	public static final GTControlledRegistry<String, OrePrefix> REGISTRY_HEATABLE_PREFIX = new GTControlledRegistry<String, OrePrefix>(
			1000);
}