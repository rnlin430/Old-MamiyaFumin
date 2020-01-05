package com.github.rnlin;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

public class PlayerFumin {

    private final Player player;

    public PlayerFumin(@Nonnull Player player) {
        this.player = player;
    }

    public int getCurrentScore() {
        UUID uuid = player.getUniqueId();
        try {
            return Objects.requireNonNull(MamiyaFumin.scoreList.get(uuid));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public int getTotalScore() {
        return 0;
    }

    public int[] getRanking() {
        return new int[]{0, 0, 0};
    }

    public boolean decreaseCurrentScore(int point) {
        if(getCurrentScore() - point < 0) return false;


        return true;
    }
}
