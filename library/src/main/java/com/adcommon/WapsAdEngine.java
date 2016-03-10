package com.adcommon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import cn.waps.AdInfo;
import cn.waps.AppConnect;
import cn.waps.AppListener;
import cn.waps.UpdatePointsListener;

/**
 * Created by hanbing on 2016/3/2.
 */
public class WapsAdEngine extends AdEngine implements UpdatePointsListener {


    /**
     * AdInfoadInfo =AppConnect.getInstance(this).getAdInfo();//每次调用将自动轮换广告
     * StringadId		  = adInfo.getAdId();		//广告id
     * StringadName	  = adInfo.getAdName();	//广告标题
     * StringadText	  = adInfo.getAdText();		//广告语文字
     * BitmapadIcon	  = adInfo.getAdIcon();	//广告图标(48*48像素)
     * intadPoint		  = adInfo.getAdPoints();	//广告积分
     * String description = adInfo.getDescription(); //应用描述
     * String version 	  = adInfo.getVersion(); 	//程序版本
     * Stringfilesize	  = adInfo.getFilesize(); 	//安装包大小
     * String provider 	  = adInfo.getProvider();	//应用提供商
     * String[]imageUrls = adInfo.getImageUrls(); //应用截图的url数组，每个应用2张截图
     * StringadPackage = adInfo.getAdPackage();//广告应用包名
     * String action          = adInfo.getAction();     //用于存储“安装”或“注册”的字段
     */
    public static class AdListener2 extends com.adcommon.AdListener {
        public void onGetAdInfo(AdInfo adInfo) {

        }

        public void onGetAdInfoList(List<AdInfo> adInfoList) {

        }

        @Override
        public void onGetPoints(float amount) {
            super.onGetPoints(amount);
        }
    }

    /**
     * 是否需要弹出广告
     */
    boolean enablePopAd = true;

    /**
     * 是否需要自定义广告
     */
    boolean enableCustomAd = true;

    /**
     * 弹出广告是否可以按返回键关闭
     */
    boolean enablePopBack = true;

    Handler handler = new Handler();

    /**
     * 需要在AndroidManifest中添加配置APP_ID，APP_PID,WX_APP_ID
     *
     * @param context
     */
    @Override
    public void init(Context context) {

        String appId = getApplicationMetaData(context, "WAPS_APP_ID");
        String appPid = getApplicationMetaData(context, "WAPS_APP_PID");

        //微信开放平台申请审核通过后可获得
        String wx_appid = getApplicationMetaData(context, "WX_APP_ID");

        AppConnect.getInstance(appId, appPid, context);
        AppConnect.getInstance(context).setWeixinAppId(wx_appid, context);

        /**
         * 初始化弹出广告数据
         */
        if (enablePopAd) {
            AppConnect.getInstance(context).initPopAd(context);
        }

        /**
         * 初始化自定义广告数据
         */
        if (enableCustomAd) {
            AppConnect.getInstance(context).initAdInfo();
        }
    }

    private ApplicationInfo getApplicationInfo(Context context, ApplicationInfo applicationInfo) {
        try {
            applicationInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return applicationInfo;
    }

    @Override
    public void release(Context context) {
        AppConnect.getInstance(context).close();
    }

    @Override
    public void showPopAd(Context context, LinearLayout layout, final AdListener listener) {

        AppConnect instance = AppConnect.getInstance(context);
        instance.setPopAdBack(enablePopBack);


        if (null != layout) {

            if (instance.hasPopAd(context)) {
                int width = layout.getWidth() - layout.getPaddingLeft() - layout.getPaddingRight();
                int height = layout.getHeight() - layout.getPaddingTop() - layout.getPaddingBottom();


                View view = null;
                if (width > 0 && height > 0) {
                    view = instance.getPopAdView(context, width, height);
                } else {
                    view = instance.getPopAdView(context);
                }

                layout.addView(view);

                if (null != listener) {
                    listener.onPopSuccess();
                }

            }

        } else {
            instance.showPopAd(context, new AppListener() {

                @Override
                public void onPopClose() {
                    super.onPopClose();

                    if (null != listener) {
                        listener.onPopClose();
                    }
                }

                @Override
                public void onPopNoData() {
                    super.onPopNoData();

                    if (null != listener) {
                        listener.onPopFailure();
                    }
                }
            });
        }

    }

    @Override
    public void showSplashPopAd(final Context context, final Class<?> clazz, LinearLayout layout, final AdListener listener) {


        if (null == layout) {
            layout = new LinearLayout(context);

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layout.setLayoutParams(params);
            ((Activity) context).addContentView(layout, params);

        }

        final long delay = 5 * 1000;

        showPopAd(context, layout, new AdListener() {
            @Override
            public void onPopSuccess() {
                if (null != listener) {
                    listener.onPopSuccess();
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        context.startActivity(new Intent(context, clazz));
                        Activity activity = (Activity) context;
                        activity.finish();
                    }
                }, delay);

            }

            @Override
            public void onPopClick(boolean isWebPath) {

                if (null != listener) {
                    listener.onPopClick(isWebPath);
                }


            }

            @Override
            public void onPopClose() {
                if (null != listener) {
                    listener.onPopClose();
                }


            }

            @Override
            public void onPopFailure() {
                if (null != listener) {
                    listener.onPopFailure();
                }
            }
        });

    }

    @Override
    public void showBannerAd(Context context, LinearLayout layout, final AdListener listener) {

        AppConnect.getInstance(context).showBannerAd(context, layout, new AppListener() {
            @Override
            public void onBannerClose() {
                super.onBannerClose();

                if (null != listener) {
                    listener.onBannerClose();
                }
            }

            @Override
            public void onBannerNoData() {
                super.onBannerNoData();
                if (null != listener) {
                    listener.onBannerFailure();
                }
            }
        });
    }

    @Override
    public void showMiniAd(Context context, LinearLayout layout, int interval, AdListener listener) {
        if (interval <= 0)
            interval = 10;

//        //设置迷你广告背景颜色
//        AppConnect.getInstance(context).setAdBackColor(Color.RED);
//        //设置迷你广告广告诧颜色
//        AppConnect.getInstance(context).setAdForeColor(Color.YELLOW);
        //若未设置以上两个颜色，则默认为黑底白字
        AppConnect.getInstance(context).showMiniAd(context, layout, interval);
    }

    @Override
    public void showPointsWallAd(Context context, String id, PointsWallAdType type, boolean useDialog, final AdListener listener) {


        AppConnect instance = AppConnect.getInstance(context);
        instance.setOffersCloseListener(new AppListener() {
            @Override
            public void onOffersClose() {
                super.onOffersClose();

                if (null != listener) {
                    listener.onPointsWallClose();
                }
            }
        });

        if (PointsWallAdType.GAME == type) {
            instance.showGameOffers(context);
        } else if (PointsWallAdType.SHARE == type) {
            instance.showShareOffers(context, id);
        } else {
            instance.showOffers(context, id);
        }


    }

    @Override
    public void getCustomAdInfo(Context context, AdListener listener) {

        if (listener instanceof AdListener2) {
            ((AdListener2) listener).onGetAdInfo(AppConnect.getInstance(context).getAdInfo());
        }
    }

    @Override
    public void getCustomAdInfoList(Context context, AdListener listener) {

        if (listener instanceof AdListener2) {
            ((AdListener2) listener).onGetAdInfoList(AppConnect.getInstance(context).getAdInfoList());
        }

    }

    @Override
    public void showMore(Context context, String id, AdListener listener) {

        AppConnect.getInstance(context).showMore(context, id);

    }

    @Override
    public void clickAd(Context context, String id, AdListener listener) {

        //当广告被点击时，显示广告详情
        AppConnect.getInstance(context).clickAd(context, id);

    }

    @Override
    public void downloadAd(Context context, String id, AdListener listener) {
        //当用户确认要下载广告应用时，启动下载（仅适用于应用下载类广告）
        AppConnect.getInstance(context).downloadAd(context, id);

    }


    @Override
    public void getPoints(Context context, String id) {

        AppConnect.getInstance(context).getPoints(this);
    }

    @Override
    public void awardPoints(Context context, String id, float amount) {
        AppConnect.getInstance(context).awardPoints((int) amount, this);
    }

    @Override
    public void spendPoints(Context context, String id, float amount) {
        AppConnect.getInstance(context).spendPoints((int) amount, this);
    }

    @Override
    public void getConfig(Context context, final String key, final AdListener listener) {

        AppConnect.getInstance(context).getConfig(key, null, new AppListener() {
            @Override
            public void onGetConfig(String s) {
                super.onGetConfig(s);
                if (null != listener) {
                    listener.onGetConfig(key, s);
                }
            }
        });
    }

    @Override
    public void setConfig(Context context, String key, String value, AdListener listener) {
    }

    @Override
    public void getUpdatePoints(String s, final int i) {
        if (null != adPointsListener) {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    adPointsListener.onGetPoints(i);
                }
            });

        }
    }

    @Override
    public void getUpdatePointsFailed(final String s) {
        if (null != adPointsListener) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    adPointsListener.onFailure(s);
                }
            });

        }
    }

    public WapsAdEngine setMiniAdBackgroundColor(Context context, int color) {
        AppConnect.getInstance(context).setAdBackColor(color);

        return this;
    }

    public WapsAdEngine setMiniAdForegroundColor(Context context, int color) {
        AppConnect.getInstance(context).setAdForeColor(color);

        return this;
    }

    public WapsAdEngine setEnablePopAd(boolean enablePopAd) {
        this.enablePopAd = enablePopAd;

        return this;
    }

    public WapsAdEngine setEnableCustomAd(boolean enableCustomAd) {
        this.enableCustomAd = enableCustomAd;

        return this;
    }

    public WapsAdEngine setEnablePopBack(boolean enablePopBack) {
        this.enablePopBack = enablePopBack;

        return this;
    }


}
