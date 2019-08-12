package com.github.rnlin;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.rnlin.ForReorderingPlayerScore.scoretype;

public class MainCommands implements CommandExecutor {

	private MamiyaFumin plugin = MamiyaFumin.getPlugin();
	private static HashMap<Player, Integer> scoreboadkeeper = new HashMap<Player, Integer>();

	public MainCommands() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		if (cmd.getName().equalsIgnoreCase(plugin.COMMANDS[0])) {
			if (!sender.hasPermission("mamiyafumin.command.main.mamiyafumin")) {
				sender.sendMessage(ChatColor.DARK_RED + cmd.getPermissionMessage());
				return true;
			}

			Player player = (Player) sender;
			switch (args.length) {
			case 0:
				sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "■ MamiyaFumin ■");
				sender.sendMessage(ChatColor.WHITE + "Spigotバージョン : 1.13.2");
				sender.sendMessage(ChatColor.WHITE + "Pluginバージョン : " + plugin.getDescription().getVersion());
				sender.sendMessage(ChatColor.AQUA + "ダウンロードURL : " + plugin.getSiteURL());
				sender.sendMessage(ChatColor.AQUA + "コマンド一覧 : " + "/mamiyafumin command");
				sender.sendMessage(ChatColor.DARK_AQUA + "Developed by rnlin(Twitter: @rnlin)");
				sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "--------");
				return true;
			case 1:
				if (args[0].equalsIgnoreCase("on")) {
					plugin.settingConfig.set("enablefumin", true);
					plugin.saveConfig();
					sender.sendMessage(ChatColor.AQUA + "まみや不眠が有効になりました。");
					return true;
				} else if (args[0].equalsIgnoreCase("off")) {
					plugin.settingConfig.set("enablefumin", false);
					plugin.saveConfig();
					sender.sendMessage(ChatColor.AQUA + "まみや不眠が無効になりました。");
					return true;
				} else if (args[0].equalsIgnoreCase("reload")) {
					plugin.reloadConfig();
					plugin.initializingSetting();
					sender.sendMessage(ChatColor.GOLD + "リロードしました。");
					return true;
				} else if (args[0].equalsIgnoreCase("command")) {
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
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "fuminitemlist");
					sender.sendMessage(ChatColor.WHITE + "- 未実装です。");
					sender.sendMessage(ChatColor.WHITE + "- aliases: [fi,fmitemlist,fmil]");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "/permissions");
					sender.sendMessage(ChatColor.WHITE + "- パーミッションを表示します");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mamiyafumin reload");
					sender.sendMessage(ChatColor.WHITE + "- コンフィグをリロードします。");
					return true;
				} else if (args[0].equalsIgnoreCase("permissions")) {
					sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "- MamiyaFumin - パーミッション一覧 -");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mamiyafumin [command/reload/pemissions]");
					sender.sendMessage(
							ChatColor.WHITE + "mamiya.fumin.command.main.mamiyafumin" + ChatColor.RED + "OPコマンドです。");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "/fumintop [PageNumber]");
					sender.sendMessage(ChatColor.WHITE + "mamiya.fumin.command.fumintop");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "fuminstats [keep/clear]");
					sender.sendMessage(ChatColor.WHITE + "mamiya.fumin.command.fuminstats");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "fuminrank [PageNumber]");
					sender.sendMessage(ChatColor.WHITE + "mamiya.fumin.command.fuminrank");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "fuminlevel");
					sender.sendMessage(ChatColor.WHITE + "mamiya.fumin.command.fuminlevel");
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "fuminitemlist");
					sender.sendMessage(ChatColor.WHITE + "mamiya.fumin.command.fuminitemlist");
					return true;
				} else {
					return false;
				}
			case 2:
				if (args[0].equalsIgnoreCase("getPlayerScore")) {
					// 他プレイヤーのトータルスコア、スコア、順位を取得
					return true;
				}
				if (args[0].equalsIgnoreCase("setPlayerScore")) {
					// 他プレイヤーのトータルスコアをセット
					return true;
				}
				if (args[0].equalsIgnoreCase("addPlayerScore")) {

					return true;
				}
				if (args[0].equalsIgnoreCase("reducePlayerScore")) {

					return true;
				}
				if (args[0].equalsIgnoreCase("getitem")) {

					return true;
				}
				if (args[0].equalsIgnoreCase("addPlayerScore")) {

					return true;
				}
				if (args[0].equalsIgnoreCase("setMagnification")) {

					return true;
				}
			case 5:
				if(args[0].equalsIgnoreCase("setdummyplayer")){
					String name = args[1];
					String currentPoint = args[2];
					String totalPoint = args[3];
					String bestPoint = args[4];

					return true;
				}
				if(args[0].equalsIgnoreCase("reomvedummyplayer")){
					String name = args[1];
					String currentPoint = args[2];
					String totalPoint = args[3];
					String bestPoint = args[4];

					return true;
				}
				break;
			}
			// fumintop
		} else if (cmd.getName().equalsIgnoreCase(plugin.COMMANDS[2])) {
			switch (args.length) {
			case 0:
				sender.sendMessage(ChatColor.YELLOW + "--MamiyaFumin " + ChatColor.DARK_PURPLE + "Fumin Point"
						+ ChatColor.YELLOW + " Leaderboard---");
				boolean result = CommandProcessing.displayRanking(plugin, sender, "1", scoretype.CURRENT);
				sender.sendMessage(ChatColor.GOLD + "Tip: " + ChatColor.DARK_RED + "/fumintop [PageNumber]"
						+ ChatColor.GOLD + "  - ページ数を切り替えます。");
				return result;
			case 1:
				sender.sendMessage(ChatColor.YELLOW + "--MamiyaFumin " + ChatColor.DARK_PURPLE + "Fumin Point"
						+ ChatColor.YELLOW + " Leaderboard---");
				boolean result1 = CommandProcessing.displayRanking(plugin, sender, args[0], scoretype.CURRENT);
				sender.sendMessage(
						ChatColor.YELLOW + "-- page. " + ChatColor.GRAY + args[0] + ChatColor.YELLOW + " ---");
				return result1;
			}
			// fuminstats
		} else if (cmd.getName().equalsIgnoreCase(plugin.COMMANDS[3])) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("ゲーム内から実行してください");
				return true;
			}
			switch (args.length) {
			case 0:
				Player player = (Player) sender;
				ScoreboardManagement sbm = new ScoreboardManagement(player, plugin);
				sbm.updateScoreboarsRank(); // ランキングを更新
				sbm.setPlayerScoreboad();
				// MamiyaFumin.DisplayHours秒後に非表示
				new Countscheduler(plugin, sbm, player).runTaskLater(plugin, 20 * MamiyaFumin.displayHours);
				// 15tick毎に更新&MamiyaFumin.DisplayHours秒で更新を終了
				new UpdateScoreboard(plugin, MamiyaFumin.displayHours * (20 / 15), sbm).runTaskTimer(plugin, 0, 15);
				return true;
			case 1:
				ScoreboardManagement sbm1;
				if (args[0].equalsIgnoreCase("keep")) {
					Player player1 = (Player) sender;
					sbm1 = new ScoreboardManagement(player1, plugin);
					sbm1.updateScoreboarsRank();
					sbm1.setPlayerScoreboad();
					Integer value = new UpdateScoreboard(plugin, 50000, sbm1).runTaskTimer(plugin, 0, 15).getTaskId();
					scoreboadkeeper.put(player1, Integer.valueOf(value));
					return true;
				} else if (args[0].equalsIgnoreCase("clear")) {
					Player player1 = (Player) sender;
					sbm1 = new ScoreboardManagement(player1, plugin);
					sbm1.setPlayerScoreboad();
					sbm1.removeScoreboard(player1);
					try {
						int usb = scoreboadkeeper.get(player1);
						plugin.getServer().getScheduler().cancelTask(usb);
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("fuminstats:" + e);
					}
				}
			}
		}
		// fuminrank
		else if (cmd.getName().equalsIgnoreCase(plugin.COMMANDS[1])) {
			switch (args.length) {
			case 0:
				sender.sendMessage(ChatColor.YELLOW + "==MamiyaFumin " + ChatColor.DARK_GREEN + "Fumin TotalPoint"
						+ ChatColor.YELLOW + " Leaderboard===");
				boolean result = CommandProcessing.displayRanking(plugin, sender, "1", scoretype.TOTAL);
				sender.sendMessage(ChatColor.GOLD + "Tip: " + ChatColor.DARK_RED + "/fuminrank [PageNumber]"
						+ ChatColor.GOLD + "  - ページ数を切り替えます。");
				return result;

			case 1:
				sender.sendMessage(ChatColor.YELLOW + "==MamiyaFumin " + ChatColor.DARK_GREEN + "Fumin TotalPoint"
						+ ChatColor.YELLOW + " Leaderboard===");
				boolean result1 = CommandProcessing.displayRanking(plugin, sender, args[0], scoretype.TOTAL);
				sender.sendMessage(
						ChatColor.YELLOW + "-- page. " + ChatColor.GRAY + args[0] + ChatColor.YELLOW + " ---");
				return result1;
			}
		}
		// fuminlevel
		else if (cmd.getName().equalsIgnoreCase(plugin.COMMANDS[4])) {
			sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "");
			return true;

		}
		// fuminitemlist
		else if (cmd.getName().equalsIgnoreCase(plugin.COMMANDS[5])) {
			sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "");
			return true;
		}
		// fuminbest
		else if (cmd.getName().equalsIgnoreCase(plugin.COMMANDS[6])) {
			switch (args.length) {
				case 0:
					sender.sendMessage(ChatColor.YELLOW + "--MamiyaFumin " + ChatColor.DARK_PURPLE + "Fumin BestPoint"
							+ ChatColor.YELLOW + " Leaderboard---");
					boolean result = CommandProcessing.displayRanking(plugin, sender, "1", scoretype.BEST);
					sender.sendMessage(ChatColor.GOLD + "Tip: " + ChatColor.DARK_RED + "/fuminbest [PageNumber]"
							+ ChatColor.GOLD + "  - ページ数を切り替えます。");
					return result;
				case 1:
					sender.sendMessage(ChatColor.YELLOW + "--MamiyaFumin " + ChatColor.DARK_PURPLE + "Fumin BestPoint"
							+ ChatColor.YELLOW + " Leaderboard---");
					boolean result1 = CommandProcessing.displayRanking(plugin, sender, args[0], scoretype.BEST);
					sender.sendMessage(
							ChatColor.YELLOW + "-- page. " + ChatColor.GRAY + args[0] + ChatColor.YELLOW + " ---");
					return result1;
			}
			return true;
		}
		return false;
	}
}
