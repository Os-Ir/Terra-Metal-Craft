package com.osir.tmc.api.te;

import com.osir.tmc.api.block.MetaBlock;

import gregtech.api.metatileentity.SyncedTileEntityBase;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

public class MetaTileEntity extends SyncedTileEntityBase implements ITickable {
	protected MetaValueTileEntity metaValue;

	public MetaTileEntity() {

	}

	public void init(MetaValueTileEntity metaValue, MetaBlock block) {
		this.metaValue = metaValue;
		metaValue.holder = this;
		metaValue.material = block.getMaterial(null);
		metaValue.modid = block.getModid();
	}

	public MetaValueTileEntity getMetaValue() {
		return this.metaValue;
	}

	@Override
	public void update() {
		if (this.metaValue != null) {
			this.metaValue.update();
		}
	}

	@Override
	public void writeInitialSyncData(PacketBuffer buf) {

	}

	@Override
	public void receiveInitialSyncData(PacketBuffer buf) {

	}

	@Override
	public void receiveCustomData(int discriminator, PacketBuffer buf) {

	}

	@Override
	public boolean hasCapability(Capability capability, EnumFacing facing) {
		if (this.metaValue != null) {
			return this.metaValue.hasCapability(capability, facing);
		}
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (this.metaValue != null) {
			return this.metaValue.getCapability(capability, facing);
		}
		return null;
	}
}