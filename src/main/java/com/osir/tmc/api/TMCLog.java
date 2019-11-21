package com.osir.tmc.api;

import org.apache.logging.log4j.Logger;

public class TMCLog {
	public static Logger logger;

	public static void init(Logger log) {
		logger = log;
	}
}