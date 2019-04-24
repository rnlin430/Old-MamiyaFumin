package com.github.rnlin;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommands implements CommandExecutor{

	private MamiyaFumin plugin = MamiyaFumin.getPlugin();
	public static HashMap<Player, Integer> scoreboadkeeper= new HashMap<Player, Integer>();

	public MainCommands() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		if (cmd.getName().equalsIgnoreCase(plugin.commands[0])) {
			if(!sender.hasPermission("mamiya.fumin.command.main.mamiyafumin")) {
				sender.sendMessage(ChatColor.GRAY + cmd.getPermissionMessage());
				return true;
			}

			Player player = (Player) sender;
			switch(args.length) {
			case 0:
				sender.sendMessage(ChatColor.GOLD+ "" + ChatColor.BOLD + "■ MamiyaFumin ■");
				sender.sendMessage(ChatColor.WHITE + "Spigotバージョン : 1.13.2");
				sender.sendMessage(ChatColor.WHITE + "Pluginバージョン : " + plugin.getDescription().getVersion());
				sender.sendMessage(ChatColor.AQUA + "ダウンロードURL : " + plugin.getSiteURL());
				sender.sendMessage(ChatColor.AQUA + "コマンド一覧 : " + "/mamiyafumin command");
				sender.sendMessage(ChatColor.DARK_AQUA + "Developed by rnlin(Twitter: @rnlin)");
				sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "--------");
				return true;
			case 1:
				if(args[0].equalsIgnoreCase("on")){
					plugin.settingConfig.set("enablefumin", true);
					plugin.saveConfig();
					sender.sendMessage(ChatColor.AQUA + "まみや不眠が有効になりました。");
					return true;
				}
				else if(args[0].equalsIgnoreCase("off")) {
					plugin.settingConfig.set("enablefumin", false);
					plugin.saveConfig();
					sender.sendMessage(ChatColor.AQUA + "まみや不眠が無効になりました。");
					return true;
				}else if(args[0].equalsIgnoreCase("reload")) {
					plugin.reloadConfig();
					plugin.InitializingSetting();
					sender.sendMessage(ChatColor.GOLD + "リロードしました。");
					return true;
				}else if(args[0].equalsIgnoreCase("command")) {
					sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "- MamiyaFumin - コマンド一覧");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mamiyafumin");
					sender.sendMessage(ChatColor.WHITE + "- MamiyaFuminの詳細な情報です。");
					sender.sendMessage(ChatColor.WHITE + "- aliases: [mf,mfm]");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mamiyafumin command");
					sender.sendMessage(ChatColor.WHITE + "- 本プラグインのコマンド一覧を表示します。");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "/fumintop [PageNumber]");
					sender.sendMessage(ChatColor.WHITE + "- 現在のまんまみーや不眠ランキングを表示します。");
					sender.sendMessage(ChatColor.WHITE + "- aliases: [ft,fmtop,fmt]");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "fuminstats [keep/clear]");
					sender.sendMessage(ChatColor.WHITE + "- まんまみーや不眠ステータスを表示します。");
					sender.sendMessage(ChatColor.WHITE + "- aliases: [fs,fmstats,fms]");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "fuminrank [PageNumber]");
					sender.sendMessage(ChatColor.WHITE + "- トータルの不眠ランキングを表示します。");
					sender.sendMessage(ChatColor.WHITE + "- aliases: [fr,fmrank,fmr]");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "fuminlevel");
					sender.sendMessage(ChatColor.WHITE + "- 未実装です。");
					sender.sendMessage(ChatColor.WHITE + "- aliases: [fi,fmitemlist,fmil]");
					sender.sendMessage(ChatColor.LIGHT_PURPLE+ "fuminitemlist");
					sender.sendMessage(ChatColor.WHITE + "- 未実装です。");
					sender.sendMessage(ChatColor.WHITE + "- aliases: [fi,fmitemlist,fmil]");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "/permissions");
					sender.sendMessage(ChatColor.WHITE + "- パーミッションを表示します");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mamiyafumin reload");
					sender.sendMessage(ChatColor.WHITE + "- コンフィグをリロードします。");
					return true;
				}else if(args[0].equalsIgnoreCase("permissions")) {
					sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "- MamiyaFumin - パーミッション一覧 -");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mamiyafumin [command/reload/pemissions]");
					sender.sendMessage(ChatColor.WHITE + "mamiya.fumin.command.main.mamiyafumin"  + ChatColor.RED + "OPコマンドです。");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "/fumintop [PageNumber]");
					sender.sendMessage(ChatColor.WHITE + "mamiya.fumin.command.fumintop");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "fuminstats [keep/clear]");
					sender.sendMessage(ChatColor.WHITE + "mamiya.fumin.command.fuminstats");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "fuminrank [PageNumber]");
					sender.sendMessage(ChatColor.WHITE + "mamiya.fumin.command.fuminrank");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "fuminlevel");
					sender.sendMessage(ChatColor.WHITE + "mamiya.fumin.command.fuminlevel");
					sender.sendMessage(ChatColor.LIGHT_PURPLE+ "fuminitemlist");
					sender.sendMessage(ChatColor.WHITE + "mamiya.fumin.command.fuminitemlist");
					return true;
				}else {
					return false;
				}
			case 2:
				if(args[0].equalsIgnoreCase("getPlayerScore")) {
					// 他プレイヤーのトータルスコア、スコア、順位を取得
					return true;
				}
				if(args[0].equalsIgnoreCase("setPlayerScore")) {
					// 他プレイヤーのトータルスコアをセット
					return true;
				}
				if(args[0].equalsIgnoreCase("addPlayerScore")) {

					return true;
				}
				if(args[0].equalsIgnoreCase("reducePlayerScore")) {

					return true;
				}
				if(args[0].equalsIgnoreCase("getitem")) {

					return true;
				}
				if(args[0].equalsIgnoreCase("addPlayerScore")) {

					return true;
				}
				if(args[0].equalsIgnoreCase("setMagnification")) {

					return true;
				}
			case 3:
			}
		}else if(cmd.getName().equalsIgnoreCase(plugin.commands[2])) {
			switch(args.length) {
			case 0:
				sender.sendMessage(ChatColor.YELLOW + "--MamiyaFumin " + ChatColor.DARK_PURPLE + "Fumin Point"
						+ ChatColor.YELLOW + " Leaderboard---");
				if(!(sender instanceof Player)) return true;
				ForReorderingPlayerScore playerscore = new ForReorderingPlayerScore(plugin);
				List<Entry<String, Integer>> RANKING = playerscore.getDescendingOrderScore();
				try {
					if(RANKING.size() < 10) {
						if(RANKING.size() == 1) {
							Entry<String, Integer> temp = RANKING.get(0);
							int value = temp.getValue();
							String name = temp.getKey();
							sender.sendMessage(ChatColor.WHITE + "1. " + ChatColor.AQUA + name +
									" - " + ChatColor.WHITE + value);
							return true;
						}
						List<Entry<String, Integer>> sbList = RANKING.subList(0, RANKING.size());
						int[] value = new int[RANKING.size()];
						String[] name = new String[RANKING.size()];
						for(int i = 0; i < RANKING.size(); i++) {
							Entry<String, Integer> temp = sbList.get(i);
							value[i] = temp.getValue();
							name[i] = temp.getKey();
							String rank = String.valueOf(i + 1);
							sender.sendMessage(ChatColor.WHITE + rank + ". " + ChatColor.AQUA + name[i] +
									" - " + ChatColor.WHITE + value[i]);
						}
						sender.sendMessage(ChatColor.GOLD + "Tip: " + ChatColor.DARK_RED + "/fumintop [PageNumber]"
								+ ChatColor.GOLD + "  - ページ数を切り替えます。");
						return true;
					}
				} catch (NullPointerException e) {
					System.out.println("fumintop1:" + e);
					return true;
				}
				if(RANKING.size() >= 10) {
					try {
						List<Entry<String, Integer>> sbList = RANKING.subList(0, 10);
						int[] value = new int[10];
						String[] name = new String[10];
						for(int i = 0; i < 10; i++) {
							Entry<String, Integer> temp = sbList.get(i);
							value[i] = temp.getValue();
							name[i] = temp.getKey();
							String rank = String.valueOf(i + 1);
							sender.sendMessage(ChatColor.WHITE + rank + ". " + ChatColor.AQUA + name[i] + " - " + ChatColor.WHITE + value[i]);
						}
						sender.sendMessage(ChatColor.GOLD + "Tip: " + ChatColor.DARK_RED + "/fumintop [PageNumber]"
								+ ChatColor.GOLD + "  - ページ数を切り替えます。");
						return true;
					} catch (NullPointerException e) {
						System.out.println("fumintop2:" + e);
						return true;
					}
				}

			case 1:
				sender.sendMessage(ChatColor.YELLOW + "--MamiyaFumin " + ChatColor.DARK_PURPLE + "Fumin Point"
						+ ChatColor.YELLOW + " Leaderboard---");
				try {
					ForReorderingPlayerScore playerscorei = new ForReorderingPlayerScore(plugin);
					List<Entry<String, Integer>> RANKINGi = playerscorei.getDescendingOrderScore();
					int mini = (Integer.parseInt(args[0]) * 10) - 10;
					int maxi= (Integer.parseInt(args[0]) * 10) - 1;
					if(RANKINGi.size() < mini + 1) {
						sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "ページがありません。");
						return true;
					}
					// ページに表示する人数が10人以下の場合
					if(RANKINGi.size() - mini < 10) {
						List<Entry<String, Integer>> sbList = RANKINGi.subList(mini, RANKINGi.size());
						int[] value = new int[RANKINGi.size()];
						String[] name = new String[RANKINGi.size()];
						for(int i = 0; i < RANKINGi.size() - 1; i++) {
							Entry<String, Integer> temp = sbList.get(i);
							value[i] = temp.getValue();
							name[i] = temp.getKey();
							String rank = String.valueOf(i + 1 + ((Integer.parseInt(args[0]) - 1) * 10));
							sender.sendMessage(ChatColor.WHITE + rank + ". " + ChatColor.AQUA + name[i] +
									" - " + ChatColor.WHITE + value[i]);
						}
						sender.sendMessage(ChatColor.YELLOW + "-- page. " + ChatColor.GRAY + args[0] + ChatColor.YELLOW + " ---");
						return true;
					}

					List<Entry<String, Integer>> sbListi = RANKINGi.subList(mini, maxi + 1);
					int[] valuei = new int[10];
					String[] namei = new String[10];
					for(int i = 0; i < 10; i++) {
						Entry<String, Integer> temp = sbListi.get(i);
						valuei[i] = temp.getValue();
						namei[i] = temp.getKey();
						String rank = String.valueOf(i + 1 + ((Integer.parseInt(args[0]) - 1) * 10));
						sender.sendMessage(ChatColor.WHITE + rank + ". " + ChatColor.AQUA +  namei[i] + " - " +
						ChatColor.WHITE + valuei[i]);
					}
					sender.sendMessage(ChatColor.YELLOW + "-- page. " + ChatColor.GRAY + args[0] + ChatColor.YELLOW + " ---");
					return true;
				} catch (NullPointerException e) {
					System.out.println("fumintop3:" + e);
				}

			}
		}else if(cmd.getName().equalsIgnoreCase(plugin.commands[3])) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("ゲーム内から実行してください");
				return true;
			}
			switch(args.length) {
			case 0:
			Player player = (Player) sender;
			ScoreboardManagement sbm = new ScoreboardManagement(player, plugin);
			sbm.setPlayerScoreboad();
			// MamiyaFumin.DisplayHours秒後に非表示
			new Countscheduler(plugin, sbm, player).runTaskLater(plugin, 20*MamiyaFumin.DisplayHours);
			// 15tick毎に更新&MamiyaFumin.DisplayHours秒で更新を終了
			new UpdateScoreboard(plugin, MamiyaFumin.DisplayHours*(20/15), sbm).runTaskTimer(plugin, 0, 15);
			return true;
			case 1:
				ScoreboardManagement sbm1;
				if(args[0].equalsIgnoreCase("keep")){
					Player player1 = (Player) sender;
					sbm1 = new ScoreboardManagement(player1, plugin);
					sbm1.setPlayerScoreboad();
					Integer value = new UpdateScoreboard(plugin, 50000, sbm1).runTaskTimer(plugin, 0, 15).getTaskId();
					scoreboadkeeper.put(player1, Integer.valueOf(value));
					return true;
				}
				else if(args[0].equalsIgnoreCase("clear")) {
					Player player1 = (Player) sender;
					sbm1 = new ScoreboardManagement(player1, plugin);
					sbm1.setPlayerScoreboad();
					sbm1.removeScoreboard(player1);
					try {
						int usb = scoreboadkeeper.get(player1);
						plugin.getServer().getScheduler().cancelTask(usb);
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("fumintop4:" + e);
					}
				}
			}

		}else if (cmd.getName().equalsIgnoreCase(plugin.commands[1])) {
			switch(args.length) {
            case 0:
                sender.sendMessage(ChatColor.YELLOW + "==MamiyaFumin " + ChatColor.DARK_GREEN + "Fumin TotalPoint"
                        + ChatColor.YELLOW + " Leaderboard===");
                if(!(sender instanceof Player)) return true;
                ForReorderingPlayerScore playerscore = new ForReorderingPlayerScore(plugin);
                List<Entry<String, Integer>> RANKING = playerscore.getDescendingOrderTotalScore();
                try {
                    if(RANKING.size() < 10) {
                        if(RANKING.size() == 1) {
                            Entry<String, Integer> temp = RANKING.get(0);
                            int value = temp.getValue();
                            String name = temp.getKey();
                            sender.sendMessage(ChatColor.WHITE + "1. " + ChatColor.AQUA + name +
                                    " - " + ChatColor.WHITE + value);
                            return true;
                        }
                        List<Entry<String, Integer>> sbList = RANKING.subList(0, RANKING.size());
                        int[] value = new int[RANKING.size()];
                        String[] name = new String[RANKING.size()];
                        for(int i = 0; i < RANKING.size(); i++) {
                            Entry<String, Integer> temp = sbList.get(i);
                            value[i] = temp.getValue();
                            name[i] = temp.getKey();
                            String rank = String.valueOf(i + 1);
                            sender.sendMessage(ChatColor.WHITE + rank + ". " + ChatColor.AQUA + name[i] +
                                    " - " + ChatColor.WHITE + value[i]);
                        }
                        sender.sendMessage(ChatColor.GOLD + "Tip: " + ChatColor.DARK_RED + "/fuminrank [PageNumber]"
                                + ChatColor.GOLD + "  - ページ数を切り替えます。");
                        return true;
                    }
                } catch (NullPointerException e) {
                    System.out.println("fumintop1:" + e);
                    return true;
                }
                if(RANKING.size() >= 10) {
                    try {
                        List<Entry<String, Integer>> sbList = RANKING.subList(0, 10);
                        int[] value = new int[10];
                        String[] name = new String[10];
                        for(int i = 0; i < 10; i++) {
                            Entry<String, Integer> temp = sbList.get(i);
                            value[i] = temp.getValue();
                            name[i] = temp.getKey();
                            String rank = String.valueOf(i + 1);
                            sender.sendMessage(ChatColor.WHITE + rank + ". " + ChatColor.AQUA + name[i] + " - " + ChatColor.WHITE + value[i]);
                        }
                        sender.sendMessage(ChatColor.GOLD + "Tip: " + ChatColor.DARK_RED + "/fuminrank [PageNumber]"
                                + ChatColor.GOLD + "  - ページ数を切り替えます。");
                        return true;
                    } catch (NullPointerException e) {
                        System.out.println("fumintop2:" + e);
                        return true;
                    }
                }

            case 1:
                sender.sendMessage(ChatColor.YELLOW + "==MamiyaFumin " + ChatColor.DARK_GREEN + "Fumin TotalPoint"
                        + ChatColor.YELLOW + " Leaderboard===");
                try {
                    ForReorderingPlayerScore playerscorei = new ForReorderingPlayerScore(plugin);
                    List<Entry<String, Integer>> RANKINGi = playerscorei.getDescendingOrderTotalScore();
                    int mini = (Integer.parseInt(args[0]) * 10) - 10;
                    int maxi= (Integer.parseInt(args[0]) * 10) - 1;
                    if(RANKINGi.size() < mini + 1) {
                        sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "ページがありません。");
                        return true;
                    }
                    // ページに表示する人数が10人以下の場合
                    if(RANKINGi.size() - mini < 10) {
                        List<Entry<String, Integer>> sbList = RANKINGi.subList(mini, RANKINGi.size());
                        int[] value = new int[RANKINGi.size()];
                        String[] name = new String[RANKINGi.size()];
                        for(int i = 0; i < RANKINGi.size() - 1; i++) {
                            Entry<String, Integer> temp = sbList.get(i);
                            value[i] = temp.getValue();
                            name[i] = temp.getKey();
                            String rank = String.valueOf(i + 1 + ((Integer.parseInt(args[0]) - 1) * 10));
                            sender.sendMessage(ChatColor.WHITE + rank + ". " + ChatColor.AQUA + name[i] +
                                    " - " + ChatColor.WHITE + value[i]);
                        }
                        sender.sendMessage(ChatColor.YELLOW + "-- page. " + ChatColor.GRAY + args[0] + ChatColor.YELLOW + " ---");
                        return true;
                    }

                    List<Entry<String, Integer>> sbListi = RANKINGi.subList(mini, maxi + 1);
                    int[] valuei = new int[10];
                    String[] namei = new String[10];
                    for(int i = 0; i < 10; i++) {
                        Entry<String, Integer> temp = sbListi.get(i);
                        valuei[i] = temp.getValue();
                        namei[i] = temp.getKey();
                        String rank = String.valueOf(i + 1 + ((Integer.parseInt(args[0]) - 1) * 10));
                        sender.sendMessage(ChatColor.WHITE + rank + ". " + ChatColor.AQUA +  namei[i] + " - " +
                        ChatColor.WHITE + valuei[i]);
                    }
                    sender.sendMessage(ChatColor.YELLOW + "-- page. " + ChatColor.GRAY + args[0] + ChatColor.YELLOW + " ---");
                    return true;
                } catch (NullPointerException e) {
                    System.out.println("fumintop3:" + e);
                }
                return false;
            }
        }else if (cmd.getName().equalsIgnoreCase(plugin.commands[4])) {
        	sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "未実装なのです。");
        	return true;
        }else if (cmd.getName().equalsIgnoreCase(plugin.commands[5])) {
        	sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "未実装なのです。");
        	return true;
        }
		return false;
	}
}
