package com.osir.tmc.item;

import java.util.List;

import com.osir.tmc.Main;
import com.osir.tmc.api.capability.ModCapabilities;
import com.osir.tmc.api.capability.IHeatable;
import com.osir.tmc.api.capability.ILiquidContainer;
import com.osir.tmc.api.heat.HeatMaterial;

import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemMould extends ItemBlock {
	public ItemMould(Block block) {
		super(block);
		this.setRegistryName(Main.MODID, "mould");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack mould = player.getHeldItem(hand);
		if (mould.hasCapability(ModCapabilities.LIQUID_CONTAINER, null)) {
			ILiquidContainer cap = mould.getCapability(ModCapabilities.LIQUID_CONTAINER, null);
			List<IHeatable> list = cap.getMaterial();
			if (!list.isEmpty()) {
				IHeatable heat = list.get(0);
				HeatMaterial material = heat.getMaterial();
				ItemStack stack = OreDictUnifier.get(OrePrefix.ingot, material.getMaterial());
				if (heat.getUnit() >= 144 && !heat.isMelt() && !stack.isEmpty()) {
					heat.increaseUnit(-144, true);
					if (heat.getUnit() == 0) {
						list.remove(0);
					}
					player.addItemStackToInventory(stack);
					if (!stack.isEmpty()) {
						player.dropItem(stack, false, false);
					}
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}