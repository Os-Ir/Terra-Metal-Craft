package com.osir.tmc.container;

import com.osir.tmc.container.slot.SlotLocked;
import com.osir.tmc.te.TEAnvil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerAnvil extends ContainerTEInventory<TEAnvil> {
	public ContainerAnvil(TEAnvil te, EntityPlayer player) {
		super(player.inventory, te, 24, 118);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		this.detectAndSendAllChanges();
	}

	@Override
	protected void addSlot() {
		IItemHandler cap = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
	}
}