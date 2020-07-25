package com.osir.tmc.api.capability;

import com.osir.tmc.Main;
import com.osir.tmc.api.heat.HeatMaterial;
import com.osir.tmc.api.heat.HeatMaterialList;
import com.osir.tmc.api.heat.MaterialStack;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityHeat implements IHeatable, ICapabilitySerializable<NBTTagCompound> {
	public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, "heatable_item");

	protected MaterialStack material;
	protected int meltTemp;
	protected float energy;

	public CapabilityHeat() {
		this(HeatMaterialList.EMPTY, 1);
	}

	public CapabilityHeat(HeatMaterial material, int unit) {
		this(material, unit, material.getMeltTemp());
	}

	public CapabilityHeat(HeatMaterial material, int unit, int meltTemp) {
		this(new MaterialStack(material, unit), meltTemp);
	}

	public CapabilityHeat(MaterialStack stack) {
		this(stack, stack.getMaterial().getMeltTemp());
	}

	public CapabilityHeat(MaterialStack stack, int meltTemp) {
		this.material = stack;
		this.meltTemp = meltTemp;
	}

	protected float getMeltEnergy() {
		return (this.meltTemp - 20) * this.getSpecificHeat() * this.getUnit();
	}

	protected float getSpecificHeat() {
		return this.material.getMaterial().getSpecificHeat();
	}

	@Override
	public HeatMaterial getMaterial() {
		return this.material.getMaterial();
	}

	@Override
	public void setMeltTemp(int temp) {
		this.meltTemp = temp;
	}

	@Override
	public int getTemp() {
		float meltEnergy = this.getMeltEnergy();
		if (this.energy < meltEnergy) {
			return (int) (this.energy / this.getSpecificHeat() / this.getUnit() + 20);
		} else if (this.energy < meltEnergy * 1.1) {
			return this.meltTemp;
		} else {
			return (int) ((this.energy - meltEnergy * 1.1) / this.getSpecificHeat() / this.getUnit() + this.meltTemp);
		}
	}

	@Override
	public int getUnit() {
		return this.material.getAmount();
	}

	@Override
	public void setUnit(int unit, boolean lockTemp) {
		if (lockTemp) {
			this.energy *= ((float) unit) / this.getUnit();
		}
		this.material.setAmount(unit);
	}

	@Override
	public void increaseUnit(int unit, boolean lockTemp) {
		this.setUnit(this.getUnit() + unit, lockTemp);
	}

	@Override
	public int getMeltTemp() {
		return this.meltTemp;
	}

	@Override
	public float getMeltProgress() {
		float meltEnergy = this.getMeltEnergy();
		return MathHelper.clamp((this.energy - meltEnergy) / meltEnergy * 10, 0, 1);
	}

	@Override
	public boolean hasEnergy() {
		return this.energy != 0;
	}

	@Override
	public float getEnergy() {
		return this.energy;
	}

	@Override
	public void setEnergy(float energy) {
		this.energy = Math.max(energy, 0);
	}

	@Override
	public void increaseEnergy(float energy) {
		this.setEnergy(this.energy + energy);
	}

	@Override
	public boolean isMelt() {
		return this.energy > this.getMeltEnergy();
	}

	@Override
	public boolean hasCapability(Capability capability, EnumFacing facing) {
		return capability == ModCapabilities.HEATABLE;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (this.hasCapability(capability, facing)) {
			return (T) this;
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		if (this.energy > 0) {
			nbt.setFloat("energy", this.energy);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("energy")) {
			this.energy = nbt.getFloat("energy");
		}
	}
}