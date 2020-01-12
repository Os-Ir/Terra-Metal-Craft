package com.osir.tmc.api.util;

import java.util.List;

import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;

public class DividedInfoBuilder {
	private List<InfoBuf> list;

	public DividedInfoBuilder() {
		this.list = NonNullList.create();
	}

	public void addInfo(InfoBuf info) {
		this.list.add(info);
	}

	public String build() {
		String str = TextFormatting.RESET + "( ";
		for (int i = 0; i < this.list.size(); i++) {
			if (i != 0) {
				str += TextFormatting.RESET + " | ";
			}
			str += list.get(i).build();
		}
		str += TextFormatting.RESET + " )";
		return str;
	}
}