package com.github.rnlin;

import org.bukkit.scheduler.BukkitRunnable;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.*;
import java.util.Map.Entry;

import static com.github.rnlin.MamiyaFumin.scoreList;

// 現在のスコア順位オブジェクト
// 一定時間毎に自動更新
// getRankingList()で取得できるリストは一定時間更新のため、遅延があります。
// 遅延の無いリアルタイムのランキングを取得する場合はrun()後にgetRankingList()で取得してください（CPUに高負荷のため連続使用は禁止）。
public class RankingManagement extends BukkitRunnable {
	MamiyaFumin plugin;
	private HashMap<String, Integer> stringScorelist;
	private HashMap<String, Integer> stringTotalScorelist;
	private HashMap<String, Integer> stringBestScorelist;

	private static List<Entry<String, Integer>> rankingScore;
	private static List<Entry<String, Integer>> rankingTotal;
	private static List<Entry<String, Integer>> rankingBest;

	enum ScoreType {
		CURRENT, TOTAL, BEST
	}

	public RankingManagement(MamiyaFumin p) {
		this.plugin = p;
		stringScorelist = new HashMap<>();
		stringTotalScorelist = new HashMap<>();
		stringBestScorelist = new HashMap<>();
	}

	@Override
	public void run() {
  //System.out.println("\u001b[31m" + "Class:RankingManagement.run() => update" + "\u001b[00m");
		convertToStringScoreList();
		convertToStringTotalList();
		convertToStringBestList();
		//Todo 非同期処理化
		rankingScore = getDescendingOrderScore();
		System.out.println(rankingScore);
		rankingTotal = getDescendingOrderTotalScore();
		rankingBest = getDescendingOrderBestScore();
	}

	// Score順位表示用リスト(StringName, value)作成
	private void convertToStringScoreList() {
		String playername;
		Integer value;
		// scorelist順位表示用リスト作成
		for (UUID uuid : MamiyaFumin.scoreList.keySet()) {
			// uuidからプレイヤーネームに変換
			try {
				playername = Objects.requireNonNull(plugin.getServer().getPlayer(uuid)).getName();
			} catch (Exception e) {
				// プレイヤーがオフラインの場合の処理
				playername = plugin.getServer().getOfflinePlayer(uuid).getName();
			}
			value = MamiyaFumin.scoreList.get(uuid);
			try {
				stringScorelist.put(playername, value);
			} catch (NullPointerException e) {
				System.out.println("RankingManagement.convertToStringScoreList(): => " + e);
			}
		}
	}

	// TotalScore順位表示用リスト(StringName, value)作成
	private void convertToStringTotalList() {
		String stringname;
		for (UUID uuid : MamiyaFumin.cumulativeScore.keySet()) {

			int value1 = MamiyaFumin.cumulativeScore.get(uuid);
			value1 += scoreList.get(uuid);
			try {
				stringname = plugin.getServer().getPlayer(uuid).getName();
			} catch (Exception e) {
				stringname = plugin.getServer().getOfflinePlayer(uuid).getName();
			}
			stringTotalScorelist.put(stringname, value1);
		}
//		Set<String> keys = plugin.cumulativePlayerscoreConfig.getKeys(false);
//		for (String key : keys) {
//			String stringname;
//			UUID uuid = UUID.fromString(key);
//
//			int value1 = Integer.parseInt(Objects.requireNonNull(plugin.cumulativePlayerscoreConfig.getString(
//					key + "." + FUMIN_TOTALSCORE_KEY)));
//			try {
//				stringname = plugin.getServer().getPlayer(uuid).getName();
//			} catch (Exception e) {
//				stringname = plugin.getServer().getOfflinePlayer(uuid).getName();
//			}
//			try {
//				// player.dataの値(ベッドに寝る、ベッド右クリック、afk時のトータルスコア) + 現在のスコアを計算
//				value1 = value1 + MamiyaFumin.scoreList.get(plugin.getServer().getPlayer(stringname).getUniqueId());
//			} catch (Exception e) {
//				OfflinePlayer op = plugin.getServer().getOfflinePlayer(uuid);
//				if (op.hasPlayedBefore()) {
//					value1 = value1 + MamiyaFumin.scoreList.get(op.getUniqueId());
//				}
//			}
//			stringTotalScorelist.put(stringname, value1);
//		}

		// トータルスコアがMamiyaFumin.cumulativeScoreに保存されていないプレイヤーの
		// 現在のスコアをトータルスコアとして順位表示用リストに保存（0（累積スコア） + 現在のスコア）
		for (String name : stringScorelist.keySet()) {
			if (!stringTotalScorelist.containsKey(name)) {
				stringTotalScorelist.put(name, stringScorelist.get(name));
			}
		}
	}

	// BestScore順位表示用リスト(StringName, value)作成
	private void convertToStringBestList() {
		String playername;
		int value;
		for (UUID uuid : MamiyaFumin.scoreBestlist.keySet()) {
			// uuidからプレイヤーネームに変換
			try {
				playername = Objects.requireNonNull(plugin.getServer().getPlayer(uuid).getName());
			} catch (Exception e) {
				// プレイヤーがオフラインの場合の処理
				playername = plugin.getServer().getOfflinePlayer(uuid).getName();
			}
			value = MamiyaFumin.scoreBestlist.get(uuid);
			try {
				stringBestScorelist.put(playername, value);
			} catch (NullPointerException e) {
				System.out.println("RankingManagement.convertToStringBestList(): => " + e);
			}
		}
	}

	// 遅延無しのランキングを取得する場合はrun()後にgetRankingListを使用してください
	public List<Entry<String, Integer>> getRankingList(ScoreType type) {
		if (type == ScoreType.CURRENT) {
			return rankingScore;
		} else if (type == ScoreType.TOTAL) {
			return rankingTotal;
		} else if (type == ScoreType.BEST) {
			return rankingBest;
		}
		return null;
	}

	// 降順
	private List<Entry<String, Integer>> getDescendingOrderScore() {

		List<Entry<String, Integer>> list_entries = null;
		try {
			list_entries = new ArrayList<Entry<String, Integer>>(stringScorelist.entrySet());

			Collections.sort(list_entries, new Comparator<Entry<String, Integer>>() {
				public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
					return obj2.getValue().compareTo(obj1.getValue());
				}
			});
			for (Entry<String, Integer> entry : list_entries) {
			}
		} catch (NullPointerException e) {
			System.out.println("RankingManagement.getDescendingOrderScore(): => " + e);
		}
		return list_entries;
	}

	private List<Entry<String, Integer>> getDescendingOrderTotalScore() {

		List<Entry<String, Integer>> list_entries = null;
		try {
			list_entries = new ArrayList<Entry<String, Integer>>(stringTotalScorelist.entrySet());

			Collections.sort(list_entries, new Comparator<Entry<String, Integer>>() {
				public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
					return obj2.getValue().compareTo(obj1.getValue());
				}
			});
			for (Entry<String, Integer> entry : list_entries) {
			}
		} catch (NullPointerException e) {
			System.out.println("RankingManagement.getDescendingOrderTotalScore(): => " + e);
		}
		return list_entries;
	}

	private List<Entry<String, Integer>> getDescendingOrderBestScore() {

		List<Entry<String, Integer>> list_entries = null;
		try {
			list_entries = new ArrayList<Entry<String, Integer>>(stringBestScorelist.entrySet());

			Collections.sort(list_entries, new Comparator<Entry<String, Integer>>() {
				public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
					return obj2.getValue().compareTo(obj1.getValue());
				}
			});
			for (Entry<String, Integer> entry : list_entries) {
// System.out.println(entry.getKey() + " : " + entry.getValue());
			}
		} catch (NullPointerException e) {
			System.out.println("RankingManagement.getDescendingOrderBestScore(): => " + e);
		}
		return list_entries;
	}


}
