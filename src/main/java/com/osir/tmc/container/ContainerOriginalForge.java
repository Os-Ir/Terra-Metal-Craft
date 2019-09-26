package com.osir.tmc.container;

import com.osir.tmc.container.slot.SlotFuel;
import com.osir.tmc.container.slot.SlotHeat;
import com.osir.tmc.te.TEOriginalForge;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerOriginalForge extends ContainerTEInventory<TEOriginalForge> {
	private int temp, burnTime;

	public ContainerOriginalForge(TEOriginalForge te, EntityPlayer player) {
		super(player.inventory, te, 8, 84);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		this.detectAndSendAllChanges();
		this.temp = this.te.getTemp();
		this.burnTime = this.te.getBurnTime();
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
			return ItemStack.EMPTY;
		}
		ItemStack stack = slot.getStack();
		ItemStack previous = stack.copy();
		int i;
		if (idx < 9) {
			if (!this.mergeItemStack(stack, 9, this.inventorySlots.size(), true)) {
				return ItemStack.EMPTY;
			}
		} else {
			if (stack != null && !stack.isEmpty()) {
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
				if (!stack.isEmpty()) {
					if (!this.mergeItemStack(stack, 6, 9, true)) {
						return ItemStack.EMPTY;
					}
				}
			}
		}
		if (stack.getCount() == 0) {
			slot.putStack(ItemStack.EMPTY);
		} else {
			slot.onSlotChanged();
		}
		if (stack.getCount() == previous.getCount()) {
			return ItemStack.EMPTY;
		}
		slot.onTake(player, stack);
		return previous;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
	protected void addSlot() {
		IItemHandler cap = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		int i, j;
		for (i = 0; i < 3; i++) {
			this.addSlotToContainer(new SlotFuel(cap, i, 8, 21 + i * 21));
		}
		for (i = 0; i < 3; i++) {
			this.addSlotToContainer(new SlotHeat(cap, i + 3, 50 + i * 34, 21));
		}
		for (i = 0; i < 3; i++) {
			this.addSlotToContainer(new SlotItemHandler(cap, i + 6, 152, 21 + i * 21));
		}
	}
}