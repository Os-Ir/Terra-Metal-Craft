package com.osir.tmc.tool;

import com.github.zi_jing.cuckoolib.metaitem.tool.ToolInfoBase;
import com.osir.tmc.Main;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ToolHammer extends ToolInfoBase {
	public static final ToolHammer INSTANCE = new ToolHammer();

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Main.MODID, "hammer");

	@Override
	public ResourceLocation getRegistryName() {
		return REGISTRY_NAME;
	}

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
		String tool = state.getBlock().getHarvestTool(state);
		if (tool != null && (tool.equals("pickage") || tool.equals("hammer"))) {
			return true;
		}
		return false;
	}

	@Override
	public float getDestroySpeed(IBlockState state, ItemStack stack) {
		return 5;
	}
}