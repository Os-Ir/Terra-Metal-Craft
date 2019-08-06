package com.osir.tmc.te;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TEInventory extends TEBase {
	protected ItemStackHandler inventory;

	public ItemStackHandler getInventory() {
		return this.inventory;
	}

	public int getSlotLimit(int slot) {
		return 64;
	}

	public boolean isItemValid(ItemStack stack, int slot) {
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.inventory.deserializeNBT(nbt.getCompoundTag("inventory"));
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("inventory", this.inventory.serializeNBT());
		return super.writeToNBT(nbt);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = this.getUpdateTag();
		return new SPacketUpdateTileEntity(this.pos, 1, nbt);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(super.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity te) {
		this.readFromNBT(te.getNbtCompound());
	}

	@Override
	public void handleUpdateTag(NBTTagCompound nbt) {
		this.readFromNBT(nbt);
	}

	@Override
	public boolean hasCapability(Capability capability, EnumFacing facing) {
		return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == null)
				|| super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == null) ? (T) this.inventory
				: super.getCapability(capability, facing);
	}

	@Override
	public void onBlockBreak() {
		int i;
		for (i = 0; i < this.inventory.getSlots(); i++) {
			InventoryHelper.spawnItemStack(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(),
					this.inventory.getStackInSlot(i));
		}
	}
}