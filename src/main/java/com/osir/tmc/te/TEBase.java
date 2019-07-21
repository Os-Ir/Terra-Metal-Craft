package com.osir.tmc.te;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TEBase extends TileEntity {
	protected ItemStackHandler inventory;

	public ItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		inventory.deserializeNBT(nbt.getCompoundTag("inventory"));
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("inventory", inventory.serializeNBT());
		return super.writeToNBT(nbt);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 1, getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(super.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity te) {
		readFromNBT(te.getNbtCompound());
	}

	@Override
	public void handleUpdateTag(NBTTagCompound nbt) {
		readFromNBT(nbt);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState stateOld, IBlockState stateNew) {
		return stateOld.getBlock() != stateNew.getBlock();
	}
}