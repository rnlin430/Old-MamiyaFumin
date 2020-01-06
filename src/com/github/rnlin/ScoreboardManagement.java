package com.github.rnlin;

import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.github.rnlin.RankingManagement.ScoreType;

// スコアボードを生成し各種操作を行います
public class ScoreboardManagement {
	private MamiyaFumin plugin;
	private ScoreboardManager manager; // ScoreboardManager
	private Scoreboard board; // Scoreboard
	private int score;
	private int totalscore;
	private int rank;
	private int totalrank;
	private int bestscore;
	private int bestrank;
	private Player player;

	// constructor
	public ScoreboardManagement(Player player, MamiyaFumin plugin) {
		this.plugin = plugin;
		this.player = player;
		UUID uuid = player.getUniqueId();
		score = MamiyaFumin.scoreList.get(uuid);

		// player.dataの値（ベッドに寝る、ベッド右クリック、afk時のトータルスコア + 現在のスコアを計算
		totalscore = plugin.cumulativePlayerscoreConfig.getInt(uuid.toString() + PlayerListener.FUMIN_TOTALSCORE_KEY)
				+ MamiyaFumin.scoreList.get(uuid);
		bestscore = plugin.cumulativePlayerscoreConfig.getInt(uuid.toString() + PlayerListener.FUMIN_BESTSCORE_KEY);
		RankingManagement frps = new RankingManagement(plugin);

		// プレイヤーネームでランクを検索
		List<Entry<String, Integer>> list = frps.getRankingList(ScoreType.CURRENT);
		Entry<String, Integer> temp = null;

		for (Entry<String, Integer> entry : list) {
			if (player.getName() == entry.getKey()) {
				temp = entry;
				break;
			}
		}
		rank = list.indexOf(temp) + 1;

		// プレイヤーネームでトータルスコアランクを検索
		list = frps.getRankingList(ScoreType.TOTAL);
		for (Entry<String, Integer> entry : list) {
			if (player.getName() == entry.getKey()) {
				temp = entry;
				break;
			}
		}
		totalrank = list.indexOf(temp) + 1;
	}

	public void setPlayerScoreboad() {

		// ScoreboardManager を取得
		this.manager = Bukkit.getScoreboardManager();

		// Scoreboard を取得
		this.board = manager.getNewScoreboard();

		Objective main = board.registerNewObjective("RANING", "dummy", "hogehoge");
		main.setDisplaySlot(DisplaySlot.SIDEBAR);
		main.setDisplayName("§b§f:*§bMamiyaFumin§f*:");
		main.getScore("§3§l-----------------").setScore(9);
		main.getScore("§3§l■Your Score■").setScore(8);
		main.getScore("§bSCORE : " + "§d§l" + String.valueOf(score)).setScore(7);
		main.getScore("§bBEST SCORE : " + "§d§l" + String.valueOf(bestscore)).setScore(6);
		main.getScore("§bTOTAL SCORE : " + "§d§l" + String.valueOf(totalscore)).setScore(5);
		main.getScore("§3§l■Your Ranking■").setScore(4);
		main.getScore("§bRANKING : " + "§d§l" + String.valueOf(rank)).setScore(3);
		main.getScore("§bTOTAL RANKING : " + "§d§l" + String.valueOf(totalrank)).setScore(2);
		main.getScore("§bBEST RANKING : " + "§d§l" + String.valueOf(bestrank)).setScore(1);
		main.getScore("§3§l―――――――――――――――――――――").setScore(0);
		this.setScoreboard(player);
	}

	// スコアボードを更新する(ランキングは更新しない）
	public void updateScoreboardScore() {
		UUID uuid = player.getUniqueId();
		score = MamiyaFumin.scoreList.get(uuid);
		// プレイヤーの現在のスコアを加算&最大スコアを保存
		totalscore = plugin.cumulativePlayerscoreConfig.getInt(uuid.toString() + PlayerListener.FUMIN_TOTALSCORE_KEY)
				+ MamiyaFumin.scoreList.get(uuid);
		bestscore = Math.max(MamiyaFumin.scoreBestlist.get(uuid), score);
	}

	public void updateScoreboarsRank() {
		RankingManagement frps = new RankingManagement(plugin);
		// プレイヤーネームでランクを検索
		List<Entry<String, Integer>> list = frps.getRankingList(ScoreType.CURRENT);
		Entry<String, Integer> temp = null;

		for (Entry<String, Integer> entry : list) {
			if (player.getName() == entry.getKey()) {
				temp = entry;
				break;
			}
		}
		rank = list.indexOf(temp) + 1;

		// プレイヤーネームでトータルスコアランクを検索
		list = frps.getRankingList(ScoreType.TOTAL);
		for (Entry<String, Integer> entry : list) {
			if (player.getName() == entry.getKey()) {
				temp = entry;
				break;
			}
		}
		totalrank = list.indexOf(temp) + 1;

		// プレイヤーネームでトータルスコアランクを検索
		list = frps.getRankingList(ScoreType.BEST);
		for (Entry<String, Integer> entry : list) {
			if (player.getName() == entry.getKey()) {
				temp = entry;
				break;
			}
		}
		bestrank = list.indexOf(temp) + 1;
	}

	// プレイヤーのスコアボードをプレイヤーに表示する
	public void setScoreboard(Player p) {
		p.setScoreboard(board);
	}

	// プレイヤーのスコアボードを非表示にする
	public void removeScoreboard(Player p) {
		p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
	}

	// スコアボードを非表示にする
	public boolean deleteScoreboard() {
		if (board.getObjective(DisplaySlot.SIDEBAR) != null) {
			board.getObjective(DisplaySlot.SIDEBAR).unregister();
		}
		board.clearSlot(DisplaySlot.SIDEBAR);
		return true;
	}
}
