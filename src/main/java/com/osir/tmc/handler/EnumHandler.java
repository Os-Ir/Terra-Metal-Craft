package com.osir.tmc.handler;

import java.util.function.Predicate;

import gregtech.api.GTValues;
import gregtech.api.unification.material.MaterialIconType;
import gregtech.api.unification.material.type.DustMaterial;
import gregtech.api.unification.material.type.IngotMaterial;
import gregtech.api.unification.material.type.Material;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraftforge.common.util.EnumHelper;

public class EnumHandler {
	public static void register() {
		EnumHelper.addEnum(MaterialIconType.class, "oreCobble", new Class[] {});

		EnumHelper.addEnum(OrePrefix.class, "ingotDouble",
				new Class[] { String.class, long.class, Material.class, MaterialIconType.class, long.class,
						Predicate.class },
				"Double Ingot", GTValues.M * 2, null, MaterialIconType.ingotDouble, OrePrefix.Flags.ENABLE_UNIFICATION,
				predicate((mat) -> mat instanceof IngotMaterial && mat.hasFlag(DustMaterial.MatFlags.GENERATE_PLATE)));
		EnumHelper.addEnum(OrePrefix.class, "plateDouble",
				new Class[] { String.class, long.class, Material.class, MaterialIconType.class, long.class,
						Predicate.class },
				"Double Plate", GTValues.M * 2, null, MaterialIconType.plateDouble, OrePrefix.Flags.ENABLE_UNIFICATION,
				predicate((mat) -> mat instanceof IngotMaterial && mat.hasFlag(DustMaterial.MatFlags.GENERATE_PLATE)));

		EnumHelper.addEnum(OrePrefix.class, "oreCobble",
				new Class[] { String.class, long.class, Material.class, MaterialIconType.class, long.class,
						Predicate.class },
				"Ore Cobble", -1, null, MaterialIconType.valueOf("oreCobble"),
				OrePrefix.Flags.ENABLE_UNIFICATION | OrePrefix.Flags.DISALLOW_RECYCLING,
				OrePrefixRecipeHandler.PREDICATE_ORE);
	}

	private static Predicate<Material> predicate(Predicate<Material> pre) {
		return (mat) -> pre.test(mat);
	}
}