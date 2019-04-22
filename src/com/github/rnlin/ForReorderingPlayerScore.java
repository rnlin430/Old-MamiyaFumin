package com.github.rnlin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

public class ForReorderingPlayerScore {
	MamiyaFumin plugin = null;
	private HashMap<String, Integer> STRING_SCORELIST = new HashMap<>();
	private HashMap<String, Integer> STRING_TOTAL_SCORELIST = new HashMap<>();
	private String playername;
	private Integer value;
	public ForReorderingPlayerScore(MamiyaFumin p) {
		this.plugin = p;

		// scorelist中間作成
		for(UUID uuid : MamiyaFumin.scorelist.keySet()) {
			// uuidからプレイヤーネームに変換

			try {
				this.playername = plugin.getServer().getPlayer(uuid).getName();
			} catch (Exception e) {
				// プレイヤーがオフラインの場合の処理
				this.playername = plugin.getServer().getOfflinePlayer(uuid).getName();
			}
			this.value = MamiyaFumin.scorelist.get(uuid);
			try {
				for(String a : STRING_SCORELIST.keySet()){
					// System.out.println(a);
				}
				STRING_SCORELIST.put(playername, value);
			} catch (NullPointerException e) {
				System.out.println("1:" + e);
			}
		}

		// TotalScoreリスト作成
		Set<String> keys =  plugin.cumulativeplayerscoreConfig.getKeys(false);
		for(String key : keys) {
			String stringname;

			int value = Integer.parseInt(plugin.cumulativeplayerscoreConfig.getString(
					key + "." + PlayerListener.FUMIN_TOTALSCORE_KEY));
			try {
				stringname = plugin.getServer().getPlayer(UUID.fromString(key)).getName();
			} catch (Exception e) {
				stringname = plugin.getServer().getOfflinePlayer(UUID.fromString(key)).getName();
			}
			try {
				// player.dataの値（ベッドに寝る、ベッド右クリック、afk時のトータルスコア + 現在のスコアを計算
				value = value + MamiyaFumin.scorelist.get(plugin.getServer().getPlayer(stringname).getUniqueId());
			} catch (Exception e) {
				value = value + MamiyaFumin.scorelist.get(plugin.getServer().getOfflinePlayer(stringname).getUniqueId());
			}
			STRING_TOTAL_SCORELIST.put(stringname, value);
		}
		// トータルスコアがplayer.dataに保存されていないプレイヤーの現在のスコアをトータルスコアとしてリストに保存
		for(String name : STRING_SCORELIST.keySet()) {
			if(!STRING_TOTAL_SCORELIST.containsKey(name)) {
				STRING_TOTAL_SCORELIST.put(name,STRING_SCORELIST.get(name));
			}
		}
	}

	// 昇順
	public List<Entry<String, Integer>> getAscendingOrder() {

        List<Entry<String, Integer>> list_entries =
        		new ArrayList<Entry<String, Integer>>(STRING_SCORELIST.entrySet());

        Collections.sort(list_entries, new Comparator<Entry<String, Integer>>() {
            public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
                // 昇順
                return obj1.getValue().compareTo(obj2.getValue());
            }
        });
        return list_entries;
    }

	// 降順
	public List<Entry<String, Integer>> getDescendingOrderScore() {


        List<Entry<String, Integer>> list_entries = null;
        try {
        	list_entries = new ArrayList<Entry<String, Integer>>(STRING_SCORELIST.entrySet());

            Collections.sort(list_entries, new Comparator<Entry<String, Integer>>() {
                //compareを使用して値を比較する
                public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2)
                {
                    // 降順
                    return obj2.getValue().compareTo(obj1.getValue());
                }
            });

            for(Entry<String, Integer> entry : list_entries) {
                // System.out.println(entry.getKey() + " : " + entry.getValue());
            }
		} catch (NullPointerException e) {
			System.out.println("2:" + e);
		}
        return list_entries;
        }


	public List<Entry<String, Integer>> getDescendingOrderTotalScore() {

        List<Entry<String, Integer>> list_entries = null;
        try {
        	list_entries = new ArrayList<Entry<String, Integer>>(STRING_TOTAL_SCORELIST.entrySet());

            Collections.sort(list_entries, new Comparator<Entry<String, Integer>>() {
                //compareを使用して値を比較する
                public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2)
                {
                    // 降順
                    return obj2.getValue().compareTo(obj1.getValue());
                }
            });

            for(Entry<String, Integer> entry : list_entries) {
                // System.out.println(entry.getKey() + " : " + entry.getValue());
            }
		} catch (NullPointerException e) {
			System.out.println("2:" + e);
		}
        return list_entries;
        }
	}

