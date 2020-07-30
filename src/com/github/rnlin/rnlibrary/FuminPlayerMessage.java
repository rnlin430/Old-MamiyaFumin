package com.github.rnlin.rnlibrary;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class FuminPlayerMessage {
    public static void sendDescription(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.AQUA + message);
    }

    public static void sendInfo(CommandSender sender, String message) {
        sender.sendMessage( ChatColor.AQUA + message);
    }

    public static void debugMessage(CommandSender sender, String message) {
        sender.sendMessage( "[Debug] " + ChatColor.GRAY + message);
    }

    public static void warningMessage(CommandSender sender, String message) {
        sender.sendMessage( ChatColor.RED + message);
    }

    public static void cautionMessage(CommandSender sender, String message) {
        sender.sendMessage( ChatColor.YELLOW + "" + ChatColor.BOLD + message);
    }
}
