package com.osir.tmc.api.te;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.tuple.Pair;

import com.osir.tmc.ModCreativeTab;
import com.osir.tmc.api.capability.te.IPaintable;
import com.osir.tmc.api.render.CubePipeline;
import com.osir.tmc.api.render.IRenderPipeline;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.vec.Cuboid6;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
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

public abstract class MetaValueTileEntity implements IPaintable {
	public static final int DEFAULT_PAINT_COLOR = 0xffffff;

	protected MetaTileEntity holder;
	protected String modid, name;
	protected Material material;
	protected EnumFacing frontFace;
	protected int[] paintedColor;

	public MetaValueTileEntity(String modid, String name, Material material) {
		this.modid = modid;
		this.name = name;
		this.material = material;
		this.frontFace = EnumFacing.NORTH;
		this.paintedColor = new int[6];
		for (int i = 0; i < this.paintedColor.length; i++) {
			this.paintedColor[i] = DEFAULT_PAINT_COLOR;
		}
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

	public int getHardness() {
		return 0;
	}

	public int getHarvestLevel() {
		return -1;
	}

	public String getHarvestTool() {
		return "wrench";
	}

	public void update() {

	}

	public boolean supportCreativeTab(CreativeTabs tab) {
		return tab == ModCreativeTab.tabEquipment || tab == CreativeTabs.SEARCH;
	}

	public void markDirty() {
		this.holder.markDirty();
	}

	public boolean isInvalid() {
		return this.holder == null || this.holder.isInvalid();
	}

	public int getId() {
		return MetaTileEntityRegistry.getId(this);
	}

	public World getWorld() {
		return this.holder == null ? null : this.holder.getWorld();
	}

	public BlockPos getPos() {
		return this.holder == null ? null : this.holder.getPos();
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	}

	public boolean hasSpecialDisplayeName(ItemStack stack) {
		return false;
	}

	public String getDisplayName(ItemStack stack) {
		return null;
	}

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		return false;
	}

	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {
		drops.add(new ItemStack(MetaTileEntityRegistry.getBlock(this), 1, this.getId()));
	}

	public void paint(EnumFacing side, int color) {
		this.paintedColor[side.getIndex()] = color;
	}

	public int getPaintedColor(EnumFacing side) {
		return this.paintedColor[side.getIndex()];
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {

	}

	@SideOnly(Side.CLIENT)
	public boolean canRender(BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT_MIPPED;
	}

	@SideOnly(Side.CLIENT)
	public List<IRenderPipeline> getRenderPipeline(boolean renderItem) {
		BlockPos pos = renderItem ? BlockPos.ORIGIN : this.getPos();
		return Arrays.asList(new CubePipeline(pos, new Cuboid6(0, 0, 0, 1, 1, 1)));
	}

	@SideOnly(Side.CLIENT)
	public void onRender(CCRenderState render) {

	}

	@SideOnly(Side.CLIENT)
	public Pair<TextureAtlasSprite, Integer> getParticleTexture(World world, BlockPos pos) {
		return Pair.of(TextureUtils.getMissingSprite(), 0xffffff);
	}

	protected void scheduleChunkForRenderUpdate() {
		BlockPos pos = this.getPos();
		this.getWorld().markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1,
				pos.getY() + 1, pos.getZ() + 1);
	}

	public NBTTagCompound writeItemStackNBT(NBTTagCompound nbt) {
		return nbt;
	}

	public void readItemStackNBT(NBTTagCompound nbt) {

	}

	public void writeInitialSyncData(PacketBuffer buf) {

	}

	public void receiveInitialSyncData(PacketBuffer buf) {

	}

	public void writeCustomData(int discriminator, Consumer<PacketBuffer> dataWriter) {
		if (this.holder != null) {
			this.holder.writeCustomData(discriminator, dataWriter);
		}
	}

	public void receiveCustomData(int discriminator, PacketBuffer buf) {

	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		return nbt;
	}

	public void readFromNBT(NBTTagCompound nbt) {

	}

	public boolean hasCapability(Capability capability, EnumFacing facing) {
		return false;
	}

	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return null;
	}
}