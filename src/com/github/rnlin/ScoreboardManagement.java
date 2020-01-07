package com.github.rnlin;

import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import com.github.rnlin.rnlibrary.PlayerMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.github.rnlin.RankingManagement.ScoreType;

import static com.github.rnlin.MamiyaFumin.*;

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

		updateScoreboardScore();

		// プレイヤーネームでランクを検索
		MamiyaFumin.getRankingManagement().run(); // 最新のランキングを取得するためにランキングの情報を更新
		updateScoreboarsRank();
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
		main.getScore("§bSCORE : " + "§d§l" + score).setScore(7);
		main.getScore("§bBEST SCORE : " + "§d§l" + bestscore).setScore(6);
		main.getScore("§bTOTAL SCORE : " + "§d§l" + totalscore).setScore(5);
		main.getScore("§3§l■Your Ranking■").setScore(4);
		main.getScore("§bRANKING : " + "§d§l" + rank).setScore(3);
		main.getScore("§bTOTAL RANKING : " + "§d§l" + totalrank).setScore(2);
		main.getScore("§bBEST RANKING : " + "§d§l" + bestrank).setScore(1);
		main.getScore("§3§l―――――――――――――――――――――").setScore(0);
		this.setScoreboard(player);
	}

	// スコアボードを更新する(ランキングは更新しない）
	public void updateScoreboardScore() {
		score = MamiyaFumin.getPlayerFumin(player).getCurrentScore();
		totalscore = MamiyaFumin.getPlayerFumin(player).getTotalScore();
		bestscore = MamiyaFumin.getPlayerFumin(player).getBestScore();
	}

	// スコアボードのランキングを更新します（更新頻度はRankingManagementインスタンス内の更新頻度に依存）
	public void updateScoreboarsRank() {

		// プレイヤーネームでランクを検索
		List<Entry<String, Integer>> list = getRankingManagement().getRankingList(ScoreType.CURRENT);
		Entry<String, Integer> temp = null;

		for (Entry<String, Integer> entry : list) {
			if (player.getName() == entry.getKey()) {
				temp = entry;
				break;
			}
		}
		rank = list.indexOf(temp) + 1;

		// プレイヤーネームでトータルスコアランクを検索
		list = getRankingManagement().getRankingList(ScoreType.TOTAL);
		for (Entry<String, Integer> entry : list) {
			if (player.getName() == entry.getKey()) {
				temp = entry;
				break;
			}
		}
		totalrank = list.indexOf(temp) + 1;

		// プレイヤーネームでトータルスコアランクを検索
		list = getRankingManagement().getRankingList(ScoreType.BEST);
		for (Entry<String, Integer> entry : list) {
			if (player.getName() == entry.getKey()) {
				temp = entry;
				break;
			}
		}
		bestrank = list.indexOf(temp) + 1;
		// PlayerMessage.debugMessage(player, "ScoreboardManagement.updateScoboars(): =>");
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
