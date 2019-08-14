package com.osir.tmc.capability;

import com.osir.tmc.Main;
import com.osir.tmc.api.heat.HeatMaterial;
import com.osir.tmc.api.heat.MaterialList;
import com.osir.tmc.api.heat.TempList;
import com.osir.tmc.api.inter.IHeatable;
import com.osir.tmc.handler.CapabilityHandler;

import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityHeat {
	public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, "heatable_item");

	public static class Storage implements IStorage<IHeatable> {
		@Override
		public NBTBase writeNBT(Capability capability, IHeatable instance, EnumFacing side) {
			NBTTagCompound nbt = new NBTTagCompound();
			boolean flag = false;
			if (instance.getUnit() != instance.getCompleteUnit()) {
				nbt.setInteger("unit", instance.getUnit());
				flag = true;
			}
			if (instance.getEnergy() != 0) {
				nbt.setFloat("energy", instance.getEnergy());
				flag = true;
			}
			if (instance.getOverEnergy() != 0) {
				nbt.setFloat("overEnergy", instance.getOverEnergy());
				flag = true;
			}
			if (flag) {
				return nbt;
			}
			return null;
		}

		@Override
		public void readNBT(Capability capability, IHeatable instance, EnumFacing side, NBTBase base) {
			NBTTagCompound nbt = (NBTTagCompound) base;
			if (nbt == null) {
				return;
			}
			if (nbt.hasKey("unit")) {
				instance.setUnit(nbt.getInteger("unit"));
			}
			float energy = 0;
			if (nbt.hasKey("energy")) {
				energy += nbt.getFloat("energy");
			}
			if (nbt.hasKey("overEnergy")) {
				energy += nbt.getFloat("overEnergy");
			}
			instance.setEnergy(energy);
		}
	}

	public static class Implementation implements IHeatable, ICapabilitySerializable<NBTTagCompound> {
		private static final String[] NUMBER = { "I", "II", "III", "IV", "V" };
		private int specificHeat, meltTemp, unit, compUnit;
		private float maxEnergy, energy, overEnergy;

		public Implementation() {
			this(MaterialList.IRON, 144);
		}

		public Implementation(HeatMaterial material, int unit) {
			this.compUnit = this.unit = unit;
			this.specificHeat = material.getSpecificHeat();
			this.meltTemp = material.getMeltTemp();
			this.maxEnergy = this.specificHeat * (this.meltTemp - 20) * (unit / 144);
		}

		@Override
		public int getMeltTemp() {
			return this.meltTemp;
		}

		@Override
		public int getSpecificHeat() {
			return this.specificHeat;
		}

		@Override
		public int getTemp() {
			return (int) ((this.energy / this.maxEnergy) * (this.meltTemp - 20) + 20);
		}

		@Override
		public String getColor() {
			if (!this.hasEnergy()) {
				return null;
			}
			String str = "";
			float temp = this.getTemp();
			TempList[] list = TempList.values();
			if (temp < list[list.length - 1].getTemp()) {
				int i;
				for (i = 0; i < list.length - 1; i++) {
					if (temp >= list[i + 1].getTemp()) {
						continue;
					}
					str = list[i].getColor() + I18n.format("item.heatable." + list[i].name()) + " "
							+ NUMBER[(int) ((temp - list[i].getTemp()) / (list[i + 1].getTemp() - list[i].getTemp())
									* 5)];
					break;
				}
			} else {
				str = list[list.length - 1].getColor() + I18n.format("item.heatable." + list[list.length - 1].name());
			}
			return str;
		}

		@Override
		public int getUnit() {
			return this.unit;
		}

		@Override
		public int getCompleteUnit() {
			return this.compUnit;
		}

		@Override
		public void setUnit(int unit) {
			this.unit = unit;
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
		public float getOverEnergy() {
			return this.overEnergy;
		}

		@Override
		public float getMaxEnergy() {
			return this.maxEnergy;
		}

		@Override
		public void setEnergy(float energy) {
			// if (energy <= this.maxEnergy) {
			// this.energy = energy;
			// return 0;
			// } else {
			// float over = energy - this.maxEnergy;
			// float delta = this.maxEnergy / this.unit / 10;
			// int melt = Math.min((int) (over / delta), this.unit);
			// this.unit -= melt;
			// this.energy = this.maxEnergy = this.specificHeat * (this.meltTemp - 20) *
			// (this.unit / 144);
			// this.overEnergy = over - melt * delta;
			// return melt;
			// }
			this.energy = Math.max(Math.min(energy, this.maxEnergy), 0);
		}

		@Override
		public void setIncreaseEnergy(float energy) {
			// this.setEnergy(energy + this.energy + this.overEnergy);
			if (energy + this.energy > this.maxEnergy) {
				this.overEnergy += energy + this.energy - this.maxEnergy;
				this.energy = this.maxEnergy;
				this.unit = Math.max((int) ((1 - this.overEnergy / this.maxEnergy * 10) * this.compUnit), 0);
			} else {
				this.energy += energy;
			}
			this.energy = Math.max(this.energy, 0);
		}

		@Override
		public boolean hasCapability(Capability capability, EnumFacing facing) {
			return capability == CapabilityHandler.heatable;
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
			if (this.unit != this.compUnit) {
				nbt.setInteger("unit", this.unit);
			}
			if (this.energy != 0) {
				nbt.setFloat("energy", this.energy);
			}
			if (this.overEnergy != 0) {
				nbt.setFloat("overEnergy", this.overEnergy);
			}
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			if (nbt == null) {
				return;
			}
			if (nbt.hasKey("unit")) {
				this.unit = nbt.getInteger("unit");
			}
			if (nbt.hasKey("energy")) {
				this.energy = nbt.getFloat("energy");
			}
			if (nbt.hasKey("overEnergy")) {
				this.overEnergy = nbt.getFloat("overEnergy");
			}
		}
	}
}