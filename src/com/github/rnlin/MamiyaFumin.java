package com.github.rnlin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;

public class MamiyaFumin extends JavaPlugin implements Listener {

	static MamiyaFumin plugin;
	public Essentials ess = (Essentials) this.getServer().getPluginManager().getPlugin("Essentials");
	static int Magnification = 20 * 2;

	Collection<? extends Player> playerlist; //ワールドにいるプレイヤーリストを格納するListを宣言
	public static HashMap<UUID, Integer> scorelist = new HashMap<UUID, Integer>(); //UUIDとScoreDataを格納するHashMapを宣言
	public ScoreboardManagement scoreboardmanagement;
	String[] commands = { "MamiyaFumin", "fuminrank", "fumintop", "fuminstats", "fuminlevel", "fuminitemlist" };
	public FileConfiguration settingConfig;
	public PlayerListener pl = null;
	static int DisplayHours;
	TempFileManagement tfm;

	// playerdata.yml
	public CustomConfig customconfigCumulative;
	public FileConfiguration cumulativeplayerscoreConfig;

	// temp.yml
	public CustomConfig customconfigTemp;
	public FileConfiguration tempConfig;

	@Override
	public void onDisable() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDisable();
		tfm.saveScorelist();
	}

	@Override
	public void onEnable() {
		// TODO 自動生成されたメソッド・スタブ
		super.onEnable();
		info("MamiyaFumin is roading!");
		pl = new PlayerListener(this);

		// config.ymlが存在しない場合ファイルに出力
		saveDefaultConfig();
		// 設定を初期化
		InitializingSetting();

		// プレイヤーデータ
		customconfigCumulative = new CustomConfig(this, "playerdata.yml");
		customconfigCumulative.saveDefaultConfig();
		cumulativeplayerscoreConfig = customconfigCumulative.getConfig();

		// 一時ファイル
		customconfigTemp = new CustomConfig(this, "temp.yml");
		customconfigTemp.saveDefaultConfig();
		tempConfig = customconfigTemp.getConfig();

		//プラグインロード時にスコアデータを作成
		this.creatScore();

		plugin = this;
		MainCommands maincommand = new MainCommands();

		for (String command : commands) {
			getCommand(command).setExecutor(maincommand);
		}

		// タスクスケジューラ呼び出し
		new ScoreUpdate(this).runTaskTimer(this, 0, 20);
	}

	// 現在のワールドにいるプレイヤーとTempからスコアデータを作成
	public HashMap<UUID, Integer> creatScore() {
		// "world"にいる全プレイヤーの名前をリストに格納
		playerlist = this.getServer().getOnlinePlayers();
		// プレイヤーをUUIDに変換し点数0とセットでリストに格納

		for (Player player : playerlist) {
			UUID player_uuid = player.getUniqueId();
			Integer scoredata = player.getStatistic(Statistic.TIME_SINCE_REST) / Magnification;
			scorelist.put(player_uuid, scoredata);
		}
		tfm = new TempFileManagement(this);
		try {
			for (UUID uuid : tfm.restoreScorelist().keySet()) {
				int value = tfm.restoreScorelist().get(uuid);
				scorelist.put(uuid, value);
			}
		} catch (NullPointerException e) {
			// System.out.println("It is the first boot!");
		}

		return scorelist;
	}

	// プレイヤーのスコアに指定ポイント付与
	public void addScore(Player player, int point) {
		UUID uuid = player.getUniqueId();
		Integer temp = scorelist.get(uuid);
		int value = temp.intValue();
		value = value + point;
		Integer new_scoredata = new Integer(value);
		scorelist.put(uuid, new_scoredata);
	}

	// プレイヤーのスコアに指定ポイント付与
	public void addScore(UUID uuid, int point) {
		Integer temp = scorelist.get(uuid);
		int value = temp.intValue();
		value = value + point;
		Integer new_scoredata = new Integer(value);
		scorelist.put(uuid, new_scoredata);
	}

	public static MamiyaFumin getPlugin() {
		return plugin;
	}

	// プレイヤーの最後に就寝してからの経過時間をリセットする
	public static boolean resetStatistic(Player p, Statistic statistic) {
		try {
			int slr = p.getStatistic(statistic);
			if (slr == 0)
				return true;
			p.decrementStatistic(statistic, slr);
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}

	public void info(String s) {
		getLogger().info(s);
	}

	// 設定を読み込み
	void InitializingSetting() {
		// cinfig.ymlを読み込む
		settingConfig = getConfig();
		settingConfig.getBoolean("enablefumin");
		DisplayHours = settingConfig.getInt("ScoreboardDisplayHours");
		Magnification = settingConfig.getInt("FuminPointMagnification");
	}

	public URL getSiteURL() {
		URL url = null;
		try {
			url = new URL("https://github.com/rnlin430/MamiyaFumin/releases");
		} catch (MalformedURLException e) {
			info(ChatColor.GRAY + "未設定です。");
		}
		return url;
	}

}