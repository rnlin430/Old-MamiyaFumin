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
	static int magnification = 20 * 2;
	static int displayHours;

	private static long rankingCreateFrequency = 300L; // ランキング生成更新頻度

	protected final String[] COMMANDS = { "mamiyafumin", "fuminrank", "fumintop", "fuminstats", "fuminlevel", "fuminitemlist", "fuminbest" };

	protected Collection<? extends Player> playerlist; //ワールドにいるプレイヤーリストを格納するListを宣言

	public static HashMap<UUID, Integer> scoreList = new HashMap<UUID, Integer>(); // UUIDとScoreDataを格納するHashMapを宣言
	public static HashMap<UUID, Integer> scoreTotallist = new HashMap<UUID, Integer>(); // 未使用
	public static HashMap<UUID, Integer> scoreBestlist  = new HashMap<UUID, Integer>();

	public Essentials ess = (Essentials) this.getServer().getPluginManager().getPlugin("Essentials");
	public ScoreboardManagement scoreboardManagement;
	public FileConfiguration settingConfig;
	public PlayerListener playerListener = null;
	public ForReorderingPlayerScore forReorderingPlayerScore;

	// playerdata.yml
	public CustomConfig customconfigCumulative;
	public FileConfiguration cumulativePlayerscoreConfig;

	// temp.yml
	public CustomConfig customconfigTemp;
	public FileConfiguration tempConfig;

	TempFileManagement tfm;

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
		playerListener = new PlayerListener(this);

		// config.ymlが存在しない場合ファイルに出力
		saveDefaultConfig();
		// 設定を初期化
		initializingSetting();

		// プレイヤーデータ
		customconfigCumulative = new CustomConfig(this, "playerdata.yml");
		customconfigCumulative.saveDefaultConfig();
		cumulativePlayerscoreConfig = customconfigCumulative.getConfig();

		// 一時ファイル
		customconfigTemp = new CustomConfig(this, "temp.yml");
		customconfigTemp.saveDefaultConfig();
		tempConfig = customconfigTemp.getConfig();

		// "world"にいる全プレイヤーの名前をリストに格納
		playerlist = this.getServer().getOnlinePlayers();

		//プラグインロード時に各スコアデータを作成
		creatScore();
		creatBestScore();

		plugin = this;
		MainCommands maincommand = new MainCommands();

		for (String command : COMMANDS) {
			getCommand(command).setExecutor(maincommand);
		}

		// タスクスケジューラ呼び出し
		new ScoreUpdate(this).runTaskTimer(this, 0L, 20L);
		this.forReorderingPlayerScore = new ForReorderingPlayerScore(this).runTaskTimer(this, 4L, rankingCreateFrequency);
	}

	// 現在のワールドにいるプレイヤーとTempからスコアデータを作成
	private void creatScore() {
		// プレイヤーをUUIDに変換し点数0とセットでリストに格納
		for (Player player : playerlist) {
			UUID player_uuid = player.getUniqueId();
			Integer scoredata = player.getStatistic(Statistic.TIME_SINCE_REST) / magnification;
			scoreList.put(player_uuid, scoredata);
		}
		tfm = new TempFileManagement(this);
		try {
			for (UUID uuid : tfm.restoreScorelist().keySet()) {
				int value = tfm.restoreScorelist().get(uuid);
				scoreList.put(uuid, value);
			}
		} catch (NullPointerException e) {
			// System.out.println("It is the first boot!");
		}
		return;
	}

	// player.ymlからベストスコアデータを作成
	private void creatBestScore() {

		for (String key : cumulativePlayerscoreConfig.getKeys(false)) {
// System.out.println("\u001b[31m" + key + "\u001b[00m");
			int value = cumulativePlayerscoreConfig.getInt(key + ".FuminBestScore");
// System.out.println("\u001b[31m" + value + "\u001b[00m");
			UUID uuid = UUID.fromString(key);
			scoreBestlist.put(uuid, value);
		}
System.out.println("\u001b[33m" + scoreBestlist + "\u001b[00m");
	}

	// プレイヤーを追加
	public void addPlayer(Player player, int currentPoint, int totalPoint, int bestPoint) {

	}

	// プレイヤーのスコアに指定ポイント付与
	public void addScore(Player player, int point) {
		UUID uuid = player.getUniqueId();
		Integer temp = scoreList.get(uuid);
		int value = temp.intValue();
		value = value + point;
		Integer new_scoredata = new Integer(value);
		scoreList.put(uuid, new_scoredata);
	}

	// プレイヤーのスコアに指定ポイント付与
	public void addScore(UUID uuid, int point) {
		Integer temp = scoreList.get(uuid);
		int value = temp.intValue();
		value = value + point;
		Integer new_scoredata = new Integer(value);
		scoreList.put(uuid, new_scoredata);
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
	void initializingSetting() {
		// cinfig.ymlを読み込む
		settingConfig = getConfig();
		settingConfig.getBoolean("enablefumin");
		displayHours = settingConfig.getInt("ScoreboardDisplayHours");
		magnification = settingConfig.getInt("FuminPointMagnification");
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