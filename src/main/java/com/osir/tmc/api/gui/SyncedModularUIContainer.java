package com.osir.tmc.api.gui;

import java.util.function.Predicate;

import gregtech.api.gui.ModularUI;
import gregtech.api.gui.impl.ModularUIContainer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;

public class SyncedModularUIContainer extends ModularUIContainer {
	protected Predicate<Integer> pre;

	public SyncedModularUIContainer(ModularUI modularUI, Predicate<Integer> pre) {
		super(modularUI);
		this.pre = pre;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < this.inventorySlots.size(); i++) {
			if (!this.pre.test(i)) {
				continue;
			}
			ItemStack stackNew = this.inventorySlots.get(i).getStack();
			ItemStack stackOld = this.inventoryItemStacks.get(i);
			if (ItemStack.areItemsEqual(stackNew, stackOld)) {
				stackOld = stackNew.isEmpty() ? ItemStack.EMPTY : stackNew.copy();
				this.inventoryItemStacks.set(i, stackOld);
				if (stackNew.areCapsCompatible(stackOld)) {
					for (IContainerListener listener : this.listeners) {
						listener.sendSlotContents(this, i, stackOld);
					}
				}
			}
		}
	}
}