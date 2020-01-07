package com.github.rnlin;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

import static java.lang.Math.abs;

public class PlayerFumin {

    private final Player player;

    public PlayerFumin(@Nonnull Player player) {
        this.player = player;
    }

    // 現在の不眠ポイントを取得します。
    public int getCurrentScore() {
        UUID uuid = player.getUniqueId();
        try {
            return Objects.requireNonNull(MamiyaFumin.scoreList.get(uuid));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    //Todo 実装
    // 現在のトータル不眠ポイントを取得します。
    public int getTotalScore() {
        // logic
        return 0;
    }

    //Todo 実装
    // 現在のランキング（現在、トータル、ベスト）を取得します。
    public int[] getRanking() {
        // logic
        return new int[]{0, 0, 0};
    }

    //Todo 実装
    public boolean increaseTotalScore(int point) {
        // logic
        return true;
    }

    public boolean decreaseCurrentScore(int point) {
        int value = abs(point);
        if(getCurrentScore() - value < 0) return false;

        UUID uuid = player.getUniqueId();
        int score = MamiyaFumin.scoreList.get(uuid);
        // 現在のスコアからポイントを引いた値と、統計値からポイント分を引いた値が等しいかを確認
        if( (score - value == player.getStatistic(Statistic.TIME_SINCE_REST) - value * MamiyaFumin.magnification) ) {
            System.out.println("値が不正です。失敗しました。");
            return false;
        }
        player.decrementStatistic(Statistic.TIME_SINCE_REST, value * MamiyaFumin.magnification);
        MamiyaFumin.scoreList.put(player.getUniqueId(), score - value);
        return true;
    }

    public boolean increaseCurrentScore(int point) {
        UUID uuid = player.getUniqueId();
        int score = MamiyaFumin.scoreList.get(uuid);
        // 現在のスコアからポイントを引いた値と、統計値からポイント分を引いた値が等しいかを確認
        if( (score + point == player.getStatistic(Statistic.TIME_SINCE_REST) + point * MamiyaFumin.magnification) ) {
            System.out.println("値が不正です。失敗しました。");
            return false;
        }
        player.incrementStatistic(Statistic.TIME_SINCE_REST, point * MamiyaFumin.magnification);
        MamiyaFumin.scoreList.put(player.getUniqueId(), score + point);
        return true;
    }
}
