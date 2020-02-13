package com.osir.tmc.command;

import java.util.Arrays;
import java.util.List;

import com.osir.tmc.command.debug.CommandDebug;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class TMCCommand extends CommandTreeBase {
	public TMCCommand() {
		this.addSubcommand(new CommandDebug());
	}

	@Override
	public String getName() {
		return "tmc";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("terrametalcraft");
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "tmc.command.usage";
	}
}