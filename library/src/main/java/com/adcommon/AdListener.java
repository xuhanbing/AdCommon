package com.adcommon;

/**
 * Created by hanbing on 2016/3/1.
 */
public class AdListener implements  AdPointsListener {


    /**
     * 积分墙关闭
     */
    public void onPointsWallClose() {
    }

    /**
     * banner关闭
     */
    public void onBannerClose() {
    }


    public void onBannerSuccess()
    {

    }
    /**
     * banner没有数据
     */
    public void onBannerFailure() {
    }

    /**
     * banner切换
     */
    public void onBannerSwitch()
    {

    }



    public void onPopClick(boolean isWebPath)
    {

    }

    /**
     * 弹出广告关闭
     */
    public void onPopClose() {
    }

    public void onPopSuccess() {

    }

    /**
     * 弹出广告没有数据
     */
    public void onPopFailure() {
    }

    /**
     * 获取到在线配置参数
     * @param key
     * @param value
     */
    public void onGetConfig(String key, String value) {
    }

    @Override
    public void onGetPoints(float amount) {

    }

    @Override
    public void onFailure(String message) {

    }
}
