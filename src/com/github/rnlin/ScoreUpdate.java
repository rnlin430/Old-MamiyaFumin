package com.github.rnlin;

import java.util.UUID;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreUpdate extends BukkitRunnable {

	MamiyaFumin plugin;
	public ScoreUpdate(MamiyaFumin p) {
		plugin = p;
	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ

		// プレイヤーリストにいる（現在参加中のプレイヤー）一人一人の現在の統計値をスコアリストに格納
		for(Player player : plugin.playerlist) {
			int temp = player.getStatistic(Statistic.TIME_SINCE_REST)/MamiyaFumin.Magnification;
			UUID uuid = player.getUniqueId();
			// Integer scoredata = MamiyaFumin.scorelist.get(uuid);
			Integer new_scoredata = new Integer(temp);
			MamiyaFumin.scorelist.put(uuid,new_scoredata);
		}

	}

}
