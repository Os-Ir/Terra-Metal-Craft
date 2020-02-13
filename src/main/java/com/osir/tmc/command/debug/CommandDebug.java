package com.osir.tmc.command.debug;

import com.osir.tmc.command.debug.deposit.CommandOreDeposit;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandDebug extends CommandTreeBase {
	public CommandDebug() {
		this.addSubcommand(new CommandOreDeposit());
	}

	@Override
	public String getName() {
		return "debug";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "tmc.command.usage.debug";
	}
}