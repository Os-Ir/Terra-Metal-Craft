package com.osir.tmc.handler.recipe;

import com.osir.tmc.api.heat.HeatMaterialList;
import com.osir.tmc.api.heat.MaterialStack;
import com.osir.tmc.api.recipe.ModRecipeMap;
import com.osir.tmc.api.recipe.ModRegistry;

import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class HeatRecipeHandler {
	public static void register() {
		ModRegistry.REGISTRY_HEATABLE_MATERIAL.register(0, Materials.Iron, HeatMaterialList.IRON);
		ModRegistry.REGISTRY_HEATABLE_MATERIAL.register(1, Materials.Copper, HeatMaterialList.COPPER);
		ModRegistry.REGISTRY_HEATABLE_MATERIAL.register(2, Materials.Silver, HeatMaterialList.SILVER);
		ModRegistry.REGISTRY_HEATABLE_MATERIAL.register(3, Materials.Tin, HeatMaterialList.TIN);
		ModRegistry.REGISTRY_HEATABLE_MATERIAL.register(4, Materials.Antimony, HeatMaterialList.ANTIMONY);
		ModRegistry.REGISTRY_HEATABLE_MATERIAL.register(5, Materials.Gold, HeatMaterialList.GOLD);
		ModRegistry.REGISTRY_HEATABLE_MATERIAL.register(6, Materials.Lead, HeatMaterialList.LEAD);

		ModRegistry.REGISTRY_HEATABLE_MATERIAL.register(100, Materials.WroughtIron, HeatMaterialList.WROUGHT_IRON);
		ModRegistry.REGISTRY_HEATABLE_MATERIAL.register(101, Materials.PigIron, HeatMaterialList.PIG_IRON);
		ModRegistry.REGISTRY_HEATABLE_MATERIAL.register(102, Materials.Steel, HeatMaterialList.STEEL);

		ModRegistry.REGISTRY_HEATABLE_MATERIAL.register(200, Materials.Glass, HeatMaterialList.GLASS);

		ModRegistry.REGISTRY_HEATABLE_PREFIX.register(0, "dust", OrePrefix.dust);
		ModRegistry.REGISTRY_HEATABLE_PREFIX.register(1, "ingot", OrePrefix.ingot);
		ModRegistry.REGISTRY_HEATABLE_PREFIX.register(2, "nugget", OrePrefix.nugget);
		ModRegistry.REGISTRY_HEATABLE_PREFIX.register(3, "plate", OrePrefix.plate);
		ModRegistry.REGISTRY_HEATABLE_PREFIX.register(4, "ingotDouble", OrePrefix.valueOf("ingotDouble"));
		ModRegistry.REGISTRY_HEATABLE_PREFIX.register(5, "plateDouble", OrePrefix.valueOf("plateDouble"));

		ModRecipeMap.MAP_HEAT.recipeBuilder().setValue("temp", 820).setValue("material", MaterialStack.EMPTY)
				.input("sand", 1).outputs(new ItemStack(Blocks.GLASS)).buildAndRegister();

		ModRecipeMap.MAP_MATERIAL.recipeBuilder().setValue("material", new MaterialStack(HeatMaterialList.GLASS, 144))
				.input("sand", 1).buildAndRegister();
	}
}