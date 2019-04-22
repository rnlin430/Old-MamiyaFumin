package com.github.rnlin;


import java.util.UUID;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

import net.ess3.api.events.AfkStatusChangeEvent;

public class PlayerListener implements Listener{
	private MamiyaFumin plugin;
	protected static String FUMIN_TOTALSCORE_KEY = ".FuminTotalScore";
	protected static String FUMIN_BESTSCORE_KEY = ".FuminBestScore";

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
		MamiyaFumin.scorelist.put(u,scoredata);

		// ログの出力
		/*
		String playername = player.getName();
		System.out.println("\u001b[32m" + playername + "さんがまみや不眠に参加しました" + "\u001b[m");
		*/
	}

	// プレイヤーがログアウトするときのイベントハンドラ
	@EventHandler
	public void onLogout(PlayerQuitEvent e) {
		// scorelistからプレイヤーのスコアを削除
		Player player = (Player) e.getPlayer();
		UUID u = player.getUniqueId();
		if(!MamiyaFumin.scorelist.containsKey(u)) return;
	}

	// AFK状態が変化したときのイベントハンドラ
	@EventHandler
	public void onAfkChange(final AfkStatusChangeEvent e){
		// System.out.println("AFKchange");
		Player player;
		try {
			player = plugin.getServer().getPlayer(e.getController().getName());
		} catch (Exception e2) {
			System.out.println("1:" + e2);
			player = (Player) plugin.getServer().getOfflinePlayer(e.getController().getName());
		}

	 	try {
		 	//　累積スコアとして保存する プレイヤーが非AFK状態からAFKになったときに統計値がリセットされプレイヤーのスコアconfigに保存される
		 	if(!(plugin.ess.getUser(player).isAfk())){
		 		updateScore(player);
				MamiyaFumin.resetStatistic(player,Statistic.TIME_SINCE_REST);
		 	}
		} catch (Exception e2) {
			System.out.println("2:" + e2);
			// TODO: handle exception
		}
	}

	// プレイヤーの統計が増加したときのイベントハンドラ
	@EventHandler(priority = EventPriority.HIGH)
	public void onChangePlayerStatistic(PlayerStatisticIncrementEvent e) {
		Player player = e.getPlayer();
		//
		try {
			if(plugin.ess.getUser(player).isAfk()){
				//UUID uuid = player.getUniqueId();
				MamiyaFumin.resetStatistic(player,Statistic.TIME_SINCE_REST);
		    }
		} catch (Exception e2) {
			// TODO: handle exception
		}
	}

	// プレイヤーがベッドを右クリックしたときのイベントハンドラ
	@EventHandler
	public void onPlayerBedEnter(PlayerBedEnterEvent e) {
		Player player = e.getPlayer();
		updateScore(player);
		MamiyaFumin.resetStatistic(player, Statistic.TIME_SINCE_REST);
		// UUID uuid = UUID.fromString(uuid.toString());
	}

	// プレイヤーの現在のスコアを加算&最大スコア
	public void updateScore(Player player) {
		UUID uuid = player.getUniqueId();
		String stringuuid = uuid.toString();
		int currentscore = player.getStatistic(Statistic.TIME_SINCE_REST)/MamiyaFumin.Magnification;
		if(!(plugin.cumulativeplayerscoreConfig.contains(stringuuid + FUMIN_TOTALSCORE_KEY))) {
			plugin.cumulativeplayerscoreConfig.set(stringuuid + FUMIN_TOTALSCORE_KEY, currentscore);
		}
		else {
		plugin.cumulativeplayerscoreConfig.set(stringuuid + FUMIN_TOTALSCORE_KEY,
				plugin.cumulativeplayerscoreConfig.getInt(stringuuid + FUMIN_TOTALSCORE_KEY) + currentscore);
		}

		if(plugin.cumulativeplayerscoreConfig.contains(stringuuid + FUMIN_BESTSCORE_KEY)) {
			int result = Math.max(currentscore, plugin.cumulativeplayerscoreConfig.
					getInt(stringuuid + FUMIN_BESTSCORE_KEY));
			plugin.cumulativeplayerscoreConfig.set(stringuuid + FUMIN_BESTSCORE_KEY, result);
			plugin.customconfigCumulative.saveConfig();
			return;
		}
		plugin.cumulativeplayerscoreConfig.set(stringuuid + FUMIN_BESTSCORE_KEY, currentscore);
		plugin.customconfigCumulative.saveConfig();
	}

	public void updateBestScore(Player player) {
		UUID uuid = player.getUniqueId();
		String stringuuid = uuid.toString();
		int currentscore = player.getStatistic(Statistic.TIME_SINCE_REST)/MamiyaFumin.Magnification;
		if(plugin.cumulativeplayerscoreConfig.contains(stringuuid + FUMIN_BESTSCORE_KEY)) {
			int result = Math.max(currentscore, plugin.cumulativeplayerscoreConfig.
					getInt(stringuuid + FUMIN_BESTSCORE_KEY));
			plugin.cumulativeplayerscoreConfig.set(stringuuid + FUMIN_BESTSCORE_KEY, result);
			plugin.customconfigCumulative.saveConfig();
			return;
		}
		plugin.cumulativeplayerscoreConfig.set(stringuuid + FUMIN_BESTSCORE_KEY, currentscore);
		plugin.customconfigCumulative.saveConfig();
	}

}
