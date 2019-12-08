package com.osir.tmc.api.heat;

import org.apache.commons.lang3.Validate;

public class MaterialStack {
	public static final MaterialStack EMPTY = new MaterialStack(HeatMaterialList.EMPTY, 0);

	private HeatMaterial material;
	private int amount;

	public MaterialStack(HeatMaterial material, int amount) {
		Validate.notNull(material);
		this.material = material;
		this.amount = Math.max(amount, 0);
	}

	public MaterialStack(MaterialStack stack, int amount) {
		Validate.notNull(stack);
		this.material = stack.material;
		this.amount = Math.max(amount, 0);
	}

	public HeatMaterial getMaterial() {
		return this.material;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = Math.max(amount, 0);
	}

	public MaterialStack copy() {
		return new MaterialStack(this, this.amount);
	}

	public boolean isEmpty() {
		return this.amount == 0 || this.material == HeatMaterialList.EMPTY;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MaterialStack)) {
			return false;
		}
		MaterialStack stack = (MaterialStack) obj;
		return stack.material.equals(this.material) && stack.amount == this.amount;
	}
}