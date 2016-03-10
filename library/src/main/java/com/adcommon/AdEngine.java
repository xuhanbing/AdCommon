package com.adcommon;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.LinearLayout;

/**
 * Created by hanbing on 2016/3/1.
 */
public abstract class AdEngine {

    public static enum  PointsWallAdType {
        ALL,
        APP,
        GAME,
        SHARE,
        OTHER,
    }

    public static String getApplicationMetaData(Context context, String key)
    {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != applicationInfo.metaData)
            {
                return  applicationInfo.metaData.getString(key);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

    protected AdPointsListener adPointsListener;

    /**
     * 初始化
     * @param context
     */
    public abstract void init(Context context);

    public final void init(Context context, AdPointsListener adPointsListener) {
        init(context);

        this.adPointsListener = adPointsListener;
    }

    /**
     * 释放资源
     * @param context
     */
    public abstract void release(Context context);

    /**
     * 展示弹出广告
     * @param context
     * @param layout 显示广告的控件
     * @param listener
     */
    public abstract void showPopAd(Context context, LinearLayout layout, AdListener listener);

    /**
     * 开屏广告
     *
     * @param context
     * @param clazz    需要跳转的activity
     * @param layout   如果容器为null，直接显示，否则添加到容器中
     * @param listener
     */
    public abstract void showSplashPopAd(Context context, Class<?> clazz, LinearLayout layout, final AdListener listener);

    /**
     * 展示互动广告
     * @param context
     * @param layout 显示广告的控件
     * @param listener
     */
    public abstract void showBannerAd(Context context, LinearLayout layout, AdListener listener);

    /**
     * 展示迷你广告
     * @param context
     * @param layout 显示广告的控件
     * @param interval 周期
     * @param listener
     */
    public abstract void showMiniAd(Context context, LinearLayout layout, int interval, AdListener listener);

    /**
     * 显示积分墙
     * @param context
     * @param id 用户id，如果需要
     * @param listener
     */
    public final void showPointsWallAd(Context context, String id, AdListener listener) {
        showPointsWallAd(context, id, PointsWallAdType.ALL, false, listener);
    }

    /**
     * 显示积分墙
     * @param context
     * @param id 用户id，如果需要
     * @param type
     * @param useDialog
     * @param listener
     */
    public abstract void showPointsWallAd(Context context, String id, PointsWallAdType type, boolean useDialog, AdListener listener);

    /**
     * 获取自定义广告元数据
     * @param context
     * @param listener
     */
    public abstract  void getCustomAdInfo(Context context, AdListener listener);


    /**
     *获取自定义广告元数据
     * @param context
     * @param listener
     */
    public abstract  void getCustomAdInfoList(Context context, AdListener listener);

    /**
     * 显示更多
     * @param context
     * @param id
     * @param listener
     */
    public abstract  void showMore(Context context, String id, AdListener listener);

    /**
     * 点击广告
     * 只在自定义广告时有效
     * @param context
     * @param id 广告id
     * @param listener
     */
    public abstract  void clickAd(Context context, String id, AdListener listener);


    /**
     * 点击广告，下载广告应用
     * 只在自定义广告时有效
     * @param context
     * @param id 广告id
     * @param listener
     */
    public abstract  void downloadAd(Context context, String id, AdListener listener);

    /**
     * 获取用户积分
     * @param context
     * @param id  用户id，如果需要
     */
    public abstract void getPoints(Context context, String id);

    /**
     * 奖励用户积分
     * @param context
     * @param id 用户id，如果需要
     * @param amount 数量
     */
    public abstract void awardPoints(Context context, String id, float amount);

    /**
     * 消费用户积分
     * @param context
     * @param id  用户id，如果需要
     * @param amount 数量
     */
    public abstract void spendPoints(Context context, String id, float amount);

    /**
     * 获取在线配置
     * @param context
     * @param key 配置名称
     * @param listener
     */
    public abstract void getConfig(Context context, String key, AdListener listener);

    /**
     * 设置在线配置
     * @param context
     * @param key 配置名称
     * @param value 配置值
     * @param listener
     */
    public abstract void setConfig(Context context, String key, String value, AdListener listener);
}
