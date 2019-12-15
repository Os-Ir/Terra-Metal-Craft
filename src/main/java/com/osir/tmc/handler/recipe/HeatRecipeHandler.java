package com.osir.tmc.handler.recipe;

import com.osir.tmc.api.heat.HeatMaterialList;
import com.osir.tmc.api.heat.MaterialStack;
import com.osir.tmc.api.recipe.ModRecipeMap;
import com.osir.tmc.api.recipe.ScalableRecipeBuilder;
import com.osir.tmc.api.util.ItemIndex;
import com.osir.tmc.api.util.function.Validation;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.RegistrySimple;

public class HeatRecipeHandler {
	public static final RegistrySimple<ItemIndex, MaterialStack> MATERIAL = new RegistrySimple<ItemIndex, MaterialStack>();
	public static final Validation<ItemIndex, ItemStack> VALI_ITEM = (idx, stack) -> idx.getItem() == stack.getItem();
	public static final Validation<ItemIndex, ItemStack> VALI_META = (idx,
			stack) -> idx.getMeta() == stack.getItemDamage();
	public static final Validation<ItemIndex, ItemStack> VALI_STACK = (idx, stack) -> VALI_ITEM.test(idx, stack)
			&& VALI_META.test(idx, stack);

	private static final ScalableRecipeBuilder BUILDER = ModRecipeMap.BUILDER_HEAT;

	public static void register() {
		BUILDER.EUt(1);
		BUILDER.duration(1);
		BUILDER.addFormat(ModRecipeMap.FORMAT_TEMP);
		BUILDER.addFormat(ModRecipeMap.FORMAT_MATERIAL);

		MATERIAL.putObject(new ItemIndex(Blocks.SAND, VALI_ITEM), new MaterialStack(HeatMaterialList.GLASS, 144));
		MATERIAL.putObject(new ItemIndex(Items.IRON_INGOT, VALI_ITEM), new MaterialStack(HeatMaterialList.IRON, 144));
		MATERIAL.putObject(new ItemIndex(Items.GOLD_INGOT, VALI_ITEM), new MaterialStack(HeatMaterialList.GOLD, 144));

		BUILDER.setValue("temp", 820);
		BUILDER.setValue("material", HeatMaterialList.EMPTY);
		BUILDER.input("sand", 1);
		BUILDER.outputs(new ItemStack(Blocks.GLASS));
	}
}