package com.osir.tmc.handler;

import com.osir.tmc.Main;
import com.osir.tmc.api.capability.CapabilityHeat;
import com.osir.tmc.api.capability.CapabilityLiquidContainer;
import com.osir.tmc.api.capability.CapabilityList;
import com.osir.tmc.api.capability.CapabilityWork;
import com.osir.tmc.api.capability.IHeatable;
import com.osir.tmc.api.capability.ILiquidContainer;
import com.osir.tmc.api.container.ContainerListenerCapability;
import com.osir.tmc.api.heat.HeatMaterialList;
import com.osir.tmc.api.heat.MaterialStack;
import com.osir.tmc.api.recipe.ScalableRecipe;
import com.osir.tmc.api.util.CapabilityUtil;
import com.osir.tmc.handler.recipe.HeatRecipeHandler;
import com.osir.tmc.handler.recipe.OrePrefixRecipeHandler;
import com.osir.tmc.item.ItemMould;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent.Open;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

@EventBusSubscriber(modid = Main.MODID)
public class EventHandler {
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		HeatRecipeHandler.register();
		OrePrefixRecipeHandler.register();
	}

	@SubscribeEvent
	public static void onAttachHeat(AttachCapabilitiesEvent<ItemStack> e) {
		ItemStack stack = e.getObject();
		if (stack.hasCapability(CapabilityList.HEATABLE, null)) {
			return;
		}
		MaterialStack mat = HeatMaterialList.findMaterial(stack);
		ScalableRecipe recipe = HeatMaterialList.findRecipe(stack);
		if (mat != null && recipe != null) {
			e.addCapability(CapabilityHeat.KEY,
					new CapabilityHeat(mat.getMaterial(), mat.getAmount(), (int) recipe.getValue("temp")));
			e.addCapability(CapabilityWork.KEY, new CapabilityWork());
		}
	}

	@SubscribeEvent
	public static void onAttachLiquidContainer(AttachCapabilitiesEvent<ItemStack> e) {
		ItemStack stack = e.getObject();
		if (stack.hasCapability(CapabilityList.LIQUID_CONTAINER, null)) {
			return;
		}
		if (stack.getItem() instanceof ItemMould) {
			e.addCapability(CapabilityLiquidContainer.KEY, new CapabilityLiquidContainer(144));
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerLoggedInEvent e) {
		if (e.player instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) e.player;
			player.inventoryContainer.addListener(new ContainerListenerCapability(player));
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerRespawnEvent e) {
		if (e.player instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) e.player;
			player.inventoryContainer.addListener(new ContainerListenerCapability(player));
		}
	}

	@SubscribeEvent
	public static void onContainerOpen(Open e) {
		if (e.getEntityPlayer() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) e.getEntityPlayer();
			e.getContainer().addListener(new ContainerListenerCapability(player));
		}
	}

	@SubscribeEvent
	public static void onTempUpdate(LivingUpdateEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntityLiving();
			InventoryPlayer inv = player.inventory;
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (stack.hasCapability(CapabilityList.HEATABLE, null)) {
					IHeatable cap = stack.getCapability(CapabilityList.HEATABLE, null);
					CapabilityUtil.heatExchange(cap, 20, 50);
				}
				if (stack.hasCapability(CapabilityList.LIQUID_CONTAINER, null)) {
					ILiquidContainer liquid = stack.getCapability(CapabilityList.LIQUID_CONTAINER, null);
					for (IHeatable cap : liquid.getMaterial()) {
						CapabilityUtil.heatExchange(cap, 20, 100);
					}
				}
			}
		}
	}
}