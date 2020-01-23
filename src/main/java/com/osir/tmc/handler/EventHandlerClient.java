package com.osir.tmc.handler;

import java.util.List;

import com.osir.tmc.Main;
import com.osir.tmc.api.capability.CapabilityList;
import com.osir.tmc.api.capability.IHeatable;
import com.osir.tmc.api.capability.ILiquidContainer;
import com.osir.tmc.api.heat.HeatMaterial;
import com.osir.tmc.api.util.DividedInfoBuilder;
import com.osir.tmc.api.util.InfoBuf;
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
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onHeatableItemTooltip(ItemTooltipEvent e) {
		ItemStack stack = e.getItemStack();
		if (!stack.hasCapability(CapabilityList.HEATABLE, null)) {
			return;
		}
		IHeatable cap = stack.getCapability(CapabilityList.HEATABLE, null);
		List<String> tooltip = e.getToolTip();
		HeatMaterial material = cap.getMaterial();
		DividedInfoBuilder builder = new DividedInfoBuilder();
		builder.addInfo(new InfoBuf("m", material.getMeltTemp(), TextFormatting.RED, "\u2103", TextFormatting.GREEN));
		builder.addInfo(new InfoBuf("c", material.getSpecificHeat(), TextFormatting.RED, "J/kg·\u2103", TextFormatting.GREEN)
				.setAccuracy(2));
		tooltip.add(cap.getTemp() + "\u2103");
		tooltip.add(String.format(TextFormatting.BLUE + "%.3f" + TextFormatting.YELLOW, ((float) cap.getUnit()) / 144)
				+ " " + material.getLocalizedName() + builder.build());
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onLiquidContainerTooltip(ItemTooltipEvent e) {
		ItemStack stack = e.getItemStack();
		if (!stack.hasCapability(CapabilityList.LIQUID_CONTAINER, null)) {
			return;
		}
		ILiquidContainer cap = stack.getCapability(CapabilityList.LIQUID_CONTAINER, null);
		List tooltip = e.getToolTip();
		tooltip.add(TextFormatting.BLUE + I18n.format("item.liquidContainer.state.capacity") + " " + TextFormatting.GOLD
				+ cap.getCapacity() + TextFormatting.GREEN + I18n.format("item.unit.volume"));
	}
}