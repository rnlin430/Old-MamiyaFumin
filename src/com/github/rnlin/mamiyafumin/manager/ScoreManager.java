package com.github.rnlin.mamiyafumin.manager;

import com.github.rnlin.MamiyaFumin;
import com.github.rnlin.PlayerFumin;
import com.github.rnlin.Utility;
import com.github.rnlin.mamiyafumin.api.MamiyaFuminAPI;
import com.github.rnlin.rnlibrary.ConsoleLog;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ScoreManager implements MamiyaFuminAPI {

    //Todo
    @Override
    public int getBestScore(Player player) {
        return 0;
    }

    //Todo
    @Override
    public int getBestScore(UUID uuid) {
        return 0;
    }

    @Override
    public int getTotalScore(Player player) {
        return MamiyaFumin.getPlayerFumin(player).getTotalScore();
    }

    @Override
    public int getTotalScore(UUID uuid) {
        return MamiyaFumin.cumulativeScore.get(uuid) + MamiyaFumin.scoreList.get(uuid);
    }

    @Override
    public int getCurrentScore(Player player) {
        return MamiyaFumin.getPlayerFumin(player).getCurrentScore();
    }

    @Override
    public int getCurrentScore(UUID uuid) {
        return Utility.getCurrentScore(uuid);
    }

    @Override
    public int[] getRanking(Player player) {
        return MamiyaFumin.getPlayerFumin(player).getRanking();
    }

    @Override
    public int[] getRanking(UUID uuid) {
        return new int[0];
    }

    @Override
    public boolean setScore(Player player, ScoreType type, int value) {
        switch(type) {
            case CURRENT:
                int currentScore;
                try {
                    currentScore = MamiyaFumin.getPlayerFumin(player).getCurrentScore();
                } catch (Exception e1) {
                    ConsoleLog.sendWarning("ScoreManager.setScore():e1 => ");
                    System.out.println(e1);
                    return false;
                }
                try {
                    if (currentScore < value) {
                        return MamiyaFumin.getPlayerFumin(player).increaseCurrentScore(value - currentScore);
                    } else if (value < currentScore) {
                        return MamiyaFumin.getPlayerFumin(player).decreaseCurrentScore(currentScore - value);
                    } else {
                        return true;
                    }
                } catch (Exception e2) {
                    ConsoleLog.sendWarning("ScoreManager.setScore():e2 => ");
                    System.out.println(e2);
                    return false;
                }
            case TOTAL:
                //Todo 実装
                return true;
            case BEST:
                //Todo 実装
                return true;
        }
        return false;
    }

    @Override
    public boolean setScore(UUID uuid, ScoreType type, int value) {

        switch(type) {
            case CURRENT:
                // プレイヤーがオンラインかどうか判定する
                if(Bukkit.getOfflinePlayer(uuid).isOnline()) {
                    Player player = Bukkit.getPlayer(uuid);
                    int currentScore = MamiyaFumin.getPlayerFumin(player).getCurrentScore();
                    if(value > currentScore) {
                        MamiyaFumin.getPlayerFumin(player).increaseCurrentScore(value - currentScore);
                    } else {
                        MamiyaFumin.getPlayerFumin(player).decreaseCurrentScore(currentScore - value);
                    }
                }
                else{
                    // スコアをセットする
                    MamiyaFumin.scoreList.put(uuid, value);
                    return true;
                }
            case TOTAL:
                //Todo 実装
                return true;
            case BEST:
                //Todo 実装
                return true;
        }
        return false;
    }

    @Override
    public boolean addScore(Player player, ScoreType type, int value) {
        return false;
    }

    @Override
    public boolean addScore(UUID uuid, ScoreType type, int value) {
        switch (type) {
            case CURRENT:
                // プレイヤーがオンラインかどうか判定する
                if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
                    Player player = Bukkit.getPlayer(uuid);
                    PlayerFumin pf = MamiyaFumin.getPlayerFumin(player);
                    if (0 <= value) {
                        pf.increaseCurrentScore(value);
                    } else {
                        pf.decreaseCurrentScore(value);
                    }
                    return true;
                } else {
                    // スコアをセットする
                    if (0 <= value) {
                        int result = MamiyaFumin.scoreList.get(uuid) + value;
                        MamiyaFumin.scoreList.put(uuid, result);
                    } else {
                        int result = MamiyaFumin.scoreList.get(uuid) + value;
                        if (0 <= result) {
                            MamiyaFumin.scoreList.put(uuid, result);
                        } else {
                            MamiyaFumin.scoreList.put(uuid, 0);
                        }
                        return true;
                    }
                    return true;
                }
            case TOTAL:
                return true;
            case BEST:
                return true;
        }
        return true;
    }

    @Override
    public boolean resetPlayerScore(Player player, ScoreType type, int value) {
        return false;
    }

    @Override
    public boolean resetPlayerScore(UUID uuid, ScoreType type, int value) {
        return false;
    }

    @Override
    public boolean removePlayer(Player player) {
        return false;
    }

    @Override
    public boolean removePlayer(UUID uuid) {
        return false;
    }
}
