package com.github.rnlin;

import org.bukkit.scheduler.BukkitRunnable;

// このクラスのインスタンスは一定時間毎にスコアボードの値を更新します
public class UpdateScoreboard extends BukkitRunnable {
	private ScoreboardManagement sbm;
	private int count;
	private MamiyaFumin plugin;

	public UpdateScoreboard(MamiyaFumin pluguin, int count, ScoreboardManagement sbm) throws IllegalArgumentException {
		this.sbm = sbm;
		this.plugin = plugin;
		if (count < 1) {
			throw new IllegalArgumentException("counter には1以上を指定してください。");
		} else {
			this.count = count;
		}
	}

	@Override
	public void run() {
		if (count > 0) {
			count--;
			sbm.updateScoreboardScore();
			sbm.setPlayerScoreboad();
		} else {
			this.cancel();
		}
	}
}
