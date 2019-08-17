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
	public static void onHeatableItemTooltip(ItemTooltipEvent e) {
		ItemStack stack = e.getItemStack();
		IHeatable cap = stack.getCapability(CapabilityHandler.heatable, null);
		if (cap == null) {
			return;
		}
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
		tooltip.add(TextFormatting.BLUE + I18n.format("item.heatable.material.meltPoint") + " " + TextFormatting.GOLD
				+ cap.getMeltTemp() + TextFormatting.GREEN + I18n.format("item.unit.temperature"));
		tooltip.add(TextFormatting.BLUE + I18n.format("item.heatable.material.specificHeat") + " " + TextFormatting.GOLD
				+ cap.getSpecificHeat() + TextFormatting.GREEN + I18n.format("item.unit.specificHeat"));
		tooltip.add(TextFormatting.BLUE + I18n.format("item.heatable.state.volume") + " " + TextFormatting.GOLD
				+ cap.getUnit() + TextFormatting.GREEN + I18n.format("item.unit.volume"));
		int temp = cap.getTemp();
		tooltip.add(TextFormatting.BLUE + I18n.format("item.heatable.state.temperature") + " " + TextFormatting.GOLD
				+ temp + TextFormatting.GREEN + I18n.format("item.unit.temperature"));
		if (temp != 20) {
			float t = (float) (temp - 20) / (cap.getMeltTemp() - 20);
			String info = "";
			if (t >= 0.9) {
				info += (e.getEntityPlayer().getEntityWorld().getTotalWorldTime() % 2 == 0 ? TextFormatting.RED
						: TextFormatting.WHITE) + I18n.format("item.heatable.state.danger") + TextFormatting.WHITE
						+ " | ";
			}
			if (t >= 0.8) {
				info += TextFormatting.WHITE + I18n.format("item.heatable.state.weldable") + " | ";
			}
			if (t >= 0.6) {
				info += TextFormatting.WHITE + I18n.format("item.heatable.state.workable");
			}
			if (!info.equals("")) {
				tooltip.add(info);
			}
			tooltip.add(cap.getColor());
		}
	}

	@SubscribeEvent
	public static void onTempUpdate(LivingUpdateEvent e) {
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
}