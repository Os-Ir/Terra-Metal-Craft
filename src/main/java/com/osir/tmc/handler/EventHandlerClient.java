package com.osir.tmc.handler;

import java.util.List;

import com.osir.tmc.Main;
import com.osir.tmc.api.capability.CapabilityList;
import com.osir.tmc.api.capability.IHeatable;
import com.osir.tmc.api.capability.ILiquidContainer;
import com.osir.tmc.api.capability.IWorkable;
import com.osir.tmc.api.heat.HeatMaterial;
import com.osir.tmc.api.util.DividedInfoBuilder;
import com.osir.tmc.block.BlockAnvil;

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

@EventBusSubscriber(modid = Main.MODID, value = Side.CLIENT)
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
		String str = TextFormatting.WHITE + "" + cap.getTemp() + "\u2103";
		if (cap.getTemp() != 20) {
			str += " | " + cap.getColor() + TextFormatting.RESET;
		}
		if (cap.isWorkable()) {
			str += " | " + I18n.format("item.heatable.state.workable");
		}
		if (cap.isWeldable()) {
			str += " | " + I18n.format("item.heatable.state.weldable");
		}
		if (cap.isDanger()) {
			str += " | " + (e.getEntityPlayer().world.getTotalWorldTime() % 10 < 5 ? "" : TextFormatting.RED)
					+ I18n.format("item.heatable.state.danger");
		}
		tooltip.add(str);
		if (cap.getMeltProgress() > 0) {
			tooltip.add(I18n.format("item.heatable.state.melt") + " "
					+ String.format("%.1f%%", cap.getMeltProgress() * 100));
		}
		HeatMaterial material = cap.getMaterial();
		DividedInfoBuilder builder = new DividedInfoBuilder();
		builder.addInfo(new DividedInfoBuilder.InfoBuf("M", material.getMeltTemp(), TextFormatting.RED, "\u2103"));
		builder.addInfo(
				new DividedInfoBuilder.InfoBuf("C", material.getSpecificHeat(), TextFormatting.AQUA, "J/(L*\u2103)")
						.setAccuracy(2));
		tooltip.add(I18n.format("item.heatable.material"));
		tooltip.add(
				String.format(TextFormatting.BLUE + "%.3f", ((float) cap.getUnit()) / 144) + " " + TextFormatting.YELLOW
						+ material.getLocalizedName() + TextFormatting.RESET + " ( " + builder.build() + " )");
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onWorkableItemTooltip(ItemTooltipEvent e) {
		ItemStack stack = e.getItemStack();
		if (!stack.hasCapability(CapabilityList.WORKABLE, null)) {
			return;
		}
		IWorkable cap = stack.getCapability(CapabilityList.WORKABLE, null);
		List<String> tooltip = e.getToolTip();
		if (cap.isWorked()) {
			tooltip.add(I18n.format("item.workable.worked"));
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onLiquidContainerTooltip(ItemTooltipEvent e) {
		ItemStack stack = e.getItemStack();
		if (!stack.hasCapability(CapabilityList.LIQUID_CONTAINER, null)) {
			return;
		}
		ILiquidContainer cap = stack.getCapability(CapabilityList.LIQUID_CONTAINER, null);
		List<String> tooltip = e.getToolTip();
		tooltip.add(TextFormatting.BLUE + I18n.format("item.liquidContainer.state.capacity") + " " + TextFormatting.GOLD
				+ cap.getCapacity() + TextFormatting.GREEN + "L");
		if (!cap.getMaterial().isEmpty()) {
			tooltip.add(I18n.format("item.liquidContainer.material"));
		}
		for (IHeatable heat : cap.getMaterial()) {
			DividedInfoBuilder builder = new DividedInfoBuilder();
			builder.addInfo(new DividedInfoBuilder.InfoBuf("M", heat.getMeltTemp(), TextFormatting.RED, "\u2103"));
			builder.addInfo(new DividedInfoBuilder.InfoBuf("C", heat.getMaterial().getSpecificHeat(),
					TextFormatting.AQUA, "J/(L*\u2103)").setAccuracy(2));
			builder.addInfo(new DividedInfoBuilder.InfoBuf("T", heat.getTemp(), TextFormatting.RED, "\u2103"));
			tooltip.add(String.format(TextFormatting.BLUE + "%.3f", ((float) heat.getUnit()) / 144) + " "
					+ TextFormatting.YELLOW + heat.getMaterial().getLocalizedName() + TextFormatting.RESET + " ( "
					+ builder.build() + " )");
		}
	}
}