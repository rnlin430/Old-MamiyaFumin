package com.github.rnlin.mamiyafumin.api;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * MamiyaFumin API
 * @author rnlin
 */
public interface MamiyaFuminAPI {

    public enum ScoreType {CURRENT, TOTAL, BEST}
    /**
     * 指定したプレイヤーのベストスコアを取得します
     * @param player playerクラスのインスタンス
     * @return スコア
     */
    public int getBestScore(Player player);
    /**
     * 指定したプレイヤーのベストスコアを取得します
     * @param uuid プレイヤーのUUID
     * @return スコア
     */
    public int getBestScore(UUID uuid);
    /**
     * 指定したプレイヤーのトータルスコアを取得します
     * @param player playerクラスのインスタンス
     * @return スコア
     */
    public int getTotalScore(Player player);
    /**
     * 指定したプレイヤーのトータルスコアを取得します
     * @param uuid プレイヤーのUUID
     * @return スコア
     */
    public int getTotalScore(UUID uuid);

    /**
     * 指定したプレイヤーの現在のスコアを取得します
     * @param player playerクラスのインスタンス
     * @return スコア
     */
    public int getCurrentScore(Player player);
    /**
     * 指定したプレイヤーの現在のスコアを取得します
     * @param uuid プレイヤーのUUID
     * @return スコア
     */
    public int getCurrentScore(UUID uuid);

    /**
     * 指定したプレイヤーのランキングを取得します
     * @param player playerクラスのインスタンス
     * @return ランキング
     */
    public int[] getRanking(Player player);
    /**
     * 指定したプレイヤーのランキングを取得します
     * @param uuid プレイヤーのUUID
     * @return ランキング
     */
    public int[] getRanking(UUID uuid);

    /**
     * プレイヤーとスコアの種類を指定し、指定したポイントにします
     * @param player playerクラスのインスタンス
     * @param type スコアの種類
     * @param value ポイント
     */
    public boolean setScore(Player player, ScoreType type, int value);
    /**
     * プレイヤーとスコアの種類を指定し、指定したポイントにします
     * @param uuid プレイヤーのUUID
     * @param type スコアの種類
     * @param value ポイント
     */
    public boolean setScore(UUID uuid, ScoreType type, int value);

    /**
     * プレイヤーとスコアの種類を指定し、指定したポイントを追加します
     * @param player playerクラスのインスタンス
     * @param type スコアの種類
     * @param value ポイント
     * @return 追加できたかどうか
     */
    public boolean addScore(Player player, MamiyaFuminAPI.ScoreType type, int value);
    /**
     * プレイヤーとスコアの種類を指定し、指定したポイントを追加します
     * @param uuid プレイヤーのUUID
     * @param type スコアの種類
     * @param value ポイント
     * @return 追加できたかどうか
     */
    public boolean addScore(UUID uuid, MamiyaFuminAPI.ScoreType type, int value);

    /**
     * プレイヤーとスコアの種類を指定しリセットします
     * @param player playerクラスのインスタンス
     * @param type スコアの種類
     * @param value ポイント
     * @return リセットできたかどうか
     */
    public boolean resetPlayerScore(Player player, MamiyaFuminAPI.ScoreType type, int value);
    /**
     * プレイヤーとスコアの種類を指定しリセットします
     * @param uuid プレイヤーのUUID
     * @param type スコアの種類
     * @param value ポイント
     * @return リセットできたかどうか
     */
    public boolean resetPlayerScore(UUID uuid, MamiyaFuminAPI.ScoreType type, int value);

    /**
     * プレイヤーを不眠から削除します
     * @param player playerクラスのインスタンス
     * @return 削除できたかどうか
     */
    public boolean removePlayer(Player player);
    /**
     * プレイヤーを不眠から削除します
     * @param uuid プレイヤーのUUID
     * @return 削除できたかどうか
     */
    public boolean removePlayer(UUID uuid);
}
