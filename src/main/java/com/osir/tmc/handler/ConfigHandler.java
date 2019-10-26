package com.osir.tmc.handler;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigHandler {
	private static Configuration config;
	private static Logger logger;

	public ConfigHandler(FMLPreInitializationEvent e) {
		logger = e.getModLog();
		config = new Configuration(e.getSuggestedConfigurationFile());
		config.load();
	}
}