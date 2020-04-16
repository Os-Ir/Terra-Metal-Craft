package com.osir.tmc.api.util;

import com.osir.tmc.api.capability.IHeatable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityUtil {
	public static <T> void registerEmptyCapability(Class<T> type) {
		CapabilityManager.INSTANCE.register(type, new Capability.IStorage<T>() {
			@Override
			public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
				throw new UnsupportedOperationException("This capability doesn't have default storage");
			}

			@Override
			public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
				throw new UnsupportedOperationException("This capability doesn't have default storage");
			}
		}, () -> {
			throw new UnsupportedOperationException("This capability doesn't have default instance");
		});
	}

	public static void heatExchange(IHeatable cap, int temp, float resistance) {
		if (cap == null) {
			return;
		}
		float exchange = (cap.getTemp() - temp) / resistance;
		cap.increaseEnergy(-exchange);
	}

	public static void heatExchange(IHeatable capA, IHeatable capB, float resistance) {
		if (capA == null || capB == null) {
			return;
		}
		float exchange = (capA.getTemp() - capB.getTemp()) / resistance;
		capA.increaseEnergy(-exchange);
		capB.increaseEnergy(exchange);
	}
}