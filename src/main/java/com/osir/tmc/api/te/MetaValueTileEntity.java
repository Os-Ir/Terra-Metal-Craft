package com.osir.tmc.api.te;

import java.util.ArrayList;
import java.util.List;

import com.osir.tmc.CreativeTabList;
import com.osir.tmc.api.render.IRenderPipeline;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class MetaValueTileEntity {
	protected MetaTileEntity holder;
	protected String modid, name;
	protected Material material;

	public MetaValueTileEntity(String modid, String name, Material material) {
		this.modid = modid;
		this.name = name;
		this.material = material;
	}

	public abstract MetaValueTileEntity create();

	public MetaTileEntity getHolder() {
		return this.holder;
	}

	public String getModid() {
		return this.modid;
	}

	public String getName() {
		return this.name;
	}

	public Material getMaterial() {
		return this.material;
	}

	public void update() {

	}

	public boolean supportCreativeTab(CreativeTabs tab) {
		return tab == CreativeTabList.tabEquipment;
	}

	public void markDirty() {
		this.holder.markDirty();
	}

	public boolean isInvalid() {
		return this.holder == null || this.holder.isInvalid();
	}

	public World getWorld() {
		return this.holder == null ? null : this.holder.getWorld();
	}

	public BlockPos getPos() {
		return this.holder == null ? null : this.holder.getPos();
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	}

	@SideOnly(Side.CLIENT)
	public boolean canRender(BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT_MIPPED;
	}

	@SideOnly(Side.CLIENT)
	public List<IRenderPipeline> getRenderPipeline() {
		return new ArrayList<IRenderPipeline>();
	}

	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {

	}

	public boolean onWrenchClick(EntityPlayer playerIn, EnumHand hand, EnumFacing wrenchSide,
			CuboidRayTraceResult hitResult) {
		return false;
	}

	public boolean hasCapability(Capability capability, EnumFacing facing) {
		return false;
	}

	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return null;
	}
}