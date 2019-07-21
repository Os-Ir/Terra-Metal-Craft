package com.osir.tmc.te;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TEHeatBlock extends TEBase implements ITickable {
	protected float temp = 20, energy;
	protected int burnTime;

	public int getTemp() {
		return (int) this.temp;
	}

	public int getBurnTime() {
		return this.burnTime;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.energy = nbt.getFloat("energy");
		this.burnTime = nbt.getInteger("burnTime");
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setFloat("energy", this.energy);
		nbt.setFloat("burnTime", this.burnTime);
		return super.writeToNBT(nbt);
	}
}