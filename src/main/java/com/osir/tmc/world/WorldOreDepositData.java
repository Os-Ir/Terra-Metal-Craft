package com.osir.tmc.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

public class WorldOreDepositData extends WorldSavedData {
	public static final String DATAID = "tmc.ore_deposit";

	private Random rand;
	private Map<ChunkPos, OreDepositInfo> oreByChunk;
	private World world;

	public WorldOreDepositData(String name) {
		super(name);
		this.oreByChunk = new HashMap<ChunkPos, OreDepositInfo>();
	}

	public void init(World world) {
		this.world = world;
		this.rand = new Random(world.getSeed());
	}

	public World getWorld() {
		return this.world;
	}

	public static WorldOreDepositData getData(World world) {
		WorldOreDepositData data = (WorldOreDepositData) world.loadData(WorldOreDepositData.class, DATAID);
		if (data == null) {
			data = new WorldOreDepositData(DATAID);
			world.setData(DATAID, data);
		}
		data.init(world);
		return data;
	}

	public OreDepositInfo getOreDepositInfo(int x, int z) {
		return this.getOreDepositInfo(new ChunkPos(x, z));
	}

	public OreDepositInfo getOreDepositInfo(BlockPos pos) {
		return this.getOreDepositInfo(new ChunkPos(pos));
	}

	public OreDepositInfo getOreDepositInfo(ChunkPos pos) {
		OreDepositInfo deposit = this.oreByChunk.get(pos);
		if (deposit == null) {
			deposit = OreDepositInfo.REGISTRY.choose();
			this.oreByChunk.put(pos, deposit);
			this.markDirty();
		}
		return deposit;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.oreByChunk = new HashMap<ChunkPos, OreDepositInfo>();
		NBTTagList list = nbt.getTagList("deposit", NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound info = (NBTTagCompound) list.getCompoundTagAt(i);
			ChunkPos pos = new ChunkPos(info.getInteger("x"), info.getInteger("z"));
			OreDepositInfo ore = OreDepositInfo.REGISTRY.getObjectById(info.getInteger("id"));
			this.oreByChunk.put(pos, ore);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for (Entry<ChunkPos, OreDepositInfo> entry : this.oreByChunk.entrySet()) {
			NBTTagCompound info = new NBTTagCompound();
			ChunkPos pos = entry.getKey();
			OreDepositInfo ore = entry.getValue();
			info.setInteger("x", pos.x);
			info.setInteger("z", pos.z);
			info.setInteger("id", OreDepositInfo.REGISTRY.getIdForObject(ore));
			list.appendTag(info);
		}
		nbt.setTag("deposit", list);
		return nbt;
	}
}