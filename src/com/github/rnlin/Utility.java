package com.github.rnlin;

import com.github.rnlin.rnlibrary.ConsoleLog;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class Utility {



    // プレイヤーの最後に就寝してからの経過時間をリセットする
    // （現在のスコアをリセットするときは必ず累積スコアに現在のスコアを加算してから行ってください）
    public static boolean resetStatistic(Player p, Statistic statistic) {
        try {
            int slr = p.getStatistic(statistic);
            if (slr == 0)
                return true;
            p.decrementStatistic(statistic, slr);
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
        return true;
    }

    // プレイヤーの最後に就寝してからの経過時間をリセットする
    // （現在のスコアをリセットするときは必ず累積スコアに現在のスコアを加算してから行ってください）
    // オフラインかオンラインでも取得可能
    public static int getCurrentScore (UUID uuid) {
        try {
            return Objects.requireNonNull(MamiyaFumin.scoreList.get(uuid));
        } catch (NullPointerException e) {
            System.out.println("PlayerFumin.getCurrentScore():e => " + e);
            ConsoleLog.sendWarning("不眠ポイントの取得に失敗しました。");
            return 0;
        }
    }

    public static boolean setOffLinePlayerScore(UUID uuid, int point) {
        if(Bukkit.getOfflinePlayer(uuid).isOnline()) {
            ConsoleLog.sendDebugMessage("Utility.setOfflinePlayerScore(): =>" +
                    " Bukkit.getOfflinePlayer(uuid).isOnline() == true");
        }
        int score = MamiyaFumin.scoreList.get(uuid);
        MamiyaFumin.scoreList.put(uuid, score + point);
        return true;
    }
}
