package com.osir.tmc.api.recipe;

import com.osir.tmc.api.heat.MaterialStack;
import com.osir.tmc.api.util.ItemIndex;
import com.osir.tmc.api.util.function.Validation;

import gregtech.api.recipes.RecipeMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.RegistrySimple;

public class ModRecipeMap {
	public static final RecipeValueFormat FORMAT_ANVIL = new RecipeValueFormat("type",
			(obj) -> obj instanceof AnvilRecipeType, AnvilRecipeType.WELD);
	public static final RecipeValueFormat FORMAT_TEMP = new RecipeValueFormat("temp", (obj) -> obj instanceof Integer,
			20);
	public static final RecipeValueFormat FORMAT_MATERIAL = new RecipeValueFormat("material",
			(obj) -> obj instanceof MaterialStack, MaterialStack.EMPTY);

	public static final RecipeMap<ScalableRecipeBuilder> MAP_ANVIL = new RecipeMap<ScalableRecipeBuilder>("anvil", 1, 2,
			1, 4, 0, 0, 0, 0, new ScalableRecipeBuilder().addFormat(FORMAT_ANVIL).EUt(1).duration(1));
	public static final RecipeMap<ScalableRecipeBuilder> MAP_HEAT = new RecipeMap<ScalableRecipeBuilder>("anvil", 1, 1,
			0, 1, 0, 0, 0, 0,
			new ScalableRecipeBuilder().addFormat(FORMAT_TEMP).addFormat(FORMAT_MATERIAL).EUt(1).duration(1));

	public static final RegistrySimple<ItemIndex, MaterialStack> REGISTRY_MATERIAL = new RegistrySimple<ItemIndex, MaterialStack>();

	public static final Validation<ItemIndex, ItemStack> VALI_ITEM = (idx, stack) -> idx.getItem() == stack.getItem();
	public static final Validation<ItemIndex, ItemStack> VALI_META = (idx,
			stack) -> idx.getMeta() == stack.getItemDamage();
	public static final Validation<ItemIndex, ItemStack> VALI_STACK = (idx, stack) -> VALI_ITEM.test(idx, stack)
			&& VALI_META.test(idx, stack);
}