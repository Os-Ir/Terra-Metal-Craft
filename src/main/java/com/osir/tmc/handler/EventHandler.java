package com.osir.tmc.handler;

import java.util.List;

import com.osir.tmc.Main;

import api.osir.tmc.heat.HeatRegistry;
import api.osir.tmc.heat.HeatTool;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Main.MODID)
public class EventHandler {
	@SubscribeEvent
	public static void tooltip(ItemTooltipEvent e) {
		ItemStack stack = e.getItemStack();
		EntityPlayer player = e.getEntityPlayer();
		List tooltip = e.getToolTip();
		if (HeatTool.hasEnergy(stack)) {
			String str = "";
			ItemStack output = HeatRegistry.findIndex(stack).getOutput();
			if (HeatTool.isMelt(stack)) {
				str += TextFormatting.WHITE + I18n.format("item.heatable.meltUnit", HeatTool.getMeltedUnit(stack),
						HeatTool.getCompleteUnit(stack));
			} else {
				if (HeatTool.isDanger(stack)) {
					if (player.getEntityWorld().getTotalWorldTime() % 20 < 10) {
						str += TextFormatting.WHITE;
					} else {
						str += TextFormatting.RED;
					}
					str += I18n.format("item.heatable.danger") + TextFormatting.WHITE + " | ";
				}
				if (HeatTool.isWeldable(stack)) {
					str += TextFormatting.WHITE + I18n.format("item.heatable.weldable") + " | ";
				}
				if (HeatTool.isWorkable(stack)) {
					str += TextFormatting.WHITE + I18n.format("item.heatable.workable");
				}
			}
			if (!str.equals("")) {
				tooltip.add(str);
			}
			tooltip.add(I18n.format("item.heatable.temperature", (int) HeatTool.getTemp(stack)) + " | "
					+ HeatTool.getHeatColor(stack));
		}
	}

	@SubscribeEvent
	public static void tempUpdate(LivingUpdateEvent e) {
		EntityLivingBase living = e.getEntityLiving();
		if (!(living instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer) living;
		InventoryPlayer inventory = player.inventory;
		int i;
		for (i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (HeatTool.hasEnergy(stack)) {
				float delta = HeatTool.update(stack, 20, 0.1F);
				inventory.setInventorySlotContents(i, HeatTool.setEnergy(stack, delta));
			}
		}
	}
}