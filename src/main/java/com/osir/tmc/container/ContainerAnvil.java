package com.osir.tmc.container;

import com.osir.tmc.container.slot.SlotLocked;
import com.osir.tmc.container.slot.SlotOutput;
import com.osir.tmc.te.TEAnvil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerAnvil extends ContainerTEInventory {
	public ContainerAnvil(TEAnvil te, EntityPlayer player) {
		super(player.inventory, te, 24, 118);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		this.detectAndSendAllChanges();
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
		if (idx < 10) {
			if (!this.mergeItemStack(stack, 12, this.inventorySlots.size(), true)) {
				return ItemStack.EMPTY;
			}
		} else if (idx > 11) {
			if (stack != null && !stack.isEmpty()) {
				if (!stack.isEmpty()) {
					if (!this.mergeItemStack(stack, 0, 4, true)) {
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
	protected void addSlot() {
		IItemHandler cap = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		int i;
		for (i = 0; i < 4; i++) {
			this.addSlotToContainer(new SlotItemHandler(cap, i, 8 + i * 22, 8));
		}
		for (i = 0; i < 4; i++) {
			this.addSlotToContainer(new SlotOutput(cap, i + 4, 8 + i * 22, 52));
		}
		this.addSlotToContainer(new SlotItemHandler(cap, 8, 8, 81));
		this.addSlotToContainer(new SlotItemHandler(cap, 9, 30, 81));
		this.addSlotToContainer(new SlotLocked(cap, 1, 124, 45));
		this.addSlotToContainer(new SlotLocked(cap, 2, 146, 45));
	}

	public void receiveMessage(int idx) {
		IItemHandlerModifiable cap = (IItemHandlerModifiable) this.te
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
	}
}