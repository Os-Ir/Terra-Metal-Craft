package com.osir.tmc.api.container;

import java.util.HashMap;
import java.util.Map;

import com.osir.tmc.handler.NetworkHandler;
import com.osir.tmc.network.MessageCapabilityUpdate;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

public class ContainerListenerCapability implements IContainerListener {
	private static final Map<String, Capability> CAPABILITY = new HashMap<String, Capability>();

	private EntityPlayerMP player;

	public ContainerListenerCapability(EntityPlayerMP player) {
		this.player = player;
	}

	public static void register(String key, Capability cap) {
		CAPABILITY.put(key, cap);
	}

	public static NBTTagCompound readCapabilityData(ItemStack stack) {
		NBTTagCompound nbt = new NBTTagCompound();
		CAPABILITY.forEach((key, cap) -> {
			if (stack.hasCapability(cap, null)) {
				INBTSerializable ser = (INBTSerializable) stack.getCapability(cap, null);
				nbt.setTag(key, ser.serializeNBT());
			}
		});
		return nbt;
	}

	public static void applyCapabilityData(ItemStack stack, NBTTagCompound nbt) {
		CAPABILITY.forEach((key, cap) -> {
			if (stack.hasCapability(cap, null)) {
				INBTSerializable ser = (INBTSerializable) stack.getCapability(cap, null);
				ser.deserializeNBT(nbt.getTag(key));
			}
		});
	}

	public static boolean shouldSync(ItemStack stack) {
		for (Capability cap : CAPABILITY.values()) {
			if (stack.hasCapability(cap, null)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void sendAllContents(Container container, NonNullList<ItemStack> items) {
		NonNullList<ItemStack> sync = NonNullList.withSize(items.size(), ItemStack.EMPTY);
		for (int i = 0; i < items.size(); i++) {
			ItemStack stack = items.get(i);
			if (shouldSync(stack)) {
				sync.set(i, stack);
			} else {
				sync.set(i, ItemStack.EMPTY);
			}
		}
		MessageCapabilityUpdate msg = new MessageCapabilityUpdate(container.windowId, sync);
		if (msg.hasData()) {
			NetworkHandler.NETWORK.sendTo(msg, player);
		}
	}

	@Override
	public void sendSlotContents(Container container, int slot, ItemStack stack) {
		if (shouldSync(stack)) {
			MessageCapabilityUpdate msg = new MessageCapabilityUpdate(container.windowId, slot, stack);
			if (msg.hasData()) {
				NetworkHandler.NETWORK.sendTo(msg, player);
			}
		}
	}

	@Override
	public void sendWindowProperty(Container container, int varToUpdate, int newValue) {

	}

	@Override
	public void sendAllWindowProperties(Container container, IInventory inventory) {

	}
}