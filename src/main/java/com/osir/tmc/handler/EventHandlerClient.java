package com.osir.tmc.handler;

import java.util.List;

import com.osir.tmc.Main;
import com.osir.tmc.api.capability.IHeatable;
import com.osir.tmc.api.capability.ILiquidContainer;
import com.osir.tmc.api.heat.HeatRecipe;
import com.osir.tmc.api.heat.HeatRegistry;
import com.osir.tmc.block.BlockAnvil;
import com.osir.tmc.block.BlockOriginalForge;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid = Main.MODID)
@SideOnly(Side.CLIENT)
public class EventHandlerClient {
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelColor(ColorHandlerEvent.Item e) {
		ItemColors itemColors = e.getItemColors();
		BlockColors blockColors = e.getBlockColors();
		itemColors.registerItemColorHandler((stack, idx) -> {
			if (stack.getItem() instanceof ItemBlock) {
				Block block = ((ItemBlock) stack.getItem()).getBlock();
				if (block instanceof BlockAnvil) {
					return ((BlockAnvil) block).getAnvilMaterial().getColor();
				}
			}
			return 0xffffff;
		}, BlockHandler.ANVIL);
		blockColors.registerBlockColorHandler((state, world, pos, idx) -> {
			Block block = state.getBlock();
			if (block instanceof BlockAnvil) {
				return ((BlockAnvil) block).getAnvilMaterial().getColor();
			}
			return 0xffffff;
		}, BlockHandler.ANVIL);
		itemColors.registerItemColorHandler((stack, idx) -> {
			return 0x202020;
		}, ItemHandler.ITEM_ORIGINAL_FORGE);
		blockColors.registerBlockColorHandler((state, world, pos, idx) -> {
			Block block = state.getBlock();
			if (block instanceof BlockOriginalForge && idx == 1) {
				return state.getValue(BlockOriginalForge.BURN) ? 0x820a0a : 0x202020;
			}
			return 0xffffff;
		}, BlockHandler.ORIGINAL_FORGE);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onHeatableItemTooltip(ItemTooltipEvent e) {
		ItemStack stack = e.getItemStack();
		if (!stack.hasCapability(CapabilityHandler.HEATABLE, null)) {
			return;
		}
		IHeatable cap = stack.getCapability(CapabilityHandler.HEATABLE, null);
		List tooltip = e.getToolTip();
		long time = 0;
		if (e.getEntityPlayer() != null && e.getEntityPlayer().getEntityWorld() != null) {
			time = e.getEntityPlayer().getEntityWorld().getTotalWorldTime();
		}
		tooltip.add(TextFormatting.AQUA + I18n.format("item.heatable.tip.title"));
		tooltip.add(TextFormatting.AQUA + "--" + I18n.format("item.heatable.tip.heating"));
		HeatRecipe recipe = HeatRegistry.findRecipe(stack);
		ItemStack output = recipe.getOutput();
		if (output == null || output.isEmpty()) {
			tooltip.add(TextFormatting.AQUA + "--" + I18n.format("item.heatable.tip.melting"));
		} else {
			tooltip.add(TextFormatting.AQUA + "--" + I18n.format("item.heatable.tip.making") + " "
					+ TextFormatting.YELLOW + I18n.format(output.getItem().getUnlocalizedName() + ".name"));
		}
		tooltip.add(TextFormatting.BLUE + I18n.format("item.heatable.material.meltPoint") + " " + TextFormatting.GOLD
				+ cap.getMeltTemp() + TextFormatting.GREEN + I18n.format("item.unit.temperature"));
		tooltip.add(TextFormatting.BLUE + I18n.format("item.heatable.material.specificHeat") + " " + TextFormatting.GOLD
				+ cap.getSpecificHeat() + TextFormatting.GREEN + I18n.format("item.unit.specificHeat"));
		tooltip.add(TextFormatting.BLUE + I18n.format("item.heatable.state.volume") + " " + TextFormatting.GOLD
				+ cap.getUnit() + TextFormatting.GREEN + I18n.format("item.unit.volume"));
		int temp = cap.getTemp();
		tooltip.add(TextFormatting.BLUE + I18n.format("item.heatable.state.temperature") + " " + TextFormatting.GOLD
				+ temp + TextFormatting.GREEN + I18n.format("item.unit.temperature"));
		if (cap.getUnit() != cap.getCompleteUnit()) {
			tooltip.add(TextFormatting.WHITE + I18n.format("item.heatable.state.melting"));
		} else {
			String info = "";
			if (cap.isDanger()) {
				info += (time % 10 < 5 ? TextFormatting.RED : TextFormatting.WHITE)
						+ I18n.format("item.heatable.state.danger") + TextFormatting.WHITE + " | ";
			}
			if (cap.isWeldable()) {
				info += TextFormatting.WHITE + I18n.format("item.heatable.state.weldable") + " | ";
			}
			if (cap.isWorkable()) {
				info += TextFormatting.WHITE + I18n.format("item.heatable.state.workable");
			}
			if (!info.equals("")) {
				tooltip.add(info);
			}
		}
		if (cap.getTemp() != 20) {
			tooltip.add(cap.getColor());
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onLiquidContainerTooltip(ItemTooltipEvent e) {
		ItemStack stack = e.getItemStack();
		if (!stack.hasCapability(CapabilityHandler.LIQUID_CONTAINER, null)) {
			return;
		}
		ILiquidContainer cap = stack.getCapability(CapabilityHandler.LIQUID_CONTAINER, null);
		List tooltip = e.getToolTip();
		tooltip.add(TextFormatting.BLUE + I18n.format("item.liquidContainer.state.capacity") + " " + TextFormatting.GOLD
				+ cap.getCapacity() + TextFormatting.GREEN + I18n.format("item.unit.volume"));
	}
}