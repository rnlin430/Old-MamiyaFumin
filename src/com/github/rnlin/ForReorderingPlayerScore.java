package com.github.rnlin;

import java.util.*;
import java.util.Map.Entry;

import org.bukkit.OfflinePlayer;

// 現在のスコア順位オブジェクト
public class ForReorderingPlayerScore {
	MamiyaFumin plugin = null;
	private HashMap<String, Integer> stringScorelist;
	private HashMap<String, Integer> stringTotalScorelist;
	private HashMap<String, Integer> stringBestScorelist;

	enum scoretype {
		CURRENT, TOTAL, BEST
	}

	public ForReorderingPlayerScore(MamiyaFumin p) {
		this.plugin = p;
		stringScorelist = new HashMap<>();
		stringTotalScorelist = new HashMap<>();
		stringBestScorelist = new HashMap<>();

		convertToStringScoreList();
		convertToStringTotalList();
		convertToStringBestList();
System.out.println("\u001b[35m" + this.stringBestScorelist + "\u001b[00m");
	}

	// Score順位表示用リスト(StringName, value)作成
	private void convertToStringScoreList() {
		String playername;
		Integer value;
		// scorelist順位表示用リスト作成
		for (UUID uuid : MamiyaFumin.scorelist.keySet()) {
			// uuidからプレイヤーネームに変換
			try {
				playername = Objects.requireNonNull(plugin.getServer().getPlayer(uuid)).getName();
			} catch (Exception e) {
				// プレイヤーがオフラインの場合の処理
				playername = plugin.getServer().getOfflinePlayer(uuid).getName();
			}
			value = MamiyaFumin.scorelist.get(uuid);
			try {
				stringScorelist.put(playername, value);
			} catch (NullPointerException e) {
				System.out.println("1:" + e);
			}
		}
	}

	// TotalScore順位表示用リスト(StringName, value)作成
	private void convertToStringTotalList() {
		Set<String> keys = plugin.cumulativePlayerscoreConfig.getKeys(false);
		for (String key : keys) {
			String stringname;
			UUID uuid = UUID.fromString(key);

			int value1 = Integer.parseInt(Objects.requireNonNull(plugin.cumulativePlayerscoreConfig.getString(
					key + "." + PlayerListener.FUMIN_TOTALSCORE_KEY)));
			try {
				stringname = plugin.getServer().getPlayer(uuid).getName();
			} catch (Exception e) {
				stringname = plugin.getServer().getOfflinePlayer(uuid).getName();
			}
			try {
				// player.dataの値(ベッドに寝る、ベッド右クリック、afk時のトータルスコア) + 現在のスコアを計算
				value1 = value1 + MamiyaFumin.scorelist.get(plugin.getServer().getPlayer(stringname).getUniqueId());
			} catch (Exception e) {
				OfflinePlayer op = plugin.getServer().getOfflinePlayer(uuid);
				if (op.hasPlayedBefore()) {
					value1 = value1 + MamiyaFumin.scorelist.get(op.getUniqueId());
				}
			}
			stringTotalScorelist.put(stringname, value1);
		}
		// トータルスコアがplayer.dataに保存されていないプレイヤーの現在のスコアをトータルスコアとして順位表示用リストに保存
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
				System.out.println("1:" + e);
			}
		}
	}

	public List<Entry<String, Integer>> getRankingList(scoretype type) {
		if (type == scoretype.CURRENT) {
			return getDescendingOrderScore();
		} else if (type == scoretype.TOTAL) {
			return getDescendingOrderTotalScore();
		} else if (type == scoretype.BEST) {
			return getDescendingOrderBestScore();
		}
		return null;
	}

	// 降順
	private List<Entry<String, Integer>> getDescendingOrderScore() {

		List<Entry<String, Integer>> list_entries = null;
		try {
			list_entries = new ArrayList<Entry<String, Integer>>(stringScorelist.entrySet());

			Collections.sort(list_entries, new Comparator<Entry<String, Integer>>() {
				//compareを使用して値を比較する
				public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
					// 降順
					return obj2.getValue().compareTo(obj1.getValue());
				}
			});

			for (Entry<String, Integer> entry : list_entries) {
			}
		} catch (NullPointerException e) {
			System.out.println("getDescendingOrderScore():" + e);
		}
		return list_entries;
	}

	private List<Entry<String, Integer>> getDescendingOrderTotalScore() {

		List<Entry<String, Integer>> list_entries = null;
		try {
			list_entries = new ArrayList<Entry<String, Integer>>(stringTotalScorelist.entrySet());

			Collections.sort(list_entries, new Comparator<Entry<String, Integer>>() {
				//compareを使用して値を比較する
				public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
					// 降順
					return obj2.getValue().compareTo(obj1.getValue());
				}
			});

			for (Entry<String, Integer> entry : list_entries) {
			}
		} catch (NullPointerException e) {
			System.out.println("getDescendingOrderTotalScore():" + e);
		}
		return list_entries;
	}

	private List<Entry<String, Integer>> getDescendingOrderBestScore() {

		List<Entry<String, Integer>> list_entries = null;
		try {
			list_entries = new ArrayList<Entry<String, Integer>>(stringBestScorelist.entrySet());

			Collections.sort(list_entries, new Comparator<Entry<String, Integer>>() {
				//compareを使用して値を比較する
				public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
					// 降順
					return obj2.getValue().compareTo(obj1.getValue());
				}
			});

			for (Entry<String, Integer> entry : list_entries) {
				// System.out.println(entry.getKey() + " : " + entry.getValue());
			}
		} catch (NullPointerException e) {
			System.out.println("getDescendingOrderBestScore():" + e);
		}
		return list_entries;
	}


}
