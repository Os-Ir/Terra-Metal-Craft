package com.osir.tmc.api.container;

import java.util.function.Predicate;

import gregtech.api.gui.ModularUI;
import gregtech.api.gui.impl.ModularUIContainer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;

public class CapabilitySyncedModularUIContainer extends ModularUIContainer {
	public CapabilitySyncedModularUIContainer(ModularUI modularUI) {
		super(modularUI);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < this.inventorySlots.size(); i++) {
			ItemStack stack = this.inventorySlots.get(i).getStack();
			if (ContainerListenerCapability.shouldSync(stack)) {
				for (IContainerListener listener : this.listeners) {
					if (listener instanceof ContainerListenerCapability) {
						listener.sendSlotContents(this, i, stack);
					}
				}
			}
		}
	}
}