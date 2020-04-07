package com.osir.tmc.te;

import java.util.ArrayList;
import java.util.Arrays;

import com.osir.tmc.api.capability.CapabilityList;
import com.osir.tmc.api.capability.IHeatable;
import com.osir.tmc.api.capability.ILiquidContainer;
import com.osir.tmc.api.recipe.ModRecipeMap;
import com.osir.tmc.block.BlockBasin;
import com.osir.tmc.handler.BlockHandler;

import gregtech.api.metatileentity.SyncedTileEntityBase;
import gregtech.api.recipes.Recipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TEBasin extends SyncedTileEntityBase {
	public static final int CAPACITY = 1000;

	protected boolean empty;
	protected int amount;

	public TEBasin() {
		this.amount = 0;
		this.empty = true;
	}

	public ItemStack applyRecipe(ItemStack stack) {
		if (this.amount < 200) {
			return ItemStack.EMPTY;
		}
		if (stack.hasCapability(CapabilityList.HEATABLE, null)) {
			IHeatable cap = stack.getCapability(CapabilityList.HEATABLE, null);
			if (cap.getEnergy() != 0) {
				cap.setEnergy(0);
				this.consumeWater();
			}
			return ItemStack.EMPTY;
		}
		if (stack.hasCapability(CapabilityList.LIQUID_CONTAINER, null)) {
			ILiquidContainer cap = stack.getCapability(CapabilityList.LIQUID_CONTAINER, null);
			boolean consume = false;
			for (IHeatable heat : cap.getMaterial()) {
				if (heat.getEnergy() != 0) {
					heat.setEnergy(0);
					consume = true;
				}
			}
			if (consume) {
				this.consumeWater();
			}
			return ItemStack.EMPTY;
		}
		Recipe recipe = ModRecipeMap.MAP_CLEAN.findRecipe(1, Arrays.asList(stack), new ArrayList<FluidStack>(), 0);
		if (recipe == null) {
			return ItemStack.EMPTY;
		}
		this.consumeWater();
		recipe.matches(true, Arrays.asList(stack), new ArrayList<FluidStack>());
		return recipe.getOutputs().get(0).copy();
	}

	public boolean isEmpty() {
		return this.empty;
	}

	public void consumeWater() {
		this.amount -= 200;
		if (this.amount == 0) {
			this.empty = true;
			this.updateBlockState();
			this.writeCustomData(100, (buf) -> buf.writeBoolean(true));
		}
	}

	public boolean fillByBucket() {
		if (this.empty) {
			this.empty = false;
			this.writeCustomData(100, (buf) -> buf.writeBoolean(false));
			this.updateBlockState();
		}
		if (this.amount <= CAPACITY - 1000) {
			this.amount += 1000;
			return true;
		}
		return false;
	}

	public boolean fill(IFluidHandler fluid) {
		FluidStack stack = fluid.drain(CAPACITY - this.amount, true);
		if (stack != null && stack.amount != 0) {
			if (this.empty) {
				this.empty = false;
				this.writeCustomData(100, (buf) -> buf.writeBoolean(false));
				this.updateBlockState();
			}
			this.amount += stack.amount;
			return true;
		}
		return false;
	}

	public int getAmount() {
		return this.amount;
	}

	public void updateBlockState() {
		IBlockState state = this.getBlockState();
		TileEntity te = this.world.getTileEntity(this.pos);
		this.world.setBlockState(this.pos,
				BlockHandler.BASIN.getDefaultState().withProperty(BlockBasin.WATER, !this.empty));
		if (te != null) {
			te.validate();
			this.world.setTileEntity(this.pos, te);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.amount = nbt.getInteger("amount");
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("amount", this.amount);
		return super.writeToNBT(nbt);
	}

	@Override
	public void writeInitialSyncData(PacketBuffer buf) {
		buf.writeBoolean(this.empty);
	}

	@Override
	public void receiveInitialSyncData(PacketBuffer buf) {
		this.empty = buf.readBoolean();
	}

	@Override
	public void receiveCustomData(int discriminator, PacketBuffer buf) {
		if (discriminator == 100) {
			this.empty = buf.readBoolean();
			this.scheduleChunkForRenderUpdate();
		}
	}

	protected void scheduleChunkForRenderUpdate() {
		BlockPos pos = this.getPos();
		this.getWorld().markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1,
				pos.getY() + 1, pos.getZ() + 1);
	}
}