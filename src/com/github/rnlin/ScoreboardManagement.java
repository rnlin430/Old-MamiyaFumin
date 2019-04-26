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

import com.github.rnlin.ForReorderingPlayerScore.scoretype;

public class ScoreboardManagement {
	private MamiyaFumin plugin;
	private ScoreboardManager manager; // ScoreboardManager
	private Scoreboard board; // Scoreboard
	private int score;
	private int totalscore;
	private int rank;
	private int totalrank;
	private int bestscore;
	private Player player;

	// constructor
	public ScoreboardManagement(Player player, MamiyaFumin plugin) {
		this.plugin = plugin;
		this.player = player;
		UUID uuid = player.getUniqueId();
		score = MamiyaFumin.scorelist.get(uuid);

		// player.dataの値（ベッドに寝る、ベッド右クリック、afk時のトータルスコア + 現在のスコアを計算
		totalscore = plugin.cumulativeplayerscoreConfig.getInt(uuid.toString() + PlayerListener.FUMIN_TOTALSCORE_KEY)
				+ MamiyaFumin.scorelist.get(uuid);
		bestscore = plugin.cumulativeplayerscoreConfig.getInt(uuid.toString() + PlayerListener.FUMIN_BESTSCORE_KEY);
		ForReorderingPlayerScore frps = new ForReorderingPlayerScore(plugin);
		// プレイヤーネームでランクを検索
		List<Entry<String, Integer>> list = frps.getRankingList(scoretype.CURRENT);
		Entry<String, Integer> temp = null;

		for(Entry<String, Integer> entry : list) {
			if(player.getName() == entry.getKey()) {
				temp = entry;
				break;
			}
		}
		rank = list.indexOf(temp) + 1;

		// プレイヤーネームでトータルスコアランクを検索
		list = frps.getRankingList(scoretype.TOTAL);
		for(Entry<String, Integer> entry : list) {
			if(player.getName() == entry.getKey()) {
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
		main.getScore("§3§l-----------------").setScore(8);
		main.getScore("§3§l■Your Score■").setScore(7);
		main.getScore("§bSCORE : " + "§d§l" + String.valueOf(score)).setScore(6);
		main.getScore("§bBESTSCORE : " + "§d§l" + String.valueOf(bestscore)).setScore(5);
		main.getScore("§bTOTALSCORE : " + "§d§l" + String.valueOf(totalscore)).setScore(4);
		main.getScore("§3§l■Your Ranking■").setScore(3);
		main.getScore("§bRANKING : " + "§d§l" + String.valueOf(rank)).setScore(2);
		main.getScore("§bTOTALRANKING : " + "§d§l" + String.valueOf(totalrank)).setScore(1);
		main.getScore("§3§l―――――――――――――――――――――").setScore(0);
		this.setScoreboard(player);
	}
	// スコアボードを更新する
	public void updateScoreboard() {
		UUID uuid = player.getUniqueId();
		score = MamiyaFumin.scorelist.get(uuid);
		// プレイヤーの現在のスコアを加算&最大スコアを保存
		//plugin.pl.updateScore(player);

		totalscore = plugin.cumulativeplayerscoreConfig.getInt(uuid.toString() + PlayerListener.FUMIN_TOTALSCORE_KEY)
				+ MamiyaFumin.scorelist.get(uuid);
		bestscore = Math.max(plugin.cumulativeplayerscoreConfig.getInt(uuid.toString() + PlayerListener.FUMIN_BESTSCORE_KEY),
				MamiyaFumin.scorelist.get(uuid));
		ForReorderingPlayerScore frps = new ForReorderingPlayerScore(plugin);
		// プレイヤーネームでランクを検索
		List<Entry<String, Integer>> list = frps.getRankingList(scoretype.CURRENT);
		Entry<String, Integer> temp = null;

		for(Entry<String, Integer> entry : list) {
			if(player.getName() == entry.getKey()) {
				temp = entry;
				break;
			}
		}
		rank = list.indexOf(temp) + 1;

		// プレイヤーネームでトータルスコアランクを検索
		list = frps.getRankingList(scoretype.TOTAL);
		for(Entry<String, Integer> entry : list) {
			if(player.getName() == entry.getKey()) {
				temp = entry;
				break;
			}
		}
		totalrank = list.indexOf(temp) + 1;
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
		if ( board.getObjective(DisplaySlot.SIDEBAR) != null ) {
			board.getObjective(DisplaySlot.SIDEBAR).unregister();
        }
        board.clearSlot(DisplaySlot.SIDEBAR);
        return true;
	}
}
