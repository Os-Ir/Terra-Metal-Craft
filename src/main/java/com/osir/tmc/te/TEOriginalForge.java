package com.osir.tmc.te;

import com.osir.tmc.api.heat.HeatRecipe;
import com.osir.tmc.api.heat.HeatRegistry;
import com.osir.tmc.api.inter.IHeatable;
import com.osir.tmc.block.BlockOriginalForge;
import com.osir.tmc.handler.BlockHandler;
import com.osir.tmc.handler.CapabilityHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public class TEOriginalForge extends TEHeatBlock {
	public static final float RATE = 50;
	public static final int MAX_TEMP = 900;
	public static final int SPECIFIC_HEAT = 46000;
	protected int burnTime;

	public TEOriginalForge() {
		this.inventory = new ItemStackHandler(9) {
			@Override
			public int getSlotLimit(int slot) {
				if (slot < 3) {
					return 1;
				}
				if (slot < 6) {
					return 4;
				}
				return 64;
			}
		};
	}

	@Override
	public void update() {
		if (this.world.isRemote) {
			return;
		}
		int i;
		if (!this.inventory.getStackInSlot(0).isEmpty() && this.inventory.getStackInSlot(1).isEmpty()) {
			this.inventory.setStackInSlot(1, this.inventory.getStackInSlot(0));
			this.inventory.setStackInSlot(0, ItemStack.EMPTY);
		}
		if (!this.inventory.getStackInSlot(1).isEmpty() && this.inventory.getStackInSlot(2).isEmpty()) {
			this.inventory.setStackInSlot(2, this.inventory.getStackInSlot(1));
			this.inventory.setStackInSlot(1, ItemStack.EMPTY);
		}
		if (this.burnTime <= 0 && !this.inventory.getStackInSlot(2).isEmpty()) {
			this.inventory.setStackInSlot(2, ItemStack.EMPTY);
			this.burnTime = 1600;
		}
		IBlockState state = this.world.getBlockState(this.pos);
		TileEntity te = this.world.getTileEntity(this.pos);
		if (this.burnTime > 0) {
			this.energy += 20000;
			this.burnTime--;
			this.world.setBlockState(this.pos,
					BlockHandler.ORIGINAL_FORGE.getDefaultState()
							.withProperty(BlockOriginalForge.FACING, state.getValue(BlockOriginalForge.FACING))
							.withProperty(BlockOriginalForge.BURN, true));
		} else {
			this.world.setBlockState(this.pos,
					BlockHandler.ORIGINAL_FORGE.getDefaultState()
							.withProperty(BlockOriginalForge.FACING, state.getValue(BlockOriginalForge.FACING))
							.withProperty(BlockOriginalForge.BURN, false));
		}
		if (te != null) {
			te.validate();
			this.world.setTileEntity(this.pos, te);
		}
		this.energy -= Math.max((this.temp - 20) * 5F, 50);
		this.energy = Math.min(Math.max(this.energy, 0), SPECIFIC_HEAT * (MAX_TEMP - 20));
		this.temp = (int) (this.energy / SPECIFIC_HEAT) + 20;
		for (i = 3; i < 6; i++) {
			ItemStack stack = this.inventory.getStackInSlot(i);
			IHeatable cap = stack.getCapability(CapabilityHandler.HEATABLE, null);
			if (cap == null) {
				continue;
			}
			float delta = (this.temp - cap.getTemp()) * RATE;
			if (delta > 0) {
				delta = Math.max(delta, RATE);
			} else if (delta < 0) {
				delta = Math.min(delta, -RATE);
			}
			int count = stack.getCount();
			cap.setIncreaseEnergy(delta);
			this.energy -= delta * count;
			if (cap.getUnit() == 0) {
				HeatRecipe recipe = HeatRegistry.findRecipe(stack);
				if (recipe != null) {
					ItemStack output = recipe.getOutput();
					if (output != null && !output.isEmpty()) {
						output = output.copy();
						output.setCount(count);
						this.inventory.setStackInSlot(i, output);
					} else {
						this.inventory.setStackInSlot(i, ItemStack.EMPTY);
					}
				}
			}
		}
		if (this.world != null) {
			this.world.markChunkDirty(this.pos, this);
		}
	}

	public int getBurnTime() {
		return this.burnTime;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.burnTime = nbt.getInteger("burnTime");
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setFloat("burnTime", this.burnTime);
		return super.writeToNBT(nbt);
	}
}