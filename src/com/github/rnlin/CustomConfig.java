package com.github.rnlin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CustomConfig {
	private FileConfiguration config = null;
	final File configFile;
	private final String filename;
	private final MamiyaFumin plugin;

	// constructor
	public CustomConfig(MamiyaFumin plugin) {
		this(plugin, "playerdata.yml");
	}

	public CustomConfig(MamiyaFumin plugin, String fileName) {
		this.plugin = plugin;
		this.filename = fileName;
		configFile = new File(plugin.getDataFolder(), filename);
	}

	public void saveDefaultConfig() {
		if (!configFile.exists()) {
			plugin.saveResource(filename, false); // Overwriting
		}
	}

	public void reloadConfig() {

		config = YamlConfiguration.loadConfiguration(configFile);
		final InputStream defConfigStream = plugin.getResource(filename);
		if (defConfigStream == null) {
			return;
		}

		config.setDefaults(YamlConfiguration.loadConfiguration(
				new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
	}

	public FileConfiguration getConfig() {
		// System.out.println("getconfig start");
		if (config == null) {
			reloadConfig();
		}
		// System.out.println("getconfig finish");
		if (!(config == null)) {
			// System.out.println("config is not null");
		}
		return config;
	}

	public void saveConfig() {
		if (config == null) {
			return;
		}
		try {
			getConfig().save(configFile);
		} catch (IOException e) {
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, e);
		}
	}
}
