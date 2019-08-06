package com.osir.tmc.container;

import com.osir.tmc.handler.CapabilityHandler;
import com.osir.tmc.handler.NetworkHandler;
import com.osir.tmc.network.MessageHeatableItem;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class CapabilityContainerListener implements IContainerListener {
	private final EntityPlayerMP player;

	public CapabilityContainerListener(EntityPlayerMP player) {
		this.player = player;
	}

	@Override
	public void sendAllContents(Container container, NonNullList<ItemStack> items) {
		NonNullList<ItemStack> list = NonNullList.withSize(items.size(), ItemStack.EMPTY);
		int i;
		for (i = 0; i < list.size(); i++) {
			ItemStack stack = items.get(i);
			if (this.shouldSync(stack)) {
				list.set(i, stack);
			} else {
				list.set(i, ItemStack.EMPTY);
			}
		}
		System.out.println("all packing");
		MessageHeatableItem message = new MessageHeatableItem(container.windowId, list);
		if (message.hasData()) {
			// NetworkHandler.instance.sendTo(message, player);
		}
	}

	@Override
	public void sendSlotContents(Container container, int slot, ItemStack stack) {
		if (this.shouldSync(stack)) {
			System.out.println("packing");
			MessageHeatableItem message = new MessageHeatableItem(container.windowId, slot, stack);
			if (message.hasData()) {
				// NetworkHandler.instance.sendTo(message, player);
			}
		}
	}

	@Override
	public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {

	}

	@Override
	public void sendAllWindowProperties(Container containerIn, IInventory inventory) {

	}

	private boolean shouldSync(ItemStack stack) {
		return stack.hasCapability(CapabilityHandler.heatable, null);
	}
}