package com.github.rnlin;

import com.earth2me.essentials.Essentials;
import com.github.rnlin.mamiyafumin.api.MamiyaFuminAPI;
import com.github.rnlin.mamiyafumin.manager.ScoreManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * <pre>
 * MamiyaFumin プラグイン
 * スコア操作は MamiyFuminAPI から行ってください。
 * ランキングリストは原則RankingManagement.getRankingList(ScoreType)を用いて取得してください。
 * RankingManagementはgetRankingManagement()で取得します。
 * </pre>
 * @author rnlin
 */
public class MamiyaFumin extends JavaPlugin implements Listener {

	private static final long SCORE_UPDATE_FREQUENCY = 20L; // スコアアップデート更新頻度
	private static final long RANKING_CREATE_FREQUENCY = 300L; // ランキング更新頻度
	private static MamiyaFumin instance;
	static MamiyaFumin plugin;
	public static int magnification = 20 * 2;
	static int displayHours;
	protected static final String FUMIN_TOTALSCORE_KEY = ".FuminTotalScore";
	protected static final String FUMIN_BESTSCORE_KEY = ".FuminBestScore";

	private static RankingManagement rankingManagement;
	private static ScoreUpdate scoreUpdate;

	protected final String[] COMMANDS = new String[] {
			"mamiyafumin", "fuminrank", "fumintop", "fuminstats", "fuminlevel", "fuminitemlist", "fuminbest"
	};
	public String playerDeathMessage;

	Collection<? extends Player> playerList; // ワールドにいるプレイヤーリストを格納するListを宣言

	public static HashMap<UUID, Integer> scoreList = new HashMap<>(); // UUIDとScoreDataを格納するHashMapを宣言
	public static HashMap<UUID, Integer> cumulativeScore = new HashMap<>(); // トータルスコア = 累積スコア+現在のスコア
	public static HashMap<UUID, Integer> scoreBestlist  = new HashMap<>();

	public Essentials ess = (Essentials) this.getServer().getPluginManager().getPlugin("Essentials");
	public ScoreboardManagement scoreboardManagement;
	public FileConfiguration settingConfig;
	public PlayerListener playerListener = null;

	// playerdata.yml
	public CustomConfig customconfigCumulative;
	public FileConfiguration cumulativePlayerscoreConfig;

	// temp.yml
	public CustomConfig customconfigTemp;
	public FileConfiguration tempConfig;

	TempFileManagement tfm;
	private ScoreManager manager;
	public PhantomMesasgeTask phantomMessageTask;

	@Override
	public void onDisable() {
		super.onDisable();
		tfm.saveScorelist();
		saveCumulativeScoreList();
		saveScoreBestList();
	}

	// メモリ上のベストスコアをディスクに保存
	private void saveScoreBestList() throws NullPointerException {
		for (UUID uuid : scoreBestlist.keySet()) {

			String stringuuid = uuid.toString();
			int currentscore = Objects.requireNonNull(MamiyaFumin.scoreList.get(uuid));
			if (plugin.cumulativePlayerscoreConfig.contains(stringuuid + FUMIN_BESTSCORE_KEY)) {
				int result = Math.max(currentscore,
						plugin.cumulativePlayerscoreConfig.getInt(stringuuid + FUMIN_BESTSCORE_KEY));
				plugin.cumulativePlayerscoreConfig.set(stringuuid + FUMIN_BESTSCORE_KEY, result);
				plugin.customconfigCumulative.saveConfig();
			}
			else {
				plugin.cumulativePlayerscoreConfig.set(stringuuid + FUMIN_BESTSCORE_KEY, currentscore);
				plugin.customconfigCumulative.saveConfig();
			}
		}
	}

	// メモリ上の累積スコアをディスクに保存
	private void saveCumulativeScoreList() {
		for (UUID uuid : cumulativeScore.keySet()){
			int score = cumulativeScore.get(uuid);
			String stringuuid = uuid.toString();
			plugin.cumulativePlayerscoreConfig.set(stringuuid + FUMIN_TOTALSCORE_KEY, score);
		}
	}

	@Override
	public void onEnable() {
		super.onEnable();
		info("MamiyaFumin is roading!");
		manager = new ScoreManager();
		playerListener = new PlayerListener(this);

		this.phantomMessageTask = new PhantomMesasgeTask(getInstance(), null);
		phantomMessageTask.runTaskTimerAsynchronously(getInstance(), 20L, 20*20L);

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
		playerList = this.getServer().getOnlinePlayers();

		//プラグインロード時に各スコアデータを作成
		creatScore();
		creatBestScore();
		createCumulativeScore();

		plugin = this;
		MainCommands maincommand = new MainCommands();

		for (String command : COMMANDS) {
			getCommand(command).setExecutor(maincommand);
		}

		// タスクスケジューラ呼び出し
		scoreUpdate = new ScoreUpdate(this);
		scoreUpdate.runTaskTimer(this, 0L, SCORE_UPDATE_FREQUENCY);
		rankingManagement = new RankingManagement(this);
		rankingManagement.runTaskTimer(this, 4L, RANKING_CREATE_FREQUENCY);
	}

	/**
	 * MamiyaFumin APIを返します。
	 * @return MamiyaFumin API
	 */
	public MamiyaFuminAPI getMamiyaFuminAPI() {
		return manager;
	}

	/**
	 * MamiyaFumin インスタンスを返します。
	 * @return MamiyaFumin インスタンス
	 */
	public static MamiyaFumin getInstance() {
		if ( instance == null ) {
			instance = (MamiyaFumin) Bukkit.getPluginManager().getPlugin("MamiyaFumin");
		}
		return instance;
	}

	// RankingManagementオブジェクトを取得します
	public static RankingManagement getRankingManagement() throws NullPointerException {
		if(rankingManagement == null){
			throw new NullPointerException();
		}
		return rankingManagement;
	}

	// 現在のワールドにいるプレイヤーとTempからスコアデータを作成(プラグイン読み込み時に作成)
	private void creatScore() {
		// プレイヤーをUUIDに変換し点数0とセットでリストに格納
		for (Player player : playerList) {
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
			System.out.println("\u001b[33m" + "MamiyaFumin.createScore() => " + "\u001b[00m" + e);
		}
	}

	// player.ymlからベストスコアデータを作成(プラグイン読み込み時に作成)
	private void creatBestScore() {
		for (String key : cumulativePlayerscoreConfig.getKeys(false)) {
// System.out.println("\u001b[31m" + key + "\u001b[00m");
			int value = cumulativePlayerscoreConfig.getInt(key + FUMIN_BESTSCORE_KEY);
// System.out.println("\u001b[31m" + value + "\u001b[00m");
			UUID uuid = UUID.fromString(key);
			scoreBestlist.put(uuid, value);
		}
// System.out.println("\u001b[33m" + scoreBestlist + "\u001b[00m");
	}

	// player.ymlから累積スコアデータをメモリ上に作成(プラグイン読み込み時に作成)
	private void createCumulativeScore() {
		for (String key : cumulativePlayerscoreConfig.getKeys(false)) {
			int value = cumulativePlayerscoreConfig.getInt(key + FUMIN_TOTALSCORE_KEY);
			UUID uuid = UUID.fromString(key);
			cumulativeScore.put(uuid, value);
		}
	}

	public void info(String s) {
		getLogger().info(s);
	}

	// 設定を読み込み
	void initializingSetting() {
		// config.ymlを読み込む
		settingConfig = getConfig();
		settingConfig.getBoolean("enablefumin");
		displayHours = settingConfig.getInt("ScoreboardDisplayHours");
		magnification = settingConfig.getInt("FuminPointMagnification");
		phantomMessageTask.setPhantomSpawnTimeMessage(settingConfig.getString("SpawnPhantomMessage"));
		playerDeathMessage = settingConfig.getString("PlayerDeathMessage");
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

	public static PlayerFumin getPlayerFumin(Player player) {
		return new PlayerFumin(player);
	}

	public static ScoreUpdate getScoreUpdate() {
		return scoreUpdate;
	}

	public int getMagnification() {
		return magnification;
	}

}