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

import static com.github.rnlin.MamiyaFumin.FUMIN_BESTSCORE_KEY;
import static com.github.rnlin.MamiyaFumin.FUMIN_TOTALSCORE_KEY;

public class PlayerListener implements Listener {
	private MamiyaFumin plugin;


	public PlayerListener(MamiyaFumin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	// プレイヤーがログインするときのイベントハンドラ
	@EventHandler
	public void onLogin(PlayerJoinEvent e) {
		//ログインしたプレイヤーのスコアデータを追加
		Player player = (Player) e.getPlayer();
		UUID u = player.getUniqueId();
		Integer scoredata = new Integer(0);
		MamiyaFumin.scoreList.put(u, scoredata);
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
		} catch (Exception e2) {
			System.out.println("[Debug] PlayerListener.onAfkChange(): " + e2);
			player = (Player) plugin.getServer().getOfflinePlayer(e.getController().getName());
		}

		try {
			if (!(e.getController().isAfk())) {
				updateScore(player);
				MamiyaFumin.resetStatistic(player, Statistic.TIME_SINCE_REST);
			}
		} catch (Exception e2) {
			System.out.println("[Debug] PlayerListener.onAfkChange(): " + e2);
		}
	}

	// プレイヤーの統計が増加したときのイベントハンドラ
	@EventHandler
	public void onChangePlayerStatistic(PlayerStatisticIncrementEvent e) {
		Player player = e.getPlayer();

		Essentials ess = (Essentials) plugin.getServer().getPluginManager().getPlugin("Essentials");
		if (ess.getUser(player).isAfk()) {
			MamiyaFumin.resetStatistic(player, Statistic.TIME_SINCE_REST);

		}
		if (player.isDead()) {
			MamiyaFumin.resetStatistic(player, Statistic.TIME_SINCE_REST);
		}

	}

	// プレイヤーがベッドを右クリックしたときのイベントハンドラ
	@EventHandler
	public void onPlayerBedEnter(PlayerBedEnterEvent e) {
		Player player = e.getPlayer();
		updateScore(player);
		MamiyaFumin.resetStatistic(player, Statistic.TIME_SINCE_REST);
	}

	// プレイヤーが死んだときのイベントハンドラ
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (!(e.getEntity() instanceof Player)) return;
		Player player = e.getEntity();
		updateScore(player);
		MamiyaFumin.resetStatistic(player, Statistic.TIME_SINCE_REST);

	}

	// プレイヤーの現在のスコアを加算&最大スコアをコンフィグに保存&更新
	private void updateScore(Player player) {
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


//	private void updateBestScore(Player player) {
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
