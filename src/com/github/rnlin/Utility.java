package com.github.rnlin;

import com.github.rnlin.rnlibrary.FuminConsoleLog;
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
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // プレイヤーの現在のスコアを取得
    public static int getCurrentScore (UUID uuid) {
        try {
            return Objects.requireNonNull(MamiyaFumin.scoreList.get(uuid));
        } catch (NullPointerException e) {
            e.printStackTrace();
            FuminConsoleLog.sendWarning("不眠ポイントの取得に失敗しました。");
            return 0;
        }
    }

    // オフラインプレイヤーのスコアを指定分追加する
    public static boolean addOffLinePlayerScore(UUID uuid, int point) {
        if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
            FuminConsoleLog.sendDebugMessage("Utility.setOfflinePlayerScore(): =>" +
                    " Bukkit.getOfflinePlayer(uuid).isOnline() == true");
            return false;
        }
        int score = MamiyaFumin.scoreList.get(uuid);
        if (0 <= point) {
            MamiyaFumin.scoreList.put(uuid, score + point);
            return true;
        } else if (point < 0 && Math.abs(point) < score) {
            MamiyaFumin.scoreList.put(uuid, score + point);
            return true;
        } else if (point < 0 && Math.abs(point) >= score) {
            // MamiyaFumin.scoreList.put(uuid, 0);
            return false;

        }
        return false;
    }


}
