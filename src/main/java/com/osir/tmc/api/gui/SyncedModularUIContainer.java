package com.osir.tmc.api.gui;

import gregtech.api.gui.ModularUI;
import gregtech.api.gui.Widget;
import gregtech.api.gui.impl.ModularUIContainer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;

public class SyncedModularUIContainer extends ModularUIContainer {
	public SyncedModularUIContainer(ModularUI modularUI) {
		super(modularUI);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < this.inventorySlots.size(); i++) {
			ItemStack stackNew = inventorySlots.get(i).getStack();
			ItemStack stackOld = inventoryItemStacks.get(i);
			if (ItemStack.areItemsEqual(stackNew, stackOld)) {
				stackOld = stackNew.isEmpty() ? ItemStack.EMPTY : stackNew.copy();
				inventoryItemStacks.set(i, stackOld);
				for (IContainerListener listener : this.listeners) {
					listener.sendSlotContents(this, i, stackOld);
				}
			}
		}
	}
}