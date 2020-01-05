package com.github.rnlin;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

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

    public boolean decreaseCurrentScore(int point) {
        if(getCurrentScore() - point < 0) return false;

        UUID uuid = player.getUniqueId();
        int score = MamiyaFumin.scoreList.get(uuid);
        // 現在のスコアからポイントを引いた値と、統計値からポイント分を引いた値が等しいかを確認
        if( (score - point == player.getStatistic(Statistic.TIME_SINCE_REST) - point * MamiyaFumin.magnification) ) {
            System.out.println("値が不正です。失敗しました。");
            return false;
        }
        player.decrementStatistic(Statistic.TIME_SINCE_REST, point * MamiyaFumin.magnification);
        MamiyaFumin.scoreList.put(player.getUniqueId(), score - point);
        return true;
    }
}
