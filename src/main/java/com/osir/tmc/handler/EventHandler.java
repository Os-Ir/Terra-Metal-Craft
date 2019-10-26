package com.osir.tmc.handler;

import com.osir.tmc.Main;
import com.osir.tmc.api.heat.HeatRecipe;
import com.osir.tmc.api.heat.HeatRegistry;
import com.osir.tmc.api.inter.IHeatable;
import com.osir.tmc.capability.CapabilityHeat;
import com.osir.tmc.capability.CapabilityLiquidContainer;
import com.osir.tmc.capability.ItemStackInventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Main.MODID)
public class EventHandler {
	@SubscribeEvent
	public static void onAttachCapabilitiesItem(AttachCapabilitiesEvent<ItemStack> e) {
		ItemStack stack = e.getObject();
		if (stack.hasCapability(CapabilityHandler.HEATABLE, null)) {
			return;
		}
		HeatRecipe recipe = HeatRegistry.findRecipe(stack);
		if (recipe != null) {
			e.addCapability(CapabilityHeat.KEY,
					new CapabilityHeat.Implementation(recipe.getMaterial(), recipe.getUnit()));
		}
		if (stack.getItem() == ItemHandler.ITEM_MOULD) {
			e.addCapability(CapabilityLiquidContainer.KEY, new CapabilityLiquidContainer.Implementation(1, 144, 230));
			e.addCapability(ItemStackInventory.KEY, new ItemStackInventory(1));
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
				if (!stack.hasCapability(CapabilityHandler.HEATABLE, null)) {
					continue;
				}
				IHeatable cap = stack.getCapability(CapabilityHandler.HEATABLE, null);
				float delta = Math.min((20 - cap.getTemp()) * 5, -200);
				cap.setIncreaseEnergy(delta);
			}
		}
	}
}