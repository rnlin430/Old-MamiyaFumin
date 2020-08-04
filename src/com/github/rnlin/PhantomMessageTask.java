package com.github.rnlin;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PhantomMessageTask extends BukkitRunnable {

    private String phantomSpawnTimeMessage;
    private MamiyaFumin plugin;

    private List<Player> playerList = new ArrayList<>();

    public PhantomMessageTask(MamiyaFumin plugin, String message) {
        this.plugin = plugin;
        this.phantomSpawnTimeMessage = message;
    }

    public String getPhantomSpawnTimeMessage() {
        return phantomSpawnTimeMessage;
    }

    public void setPhantomSpawnTimeMessage(String message) {
        this.phantomSpawnTimeMessage = message;
    }

    public static boolean isSpawnPhantom(Player player) {
        double s = player.getStatistic(Statistic.TIME_SINCE_REST);
        double phantomSpawnLimitTime = (20D*60D*20D*3D);
        if (s >= phantomSpawnLimitTime) {
            return true;
        } else {
            return false;
        }
    }

    private void sendMessage(Player player) {
        if (phantomSpawnTimeMessage == null) {
            return;
        }
        player.sendMessage(phantomSpawnTimeMessage);
    }

    private boolean canSendMessage(Player p) {
        if (playerList.contains(p)) {
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void run() {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (isSpawnPhantom(p)) {
                if (canSendMessage(p)) {
                    sendMessage(p);
                    playerList.add(p);
                } else {
                    continue;
                }
            } else {
                if (playerList.contains(p)) {
                    playerList.remove(p);
                }
            }
        }
    }
}
