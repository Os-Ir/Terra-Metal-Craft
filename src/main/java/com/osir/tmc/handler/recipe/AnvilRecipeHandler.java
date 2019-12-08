package com.osir.tmc.handler.recipe;

import java.util.ArrayList;
import java.util.List;

import com.osir.tmc.api.recipe.AnvilRecipeType;
import com.osir.tmc.api.recipe.TMCRecipeMap;
import com.osir.tmc.api.recipe.ScalableRecipe;
import com.osir.tmc.api.recipe.ScalableRecipeBuilder;

import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AnvilRecipeHandler {
	private static final ScalableRecipeBuilder BUILDER = TMCRecipeMap.BUILDER_ANVIL;

	public static void register() {
		BUILDER.EUt(1);
		BUILDER.duration(1);
		BUILDER.addFormat(TMCRecipeMap.FORMAT_ANVIL);

		BUILDER.setValue("type", AnvilRecipeType.WELD);
		BUILDER.input(OrePrefix.ingot, Materials.Iron, 2);
		BUILDER.outputs(OreDictUnifier.get(OrePrefix.valueOf("ingotDouble"), Materials.Iron, 1));
		BUILDER.buildAndRegister();

		List input = new ArrayList();
		input.add(new ItemStack(Items.IRON_INGOT, 2));
		ScalableRecipe recipe = (ScalableRecipe) TMCRecipeMap.MAP_ANVIL.findRecipe(1, input, new ArrayList(), 0);
		System.out.println(recipe.getValueList().size());
	}
}