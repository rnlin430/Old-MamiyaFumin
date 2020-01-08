package com.github.rnlin;

import com.github.rnlin.rnlibrary.ConsoleLog;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

import static java.lang.Math.abs;

public class PlayerFumin {

    @NotNull
    private final Player player;

    public PlayerFumin(@Nonnull Player player) {
        this.player = player;
    }

    // 現在の不眠ポイントを取得します。最新の値を取得するにはMamiyaFumin.getScoreUpdate().run()を呼びます。
    public int getCurrentScore() {
        UUID uuid = player.getUniqueId();
        try {
            return Objects.requireNonNull(MamiyaFumin.scoreList.get(uuid));
        } catch (NullPointerException e) {
            System.out.println("PlayerFumin.getCurrentScore():e => " + e);
            ConsoleLog.sendWarning("不眠ポイントの取得に失敗しました。");
            return 0;
        }
    }

    //Todo 実装
    // プレイヤーを削除します。
    public void removePlayer() {
    }

    // 現在のトータルスコアを取得します。
    public int getTotalScore() {
        UUID uuid = player.getUniqueId();
        //リセットor減算された累積スコア + 現在のスコア
        int total = MamiyaFumin.cumulativeScore.get(uuid) + MamiyaFumin.scoreList.get(uuid);
        return total;
    }

    //Todo 実装
    // 現在のベストスコアを取得します。
    public int getBestScore() {
        UUID uuid = player.getUniqueId();
        int best = Math.max(player.getStatistic(Statistic.TIME_SINCE_REST)/MamiyaFumin.magnification,
                MamiyaFumin.scoreBestlist.get(uuid));
        return best;
    }

    //Todo 実装
    // 現在のランキング（現在、トータル、ベスト）を取得します。
    @NotNull
    public int[] getRanking() {
        // logic
        return new int[]{0, 0, 0};
    }

    public boolean increaseTotalScore(int point) {
        int value = MamiyaFumin.cumulativeScore.get(player.getUniqueId());
        MamiyaFumin.cumulativeScore.put(player.getUniqueId(), value + point);
        return true;
    }

    public boolean decreaseTotalScore(int point) {
        int value = MamiyaFumin.cumulativeScore.get(player.getUniqueId());
        if(value < point) return false;
        MamiyaFumin.cumulativeScore.put(player.getUniqueId(), value - point);
        return true;
    }

    // 現在のスコアを減らした分は、自動的にトータルスコアに加算されます。
    public boolean decreaseCurrentScore(int point) {
        int value = abs(point);
        if(getCurrentScore() - value < 0) return false;

        UUID uuid = player.getUniqueId();
        int score = MamiyaFumin.scoreList.get(uuid);
        // 現在のスコアからポイントを引いた値と、統計値からポイント分を引いた値が等しいかを確認
        // メモリ上のスコアは遅延があるため一致しない可能性がある
        if( (score - value != player.getStatistic(Statistic.TIME_SINCE_REST)/MamiyaFumin.magnification - value) ) {
            MamiyaFumin.getScoreUpdate().run(); //スコアを最新にする。
//            System.out.println("値が不正確な可能性があります。処理を中止しました。");
//            ConsoleLog.sendDebugMessage("メモリ上のスコア: " + score);
//            ConsoleLog.sendDebugMessage("統計値のスコア: " + player.getStatistic(Statistic.TIME_SINCE_REST)/MamiyaFumin.magnification);
//            PlayerMessage.debugMessage(player, "PlayerFumin.decreaseCurrentScore():値が不正確な可能性があります。処理を中止しました。" +
//                    "お手数ですが、管理者及びプラグイン制作者に連絡してください。");
//            return false;
        }
        player.decrementStatistic(Statistic.TIME_SINCE_REST, value * MamiyaFumin.magnification);
        MamiyaFumin.scoreList.put(player.getUniqueId(), score - value);
        increaseTotalScore(point); // 減らした分をトータルスコアに加算
        return true;
    }

    public boolean increaseCurrentScore(int point) {
        UUID uuid = player.getUniqueId();
        int score = MamiyaFumin.scoreList.get(uuid);
        // 現在のスコアからポイントを引いた値と、統計値からポイント分を引いた値が等しいかを確認
        // メモリ上のスコアは遅延があるため一致しない可能性がある
        if( (score + point != player.getStatistic(Statistic.TIME_SINCE_REST)/MamiyaFumin.magnification + point) ) {
            MamiyaFumin.getScoreUpdate().run(); //スコアを最新にする。
//            System.out.println("値が不正確な可能性があります。処理を中止しました。" );
//            ConsoleLog.sendDebugMessage("メモリ上のスコア: " + score);
//            ConsoleLog.sendDebugMessage("統計値のスコア: " + player.getStatistic(Statistic.TIME_SINCE_REST)/MamiyaFumin.magnification);
//            PlayerMessage.debugMessage(player, "PlayerFumin.decreaseCurrentScore():値が不正確な可能性があります。処理を中止しました。" +
//                    "お手数ですが、管理者及びプラグイン制作者に連絡してください。");
//            return false;
        }
        player.incrementStatistic(Statistic.TIME_SINCE_REST, point * MamiyaFumin.magnification);
        MamiyaFumin.scoreList.put(player.getUniqueId(), score + point);
        return true;
    }
}
