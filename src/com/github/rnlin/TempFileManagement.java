package com.github.rnlin;

import java.util.HashMap;
import java.util.UUID;

public class TempFileManagement {
	MamiyaFumin plugin;
	private String FUMIN_TEMP_KEY = "temp";

	public TempFileManagement(MamiyaFumin plugin) {
		this.plugin = plugin;
	}

	// scorelistをtemp.ymlに保存
	public void saveScorelist() {
		HashMap<String, Integer> STRING_SCORELIST = new HashMap<>();
		if (MamiyaFumin.scoreList == null)
			return;
		for (UUID uuid : MamiyaFumin.scoreList.keySet()) {
			Integer temp = MamiyaFumin.scoreList.get(uuid);
			STRING_SCORELIST.put(uuid.toString(), temp);
		}
		plugin.tempConfig.createSection(FUMIN_TEMP_KEY, STRING_SCORELIST);
		plugin.customconfigTemp.saveConfig();
	}

	public HashMap<UUID, Integer> restoreScorelist() {
		HashMap<UUID, Integer> tempscorelist = new HashMap<>();
		for (String key : plugin.tempConfig.getConfigurationSection(FUMIN_TEMP_KEY).getKeys(false)) {
			int value = Integer.parseInt(plugin.tempConfig.getString(FUMIN_TEMP_KEY + "." + key));
			UUID uuid = UUID.fromString(key);
			tempscorelist.put(uuid, value);
		}
		return tempscorelist;
	}

}
