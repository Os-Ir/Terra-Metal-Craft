package com.osir.tmc.container;

import com.osir.tmc.te.TEOriginalForge;

import api.osir.tmc.heat.HeatRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerOriginalForge extends Container {
	private TEOriginalForge te;
	private ItemStackHandler items;
	private int temp, burnTime;

	public ContainerOriginalForge(TEOriginalForge te, EntityPlayer player) {
		this.te = te;
		items = te.getInventory();
		int i, j;
		for (i = 0; i < 3; i++) {
			this.addSlotToContainer(new SlotFuel(items, i, 8, 21 + i * 21));
		}
		for (i = 0; i < 3; i++) {
			this.addSlotToContainer(new SlotHeat(items, i + 3, 50 + i * 34, 21));
		}
		for (i = 0; i < 3; i++) {
			this.addSlotToContainer(new SlotItemHandler(items, i + 6, 152, 21 + i * 21));
		}
		for (i = 0; i < 3; i++) {
			for (j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		this.temp = te.getTemp();
		this.burnTime = te.getBurnTime();
		for (IContainerListener listener : listeners) {
			listener.sendWindowProperty(this, 0, this.temp);
			listener.sendWindowProperty(this, 1, this.burnTime);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) {
		switch (id) {
		case 0:
			this.temp = data;
			break;
		case 1:
			this.burnTime = data;
			break;
		}
	}

	public int getTemp() {
		return this.temp;
	}

	public float getBurnTime() {
		return this.burnTime;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int idx) {
		Slot slot = (Slot) this.inventorySlots.get(idx);
		if (slot == null || !slot.getHasStack()) {
			return null;
		}
		ItemStack stack = slot.getStack();
		ItemStack previous = stack.copy();
		int i;
		if (idx < 9) {
			if (!this.mergeItemStack(stack, 9, this.inventorySlots.size(), true)) {
				return null;
			}
		} else {
			if (stack.getItem() == Items.COAL) {
				Slot[] slotFuel = { (Slot) inventorySlots.get(0), (Slot) inventorySlots.get(1),
						(Slot) inventorySlots.get(2) };
				for (i = 0; i < 3; i++) {
					if (slotFuel[i].getHasStack()) {
						continue;
					} else {
						ItemStack fuel = stack.copy();
						fuel.setCount(1);
						slotFuel[i].putStack(fuel);
						stack.setCount(stack.getCount() - 1);
					}
				}
			}
			if (stack.getCount() != 0) {
				if (!this.mergeItemStack(stack, 6, 9, true)) {
					return null;
				}
			}
		}
		if (stack.getCount() == 0) {
			slot.putStack(ItemStack.EMPTY);
		} else {
			slot.onSlotChanged();
		}
		if (stack.getCount() == previous.getCount()) {
			return null;
		}
		slot.onTake(player, stack);
		return previous;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
}