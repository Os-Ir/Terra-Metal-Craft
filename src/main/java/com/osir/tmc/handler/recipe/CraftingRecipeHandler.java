package com.osir.tmc.handler.recipe;

import com.osir.tmc.api.TMCLog;
import com.osir.tmc.item.MetaItems;

import gregtech.api.recipes.ModHandler;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.UnificationEntry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CraftingRecipeHandler {
	public static void register() {
		ModHandler.addShapedRecipe("tmc_coin", MetaItems.COIN_TMC.getStackForm(), " h ", " PF", " k ", 'P',
				new UnificationEntry(OrePrefix.plate, Materials.Gold), 'F',
				new UnificationEntry(OrePrefix.foil, Materials.Lead));
	}
}