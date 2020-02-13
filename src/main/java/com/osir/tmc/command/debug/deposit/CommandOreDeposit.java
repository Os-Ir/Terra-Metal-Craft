package com.osir.tmc.command.debug.deposit;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandOreDeposit extends CommandTreeBase {
	public CommandOreDeposit() {
		this.addSubcommand(new CommandDepositInfo());
	}

	@Override
	public String getName() {
		return "deposit";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "tmc.command.usage.debug.deposit";
	}
}