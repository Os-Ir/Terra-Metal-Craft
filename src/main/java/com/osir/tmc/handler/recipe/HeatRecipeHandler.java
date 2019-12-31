package com.osir.tmc.handler.recipe;

import com.osir.tmc.api.heat.HeatMaterialList;
import com.osir.tmc.api.heat.MaterialStack;
import com.osir.tmc.api.recipe.ModRecipeMap;
import com.osir.tmc.api.recipe.ScalableRecipeBuilder;
import com.osir.tmc.api.util.ItemIndex;

import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.RegistrySimple;

public class HeatRecipeHandler {
	public static void register() {
		ModRecipeMap.MAP_HEAT.recipeBuilder().setValue("temp", 820).setValue("material", MaterialStack.EMPTY)
				.input("sand", 1).outputs(new ItemStack(Blocks.GLASS)).buildAndRegister();

		ModRecipeMap.MAP_HEAT.recipeBuilder().setValue("temp", 1535)
				.setValue("material", new MaterialStack(HeatMaterialList.IRON, 144))
				.input(OrePrefix.ingot, Materials.Iron).buildAndRegister();

		ModRecipeMap.MAP_HEAT.recipeBuilder().setValue("temp", 1064)
				.setValue("material", new MaterialStack(HeatMaterialList.GOLD, 144))
				.input(OrePrefix.ingot, Materials.Gold).buildAndRegister();

		ModRecipeMap.REGISTRY_MATERIAL.putObject(new ItemIndex(Blocks.SAND, ModRecipeMap.VALI_STACK),
				new MaterialStack(HeatMaterialList.GLASS, 144));
		ModRecipeMap.REGISTRY_MATERIAL.putObject(new ItemIndex(Items.IRON_INGOT, ModRecipeMap.VALI_STACK),
				new MaterialStack(HeatMaterialList.IRON, 144));
		ModRecipeMap.REGISTRY_MATERIAL.putObject(new ItemIndex(Items.GOLD_INGOT, ModRecipeMap.VALI_STACK),
				new MaterialStack(HeatMaterialList.GOLD, 144));
	}
}