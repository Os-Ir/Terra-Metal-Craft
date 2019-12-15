package com.osir.tmc.api.util;

import com.osir.tmc.api.util.function.Validation;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemIndex {
	private Item item;
	private int meta;
	private Validation<ItemIndex, ItemStack> vali;

	public ItemIndex(Item item, Validation<ItemIndex, ItemStack> vali) {
		this.item = item;
		this.vali = vali;
	}

	public ItemIndex(Item item, int meta, Validation<ItemIndex, ItemStack> vali) {
		this.item = item;
		this.meta = meta;
		this.vali = vali;
	}

	public ItemIndex(Block block, Validation<ItemIndex, ItemStack> vali) {
		this(Item.getItemFromBlock(block), vali);
	}

	public ItemIndex(Block block, int meta, Validation<ItemIndex, ItemStack> vali) {
		this(Item.getItemFromBlock(block), meta, vali);
	}

	public ItemIndex(ItemStack stack, Validation<ItemIndex, ItemStack> vali) {
		this.item = stack.getItem();
		this.meta = stack.getItemDamage();
		this.vali = vali;
	}

	public boolean validate(ItemStack stack) {
		return this.vali.test(this, stack);
	}

	public ItemStack createStack() {
		return this.createStack(1);
	}

	public ItemStack createStack(int size) {
		return new ItemStack(this.item, size, this.meta);
	}

	public Item getItem() {
		return this.item;
	}

	public int getMeta() {
		return this.meta;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ItemIndex)) {
			return false;
		}
		ItemIndex idx = (ItemIndex) obj;
		return this.item == idx.item && this.meta == idx.meta;
	}
}