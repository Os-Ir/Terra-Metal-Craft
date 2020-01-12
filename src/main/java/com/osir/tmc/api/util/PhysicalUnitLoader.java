package com.osir.tmc.api.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

import com.osir.tmc.Main;
import com.osir.tmc.api.TMCLog;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;

public class PhysicalUnitLoader {
	public static final Map<String, Entry<String, TextFormatting>> UNIT = new HashMap<String, Entry<String, TextFormatting>>();

	public static void load() {
		TMCLog.logger.info("Loading physical units from config...");
		Path path = Loader.instance().getConfigDir().toPath().resolve(Main.MODID);
		Path lock = path.resolve("physical_unit.lock");
		TMCLog.logger.info("Unit config path: [" + path + "]");
		if (!Files.exists(lock)) {
			try {
				Files.createDirectories(path);
				Files.createFile(lock);
			} catch (IOException e) {
				e.printStackTrace();
			}
			FileSystem zip = null;
			try {
				URI sample = PhysicalUnitLoader.class.getResource("/assets/tmc/assets.root").toURI();
				Path file;
				if (sample.getScheme().equals("jar") || sample.getScheme().equals("zip")) {
					zip = FileSystems.newFileSystem(sample, Collections.emptyMap());
					file = zip.getPath("/assets/tmc/physical_unit.json");
				} else if (sample.getScheme().equals("file")) {
					file = Paths.get(PhysicalUnitLoader.class.getResource("/assets/tmc/physical_unit.json").toURI());
				} else {
					throw new IllegalStateException("Failed to load file by URI: [" + sample + "]");
				}
				if (!Files.exists(file)) {
					throw new IOException("Assets physical unit file is nonexistent: [" + file + "]");
				}
				TMCLog.logger.info("Assets physical unit file: [" + file + "]");
				Files.copy(file, path.resolve("physical_unit.json"), StandardCopyOption.REPLACE_EXISTING);
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(zip);
			}
		}
	}

	public static String getUnit(String type) {
		return UNIT.get(type).getKey();
	}

	public static String getFormattedUnit(String type) {
		Entry<String, TextFormatting> entry = UNIT.get(type);
		return entry.getValue() + entry.getKey();
	}
}