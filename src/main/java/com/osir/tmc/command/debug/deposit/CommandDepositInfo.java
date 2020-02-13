package com.osir.tmc.command.debug.deposit;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.osir.tmc.world.OreDepositInfo;
import com.osir.tmc.world.WorldOreDepositData;

import gregtech.api.unification.material.type.Material;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandDepositInfo extends CommandBase {
	@Override
	public String getName() {
		return "info";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "tmc.command.usage.debug.deposit.info";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		EntityPlayerMP player = CommandBase.getCommandSenderAsPlayer(sender);
		OreDepositInfo info = WorldOreDepositData.getData(server.getEntityWorld())
				.getOreDepositInfo(new ChunkPos(player.getPosition()));
		List<Pair<Material, Integer>> list = info.getOreRegistry().getValueWeightList();
		sender.sendMessage(new TextComponentTranslation("tmc.command.debug.deposit.info_1", info.getWeightSum()));
		Iterator<Pair<Material, Integer>> ite = list.iterator();
		while (ite.hasNext()) {
			Pair<Material, Integer> pair = ite.next();
			player.sendStatusMessage(new TextComponentTranslation("tmc.command.debug.deposit.info_2",
					pair.getLeft().getLocalizedName(), pair.getRight()), false);
		}
	}
}