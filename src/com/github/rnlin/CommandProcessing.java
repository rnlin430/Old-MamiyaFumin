package com.github.rnlin;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.rnlin.ForReorderingPlayerScore.scoretype;

public class CommandProcessing {


	public CommandProcessing(MamiyaFumin plugin) {
	}

	static public boolean displayRanking(MamiyaFumin plugin, CommandSender sender, String args, scoretype type){
		try {
			ForReorderingPlayerScore playerscorei = new ForReorderingPlayerScore(plugin);
			List<Entry<String, Integer>> RANKINGi = playerscorei.getRankingList(type);

			if(Integer.parseInt(args) <= 0) {
				sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "1以上を指定してください。");
				return true;
			}
			int mini = (Integer.parseInt(args) * 10) - 10;
			int maxi = (Integer.parseInt(args) * 10);
			if(RANKINGi.size() < mini + 1) {
				sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "ページがありません。");
				return true;
			}

			// ページに表示する人数が10人未満の場合
			if(RANKINGi.size() - mini < 10) {
				List<Entry<String, Integer>> sbList = RANKINGi.subList(mini, RANKINGi.size());
				int[] value = new int[RANKINGi.size()];
				String[] name = new String[RANKINGi.size()];
				for(int i = 0; i < RANKINGi.size(); i++) {
					Entry<String, Integer> temp = sbList.get(i);
					value[i] = temp.getValue();
					name[i] = temp.getKey();
					String rank = String.valueOf(i + 1 + ((Integer.parseInt(args) - 1) * 10));
					sender.sendMessage(ChatColor.WHITE + rank + ". " + ChatColor.AQUA + name[i] +
							" - " + ChatColor.WHITE + value[i]);
				}
				return true;
			}
			// ページに表示する人数が10人以上の場合
			if(RANKINGi.size() - mini >= 10) {
				List<Entry<String, Integer>> sbListi = RANKINGi.subList(mini, maxi);
				int[] valuei = new int[10];
				String[] namei = new String[10];
				for(int i = 0; i < 10; i++) {
					Entry<String, Integer> temp = sbListi.get(i);
					valuei[i] = temp.getValue();
					namei[i] = temp.getKey();
					String rank = String.valueOf(i + 1 + ((Integer.parseInt(args) - 1) * 10));
					sender.sendMessage(ChatColor.WHITE + rank + ". " + ChatColor.AQUA +  namei[i] + " - " +
						ChatColor.WHITE + valuei[i]);
				}
				return true;
			}
		} catch (NullPointerException e) {
			System.out.println("displayRanking:" + e);
		}
		return false;

	}
}
