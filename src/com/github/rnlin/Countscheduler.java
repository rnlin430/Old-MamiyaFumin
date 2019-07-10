package com.github.rnlin;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Countscheduler extends BukkitRunnable{
	 private final MamiyaFumin plugin;

	 private ScoreboardManagement sbm;
	 private Player player;

	 // counterは実行回数
	 public Countscheduler(MamiyaFumin plugin, ScoreboardManagement sbm, Player player) {
	        this.plugin = plugin;
	        this.sbm = sbm;
	        this.player = player;
	    }

	 @Override
	    public void run() {
		 	sbm.removeScoreboard(player);
	 	}
}
