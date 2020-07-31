package com.github.rnlin;

import java.util.UUID;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

import com.earth2me.essentials.Essentials;

import net.ess3.api.events.AfkStatusChangeEvent;
import sun.plugin2.message.BestJREAvailableMessage;

import static com.github.rnlin.MamiyaFumin.*;

public class PlayerListener implements Listener {
	private MamiyaFumin plugin;


	public PlayerListener(MamiyaFumin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	// プレイヤーがログインするときのイベントハンドラ
	@EventHandler
	public void onLogin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		UUID u = player.getUniqueId();
		if(!scoreList.containsKey(u)) {
			//新しくログインしたプレイヤーのスコアデータを作成
			Integer scoredata = new Integer(0);
			MamiyaFumin.scoreList.put(u, scoredata);
			//ログインしたプレイヤーのベストスコアをメモリ上に追加
			int bScore, cScore;
			if(MamiyaFumin.scoreBestlist.get(u) == null) {
				bScore = 0;
			} else {
				bScore = MamiyaFumin.scoreBestlist.get(u);
			}
			if(MamiyaFumin.scoreList.get(u) == null) {
				cScore = 0;
			} else {
				cScore = MamiyaFumin.scoreList.get(u);
			}

			int bestscore = Math.max(bScore, cScore);
			MamiyaFumin.scoreBestlist.put(u, bestscore);


			return;
		}
		// スコアデータがある場合はスコアデータに統計値が合わせる（統計値を書き換える）
		int score = scoreList.get(u);
		player.setStatistic(Statistic.TIME_SINCE_REST, score * magnification);
		int bestscore = Math.max(
				MamiyaFumin.scoreBestlist.get(u), MamiyaFumin.scoreList.get(u)
		);
		MamiyaFumin.scoreBestlist.put(u, bestscore);
	}

	// プレイヤーがログアウトするときのイベントハンドラ
	@EventHandler
	public void onLogout(PlayerQuitEvent e) {
		plugin.tfm.saveScorelist();
	}

	// AFK状態が変化したときのイベントハンドラ
	@EventHandler
	public void onAfkChange(final AfkStatusChangeEvent e) {
		// System.out.println("AFKchange");
		Player player;
		try {
			player = plugin.getServer().getPlayer(e.getController().getName());
		} catch (Exception e1) {
//System.out.println("[Debug] PlayerListener#onAfkChange e1=" + e1);
			player = (Player) plugin.getServer().getOfflinePlayer(e.getController().getName());
		}

		try {
			if (!(e.getController().isAfk())) {
				saveTotalBestScore(player);

				// 現在のスコアをリセット前に累積スコアに現在のスコアを加算
				MamiyaFumin.getPlayerFumin(player).increaseTotalScore(
						player.getStatistic(Statistic.TIME_SINCE_REST) / MamiyaFumin.magnification);

				Utility.resetStatistic(player, Statistic.TIME_SINCE_REST);
			}
		} catch (Exception e2) {
			System.out.println("[Debug] PlayerListener.onAfkChange():e2 " + e2);
		}
	}

	// プレイヤーの統計が増加したときのイベントハンドラ
	@EventHandler
	public void onChangePlayerStatistic(PlayerStatisticIncrementEvent e) {
		Player player = e.getPlayer();
		Statistic type = e.getStatistic();
		if (type != Statistic.TIME_SINCE_REST) return;

		Essentials ess = (Essentials) plugin.getServer().getPluginManager().getPlugin("Essentials");
		if (ess.getUser(player).isAfk()) {
			Utility.resetStatistic(player, Statistic.TIME_SINCE_REST);
			e.setCancelled(true);
		}
		if (player.isDead()) {
			Utility.resetStatistic(player, Statistic.TIME_SINCE_REST);
			e.setCancelled(true);
		}
	}

	// プレイヤーがベッドを右クリックしたときのイベントハンドラ
	@EventHandler
	public void onPlayerBedEnter(PlayerBedEnterEvent e) {
		Player player = e.getPlayer();
		saveTotalBestScore(player);

		// 現在のスコアをリセット前に累積スコアに現在のスコアを加算
		MamiyaFumin.getPlayerFumin(player).increaseTotalScore(
				player.getStatistic(Statistic.TIME_SINCE_REST) / MamiyaFumin.magnification);

		Utility.resetStatistic(player, Statistic.TIME_SINCE_REST);
	}

	// プレイヤーが死んだときのイベントハンドラ
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (!(e.getEntity() instanceof Player)) return;
		Player player = e.getEntity();
		saveTotalBestScore(player);
		// 現在のスコアをリセット前に累積スコアに現在のスコアを加算
		MamiyaFumin.getPlayerFumin(player).increaseTotalScore(
				player.getStatistic(Statistic.TIME_SINCE_REST) / MamiyaFumin.magnification);
		Utility.resetStatistic(player, Statistic.TIME_SINCE_REST);
		if (plugin.playerDeathMessage != null) {
			player.sendMessage(plugin.playerDeathMessage);
		}
	}

	// プレイヤーの現在のスコアを加算&最大スコアをコンフィグに保存&更新
	private void saveTotalBestScore(Player player) {
		UUID uuid = player.getUniqueId();
		String stringuuid = uuid.toString();
		int currentscore = player.getStatistic(Statistic.TIME_SINCE_REST) / MamiyaFumin.magnification;
		if (!(plugin.cumulativePlayerscoreConfig.contains(stringuuid + FUMIN_TOTALSCORE_KEY))) {
			plugin.cumulativePlayerscoreConfig.set(stringuuid + FUMIN_TOTALSCORE_KEY, currentscore);
		} else {
			plugin.cumulativePlayerscoreConfig.set(stringuuid + FUMIN_TOTALSCORE_KEY,
					plugin.cumulativePlayerscoreConfig.getInt(stringuuid + FUMIN_TOTALSCORE_KEY) + currentscore);
		}

		if (plugin.cumulativePlayerscoreConfig.contains(stringuuid + FUMIN_BESTSCORE_KEY)) {
			int result = Math.max(currentscore,
					plugin.cumulativePlayerscoreConfig.getInt(stringuuid + FUMIN_BESTSCORE_KEY));
			plugin.cumulativePlayerscoreConfig.set(stringuuid + FUMIN_BESTSCORE_KEY, result);
			plugin.customconfigCumulative.saveConfig();
		} else {
			plugin.cumulativePlayerscoreConfig.set(stringuuid + FUMIN_BESTSCORE_KEY, currentscore);
			plugin.customconfigCumulative.saveConfig();
		}
	}


//	private void saveBestScore(Player player) {
//		UUID uuid = player.getUniqueId();
//		String stringuuid = uuid.toString();
//		int currentscore = player.getStatistic(Statistic.TIME_SINCE_REST) / MamiyaFumin.magnification;
//		if (plugin.cumulativePlayerscoreConfig.contains(stringuuid + FUMIN_BESTSCORE_KEY)) {
//			int result = Math.max(currentscore,
//					plugin.cumulativePlayerscoreConfig.getInt(stringuuid + FUMIN_BESTSCORE_KEY));
//			plugin.cumulativePlayerscoreConfig.set(stringuuid + FUMIN_BESTSCORE_KEY, result);
//			plugin.customconfigCumulative.saveConfig();
//		}
//		else {
//			plugin.cumulativePlayerscoreConfig.set(stringuuid + FUMIN_BESTSCORE_KEY, currentscore);
//			plugin.customconfigCumulative.saveConfig();
//		}
//	}

}
