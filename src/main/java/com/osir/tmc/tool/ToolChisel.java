package com.osir.tmc.tool;

import com.github.zi_jing.cuckoolib.metaitem.tool.ToolInfoBase;
import com.osir.tmc.Main;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ToolChisel extends ToolInfoBase {
	public static final ToolChisel INSTANCE = new ToolChisel();

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Main.MODID, "chisel");

	@Override
	public ResourceLocation getRegistryName() {
		return REGISTRY_NAME;
	}

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
		return false;
	}

	@Override
	public float getDestroySpeed(IBlockState state, ItemStack stack) {
		return 0;
	}
}