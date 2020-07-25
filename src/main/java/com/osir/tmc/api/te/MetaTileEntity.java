package com.osir.tmc.api.te;

import com.osir.tmc.api.block.MaterialNameMap;

import gregtech.api.metatileentity.SyncedTileEntityBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public class MetaTileEntity extends SyncedTileEntityBase implements ITickable {
	protected MetaValueTileEntity metaValue;

	public MetaTileEntity() {

	}

	public MetaValueTileEntity init(MetaValueTileEntity metaValue) {
		if (metaValue == null) {
			return null;
		}
		this.metaValue = metaValue;
		metaValue.holder = this;
		this.markDirty();
		return this.metaValue;
	}

	public MetaValueTileEntity getMetaValue() {
		return this.metaValue;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState != newSate;
	}

	@Override
	public void update() {
		if (this.metaValue != null) {
			this.metaValue.update();
		}
	}

	public void scheduleChunkForRenderUpdate() {
		BlockPos pos = getPos();
		this.world.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1,
				pos.getY() + 1, pos.getZ() + 1);
	}

	@Override
	public void writeInitialSyncData(PacketBuffer buf) {
		if (this.metaValue == null) {
			buf.writeBoolean(false);
		} else {
			buf.writeBoolean(true);
			buf.writeString(this.metaValue.getModid());
			buf.writeString(MaterialNameMap.getMaterialName(this.metaValue.getMaterial()));
			buf.writeString(this.metaValue.getName());
			this.metaValue.writeInitialSyncData(buf);
		}
	}

	@Override
	public void receiveInitialSyncData(PacketBuffer buf) {
		if (buf.readBoolean()) {
			this.init(MetaTileEntityRegistry.getMetaTileEntity(buf.readString(Short.MAX_VALUE),
					MaterialNameMap.getMaterial(buf.readString(Short.MAX_VALUE)), buf.readString(Short.MAX_VALUE)));
			this.metaValue.receiveInitialSyncData(buf);
		}
	}

	@Override
	public void receiveCustomData(int discriminator, PacketBuffer buf) {
		if (this.metaValue != null) {
			this.metaValue.receiveCustomData(discriminator, buf);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (this.metaValue != null) {
			nbt.setString("modid", this.metaValue.getModid());
			nbt.setString("material", MaterialNameMap.getMaterialName(this.metaValue.getMaterial()));
			nbt.setString("name", this.metaValue.getName());
			this.metaValue.writeToNBT(nbt);
		}
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("modid")) {
			this.init(MetaTileEntityRegistry.getMetaTileEntity(nbt.getString("modid"),
					MaterialNameMap.getMaterial(nbt.getString("material")), nbt.getString("name")));
			this.metaValue.readFromNBT(nbt);
		}
	}

	@Override
	public boolean hasCapability(Capability capability, EnumFacing facing) {
		if (this.metaValue != null) {
			return this.metaValue.hasCapability(capability, facing);
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (this.metaValue.hasCapability(capability, facing)) {
			return this.metaValue.getCapability(capability, facing);
		}
		return super.getCapability(capability, facing);
	}
}