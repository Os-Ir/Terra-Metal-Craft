package com.osir.tmc.handler.recipe;

import com.osir.tmc.api.heat.HeatMaterialList;
import com.osir.tmc.api.heat.MaterialStack;
import com.osir.tmc.api.recipe.RecipeMapList;

import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class HeatRecipeHandler {
	public static void register() {
		HeatMaterialList.REGISTRY_HEATABLE_MATERIAL.register(0, Materials.Iron, HeatMaterialList.IRON);
		HeatMaterialList.REGISTRY_HEATABLE_MATERIAL.register(1, Materials.Copper, HeatMaterialList.COPPER);
		HeatMaterialList.REGISTRY_HEATABLE_MATERIAL.register(2, Materials.Silver, HeatMaterialList.SILVER);
		HeatMaterialList.REGISTRY_HEATABLE_MATERIAL.register(3, Materials.Tin, HeatMaterialList.TIN);
		HeatMaterialList.REGISTRY_HEATABLE_MATERIAL.register(4, Materials.Antimony, HeatMaterialList.ANTIMONY);
		HeatMaterialList.REGISTRY_HEATABLE_MATERIAL.register(5, Materials.Gold, HeatMaterialList.GOLD);
		HeatMaterialList.REGISTRY_HEATABLE_MATERIAL.register(6, Materials.Lead, HeatMaterialList.LEAD);
		HeatMaterialList.REGISTRY_HEATABLE_MATERIAL.register(7, Materials.Bismuth, HeatMaterialList.BISMUTH);

		HeatMaterialList.REGISTRY_HEATABLE_MATERIAL.register(100, Materials.WroughtIron, HeatMaterialList.WROUGHT_IRON);
		HeatMaterialList.REGISTRY_HEATABLE_MATERIAL.register(101, Materials.PigIron, HeatMaterialList.PIG_IRON);
		HeatMaterialList.REGISTRY_HEATABLE_MATERIAL.register(102, Materials.Steel, HeatMaterialList.STEEL);
		HeatMaterialList.REGISTRY_HEATABLE_MATERIAL.register(103, Materials.Bronze, HeatMaterialList.BRONZE);
		HeatMaterialList.REGISTRY_HEATABLE_MATERIAL.register(104, Materials.BlackBronze, HeatMaterialList.BLACK_BRONZE);
		HeatMaterialList.REGISTRY_HEATABLE_MATERIAL.register(105, Materials.BismuthBronze,
				HeatMaterialList.BISMUTH_BRONZE);

		HeatMaterialList.REGISTRY_HEATABLE_MATERIAL.register(200, Materials.Glass, HeatMaterialList.GLASS);

		HeatMaterialList.REGISTRY_HEATABLE_PREFIX.register(0, "dust", OrePrefix.dust);
		HeatMaterialList.REGISTRY_HEATABLE_PREFIX.register(1, "ingot", OrePrefix.ingot);
		HeatMaterialList.REGISTRY_HEATABLE_PREFIX.register(2, "nugget", OrePrefix.nugget);
		HeatMaterialList.REGISTRY_HEATABLE_PREFIX.register(3, "plate", OrePrefix.plate);
		HeatMaterialList.REGISTRY_HEATABLE_PREFIX.register(4, "ingotDouble", OrePrefix.valueOf("ingotDouble"));
		HeatMaterialList.REGISTRY_HEATABLE_PREFIX.register(5, "plateDouble", OrePrefix.valueOf("plateDouble"));
		HeatMaterialList.REGISTRY_HEATABLE_PREFIX.register(6, "stick", OrePrefix.stick);
		HeatMaterialList.REGISTRY_HEATABLE_PREFIX.register(7, "stickLong", OrePrefix.stickLong);
		HeatMaterialList.REGISTRY_HEATABLE_PREFIX.register(8, "ring", OrePrefix.ring);

		RecipeMapList.MAP_HEAT.recipeBuilder().setValue("temp", 820).setValue("material", MaterialStack.EMPTY)
				.input("sand", 1).outputs(new ItemStack(Blocks.GLASS)).buildAndRegister();

		RecipeMapList.MAP_MATERIAL.recipeBuilder().setValue("material", new MaterialStack(HeatMaterialList.GLASS, 144))
				.input("sand", 1).buildAndRegister();
	}
}