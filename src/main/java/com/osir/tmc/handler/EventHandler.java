package com.osir.tmc.handler;

import java.util.List;

import com.osir.tmc.Main;
import com.osir.tmc.api.heat.HeatRecipe;
import com.osir.tmc.api.heat.HeatRegistry;
import com.osir.tmc.api.inter.IHeatable;
import com.osir.tmc.capability.CapabilityHeat;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid = Main.MODID)
public class EventHandler {
	@SubscribeEvent
	public static void onAttachCapabilitiesItem(AttachCapabilitiesEvent<ItemStack> e) {
		ItemStack stack = e.getObject();
		if (stack.hasCapability(CapabilityHandler.heatable, null)) {
			return;
		}
		HeatRecipe recipe = HeatRegistry.findRecipe(stack);
		if (recipe != null) {
			e.addCapability(CapabilityHeat.KEY,
					new CapabilityHeat.Implementation(recipe.getMaterial(), recipe.getUnit()));
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void heatableItemTooltip(ItemTooltipEvent e) {
		ItemStack stack = e.getItemStack();
		IHeatable cap = stack.getCapability(CapabilityHandler.heatable, null);
		if (cap != null) {
			List tooltip = e.getToolTip();
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
			tooltip.add(
					TextFormatting.BLUE + I18n.format("item.heatable.material.meltPoint") + " " + TextFormatting.GOLD
							+ cap.getMeltTemp() + TextFormatting.GREEN + I18n.format("item.unit.temperature"));
			tooltip.add(
					TextFormatting.BLUE + I18n.format("item.heatable.material.specificHeat") + " " + TextFormatting.GOLD
							+ cap.getSpecificHeat() + TextFormatting.GREEN + I18n.format("item.unit.specificHeat"));
			tooltip.add(TextFormatting.BLUE + I18n.format("item.heatable.state.volume") + " " + TextFormatting.GOLD
					+ cap.getUnit() + TextFormatting.GREEN + I18n.format("item.unit.volume"));
			tooltip.add(TextFormatting.BLUE + I18n.format("item.heatable.state.temperature") + " " + TextFormatting.GOLD
					+ cap.getTemp() + TextFormatting.GREEN + I18n.format("item.unit.temperature"));
		}
	}

	@SubscribeEvent
	public static void updateTemp(LivingUpdateEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntityLiving();
			InventoryPlayer inv = player.inventory;
			int i;
			for (i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (!stack.hasCapability(CapabilityHandler.heatable, null)) {
					continue;
				}
				IHeatable cap = stack.getCapability(CapabilityHandler.heatable, null);
				float delta = Math.min((20 - cap.getTemp()) * 0.05F, -4);
				cap.setIncreaseEnergy(delta);
			}
		}
	}

	// @SubscribeEvent
	// public static void tooltip(ItemTooltipEvent e) {
	// ItemStack stack = e.getItemStack();
	// EntityPlayer player = e.getEntityPlayer();
	// List tooltip = e.getToolTip();
	// if (HeatTool.hasEnergy(stack)) {
	// String str = "";
	// ItemStack output = HeatRegistry.findIndex(stack).getOutput();
	// if (HeatTool.isMelt(stack)) {
	// str += TextFormatting.WHITE + I18n.format("item.heatable.meltUnit",
	// HeatTool.getMeltedUnit(stack),
	// HeatTool.getCompleteUnit(stack));
	// } else {
	// if (HeatTool.isDanger(stack)) {
	// if (player.getEntityWorld().getTotalWorldTime() % 20 < 10) {
	// str += TextFormatting.WHITE;
	// } else {
	// str += TextFormatting.RED;
	// }
	// str += I18n.format("item.heatable.danger") + TextFormatting.WHITE + " | ";
	// }
	// if (HeatTool.isWeldable(stack)) {
	// str += TextFormatting.WHITE + I18n.format("item.heatable.weldable") + " | ";
	// }
	// if (HeatTool.isWorkable(stack)) {
	// str += TextFormatting.WHITE + I18n.format("item.heatable.workable");
	// }
	// }
	// if (!str.equals("")) {
	// tooltip.add(str);
	// }
	// tooltip.add(I18n.format("item.heatable.temperature", (int)
	// HeatTool.getTemp(stack)) + " | "
	// + HeatTool.getHeatColor(stack));
	// }
	// }
	//
	// @SubscribeEvent
	// public static void tempUpdate(LivingUpdateEvent e) {
	// EntityLivingBase living = e.getEntityLiving();
	// if (!(living instanceof EntityPlayer)) {
	// return;
	// }
	// EntityPlayer player = (EntityPlayer) living;
	// InventoryPlayer inventory = player.inventory;
	// int i;
	// for (i = 0; i < inventory.getSizeInventory(); i++) {
	// ItemStack stack = inventory.getStackInSlot(i);
	// if (HeatTool.hasEnergy(stack)) {
	// float delta = HeatTool.update(stack, 20, 0.1F);
	// inventory.setInventorySlotContents(i, HeatTool.setEnergy(stack, delta));
	// }
	// }
	// }
}