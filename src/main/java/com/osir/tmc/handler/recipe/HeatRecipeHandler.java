package com.osir.tmc.handler.recipe;

import com.osir.tmc.api.heat.HeatMaterialList;
import com.osir.tmc.api.recipe.ScalableRecipeBuilder;
import com.osir.tmc.api.recipe.TMCRecipeMap;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class HeatRecipeHandler {
	private static final ScalableRecipeBuilder BUILDER = TMCRecipeMap.BUILDER_HEAT;

	public static void register() {
		BUILDER.EUt(1);
		BUILDER.duration(1);

		BUILDER.addFormat(TMCRecipeMap.FORMAT_TEMP);
		BUILDER.addFormat(TMCRecipeMap.FORMAT_MATERIAL);

		BUILDER.setValue("temp", 820);
		BUILDER.setValue("material", HeatMaterialList.EMPTY);
		BUILDER.input("sand", 1);
		BUILDER.outputs(new ItemStack(Blocks.GLASS));
	}
}