package com.osir.tmc.te;

import java.util.ArrayList;
import java.util.List;

import com.osir.tmc.Main;
import com.osir.tmc.api.render.CubePipeline;
import com.osir.tmc.api.render.IRenderPipeline;
import com.osir.tmc.api.te.MetaValueTileEntity;
import com.osir.tmc.api.texture.TextureRegister;
import com.osir.tmc.item.MetaItems;

import codechicken.lib.vec.Cuboid6;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MTEStoneWorkTable extends MetaValueTileEntity {
	public static final TextureRegister TEXTURE_SIDE = new TextureRegister(Main.MODID, "blocks/stone_work_table_side");
	public static final TextureRegister TEXTURE_TOP = new TextureRegister(Main.MODID, "blocks/stone_work_table_top");
	public static final TextureRegister TEXTURE_ENGRAVING_SIDE = new TextureRegister(Main.MODID,
			"blocks/tool_engraving_side");
	public static final TextureRegister TEXTURE_ENGRAVING_TOP = new TextureRegister(Main.MODID,
			"blocks/tool_engraving_top");

	protected ItemStack engraving;

	public MTEStoneWorkTable() {
		super(Main.MODID, "stone_work_table", Material.ROCK);
		this.engraving = ItemStack.EMPTY;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return new AxisAlignedBB(0, 0, 0, 1, 0.875, 1);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {
		super.getDrops(drops, world, pos, state, fortune);
		if (!this.engraving.isEmpty()) {
			drops.add(this.engraving);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<IRenderPipeline> getRenderPipeline() {
		List<IRenderPipeline> list = new ArrayList<IRenderPipeline>();
		list.add(new CubePipeline(this.getPos(), new Cuboid6(0, 0, 0, 1, 0.875, 1))
				.addSideTexture(TEXTURE_SIDE.getTexture()).addTexture(EnumFacing.UP, TEXTURE_TOP.getTexture())
				.addTexture(EnumFacing.DOWN, TEXTURE_TOP.getTexture()));
		if (!this.engraving.isEmpty()) {
			list.add(new CubePipeline(this.getPos(), new Cuboid6(0.125, 0.875, 0.125, 0.875, 0.9375, 0.875))
					.addSideTexture(TEXTURE_ENGRAVING_SIDE.getTexture())
					.addTexture(EnumFacing.UP, TEXTURE_ENGRAVING_TOP.getTexture())
					.addTexture(EnumFacing.DOWN, TEXTURE_ENGRAVING_TOP.getTexture()));
		}
		return list;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack heldStack = player.getHeldItem(hand);
		if (this.engraving.isEmpty()) {
			if (MetaItems.toolEngraving.isItemEqual(heldStack)) {
				this.engraving = heldStack.copy();
				this.engraving.setCount(1);
				heldStack.shrink(1);
			}
		} else {
			player.addItemStackToInventory(this.engraving);
			if (!this.engraving.isEmpty()) {
				player.dropItem(this.engraving, false, false);
			}
			this.engraving = ItemStack.EMPTY;
		}
		this.scheduleChunkForRenderUpdate();
		return true;
	}

	@Override
	public MetaValueTileEntity create() {
		return new MTEStoneWorkTable();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if (!this.engraving.isEmpty()) {
			nbt.setTag("engraving", this.engraving.writeToNBT(new NBTTagCompound()));
		}
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("engraving")) {
			this.engraving = new ItemStack(nbt.getCompoundTag("engraving"));
		}
	}
}