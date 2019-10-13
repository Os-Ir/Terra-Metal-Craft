package com.osir.tmc.container;

import com.osir.tmc.te.TEInventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerTEInventory<T extends TEInventory> extends Container {
	protected T te;
	protected InventoryPlayer invPlayer;

	public T getTE() {
		return this.te;
	}

	public ContainerTEInventory(InventoryPlayer invPlayer, T te) {
		this(invPlayer, te, 0, 0);
	}

	public ContainerTEInventory(InventoryPlayer invPlayer, T te, int x, int y) {
		this.invPlayer = invPlayer;
		this.te = te;
		this.addSlot();
		this.addPlayerSlot(invPlayer, x, y);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	protected abstract void addSlot();

	protected void addPlayerSlot(InventoryPlayer inv, int x, int y) {
		int i, j;
		for (i = 0; i < 3; i++) {
			for (j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(inv, j + i * 9 + 9, x + j * 18, y + i * 18));
			}
		}
		for (i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(inv, i, x + i * 18, y + 58));
		}
	}

	protected void detectAndSendAllChanges() {
		int i;
		for (i = 0; i < this.inventorySlots.size(); i++) {
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