package com.adcommon;

/**
 * Created by hanbing on 2016/3/2.
 * 获取积分，奖励或消费积分成功使用该接口返回
 */
public interface AdPointsListener {

    /**
     * 成功
     * @param amount 当前积分
     */
    public void onGetPoints(float amount);

    /**
     * 失败
     * @param message 失败信息
     */
    public void onFailure(String message);
}
