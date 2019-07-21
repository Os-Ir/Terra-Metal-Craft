package com.osir.tmc.item;

import java.util.List;

import com.osir.tmc.Main;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMould extends Item {
	public ItemMould() {
		this.setRegistryName("mould");
	}

	@Override
	public void addInformation(ItemStack stack, World world, List tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.format("item.mould.description"));
	}

	@Override
	public ActionResult onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			ItemStack stack = player.getHeldItem(hand);
			if (stack.getCount() == 1) {
				BlockPos pos = player.getPosition();
				player.openGui(Main.instance, 2, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return super.onItemRightClick(world, player, hand);
	}
}