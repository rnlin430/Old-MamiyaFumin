package com.github.rnlin;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PhantomMesasgeTask extends BukkitRunnable {

    private String phantomSpawnTimeMessage;
    private MamiyaFumin plugin;

    private List<Player> playerList = new ArrayList<>();

    public PhantomMesasgeTask(MamiyaFumin plugin, String message) {
        this.plugin = plugin;
        this.phantomSpawnTimeMessage = message;
    }

    @Nullable
    public String getPhantomSpawnTimeMessage() {
        return phantomSpawnTimeMessage;
    }

    public void setPhantomSpawnTimeMessage(@Nullable String message) {
        this.phantomSpawnTimeMessage = message;
    }

    public static boolean isSpawnPhantom(@NotNull Player player) {
        double s = player.getStatistic(Statistic.TIME_SINCE_REST);
        double phantomSpawnLimitTime = (20D*60D*20D*3D);
        if (s >= phantomSpawnLimitTime) {
            return true;
        } else {
            return false;
        }
    }

    private void sendMessage(@NotNull Player player) {
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
