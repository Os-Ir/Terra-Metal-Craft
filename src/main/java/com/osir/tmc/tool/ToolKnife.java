package com.osir.tmc.tool;

import com.github.zi_jing.cuckoolib.metaitem.tool.ToolInfoBase;
import com.osir.tmc.Main;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ToolKnife extends ToolInfoBase {
	public static final ToolKnife INSTANCE = new ToolKnife();

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Main.MODID, "knife");

	private ToolKnife() {

	}

	@Override
	public ResourceLocation getRegistryName() {
		return REGISTRY_NAME;
	}

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
		String tool = state.getBlock().getHarvestTool(state);
		if (tool != null && (tool.equals("sword") || tool.equals("knife"))) {
			return true;
		}
		return state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.GOURD
				|| state.getMaterial() == Material.VINE || state.getMaterial() == Material.WEB
				|| state.getMaterial() == Material.CLOTH || state.getMaterial() == Material.CARPET
				|| state.getMaterial() == Material.PLANTS || state.getMaterial() == Material.CACTUS
				|| state.getMaterial() == Material.CAKE || state.getMaterial() == Material.TNT
				|| state.getMaterial() == Material.SPONGE;
	}

	@Override
	public float getDestroySpeed(IBlockState state, ItemStack stack) {
		return 5;
	}
}