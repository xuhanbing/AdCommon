package com.adcommon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.widget.LinearLayout;

import ofs.ahd.dii.AdManager;
import ofs.ahd.dii.br.AdSize;
import ofs.ahd.dii.br.AdView;
import ofs.ahd.dii.br.AdViewListener;
import ofs.ahd.dii.listener.Interface_ActivityListener;
import ofs.ahd.dii.onlineconfig.OnlineConfigCallBack;
import ofs.ahd.dii.os.OffersManager;
import ofs.ahd.dii.os.OffersWallDialogListener;
import ofs.ahd.dii.os.PointsChangeNotify;
import ofs.ahd.dii.os.PointsManager;
import ofs.ahd.dii.st.SplashView;
import ofs.ahd.dii.st.SpotDialogListener;
import ofs.ahd.dii.st.SpotManager;

/**
 * Created by hanbing on 2016/3/2.
 */
public class YoumiAdEngine extends AdEngine implements PointsChangeNotify {


    String appId;
    String appSecret;


    public YoumiAdEngine() {

    }

    @Override
    public void init(Context context) {


        // 有米android 积分墙sdk 5.0.0之后支持定制浏览器顶部标题栏的部分UI
        // setOfferBrowserConfig();

        // (可选)关闭有米log输出，建议开发者在嵌入有米过程中不要关闭，以方便随时捕捉输出信息，出问题时可以快速定位问题
        // AdManager.getInstance(Context context).setEnableDebugLog(false);

        // 初始化接口，应用启动的时候调用，参数：appId, appSecret
        appId = getApplicationMetaData(context, "YOUMI_APP_ID");//"cfdbdd2786ea88ea";
        appSecret = getApplicationMetaData(context, "YOUMI_APP_SECRET");//"d8edde7d10dd0073";

        AdManager.getInstance(context).init(appId, appSecret);

        // 如果开发者使用积分墙的服务器回调,
        // 1.需要告诉sdk，现在采用服务器回调
        // 2.建议开发者传入自己系统中用户id（如：邮箱账号之类的）（请限制在50个字符串以内）
        // 3.务必在下面的OffersManager.getInstance(context).onAppLaunch();代码之前声明使用服务器回调

        // OffersManager.getInstance(context).setUsingServerCallBack(true);
        // OffersManager.getInstance(context).setCustomUserId("user_id");

        // 如果使用积分广告，请务必调用积分广告的初始化接口:
        OffersManager.getInstance(context).onAppLaunch();

        // (可选)注册积分监听-随时随地获得积分的变动情况
        PointsManager.getInstance(context).registerNotify(this);

        // (可选)注册积分订单赚取监听（sdk v4.10版本新增功能）
//        PointsManager.getInstance(context).registerPointsEarnNotify(this);

        // (可选)设置是否在通知栏显示下载相关提示。默认为true，标识开启；设置为false则关闭。（sdk v4.10版本新增功能）
        // AdManager.setIsDownloadTipsDisplayOnNotification(false);

        // (可选)设置安装完成后是否在通知栏显示已安装成功的通知。默认为true，标识开启；设置为false则关闭。（sdk v4.10版本新增功能）
        // AdManager.setIsInstallationSuccessTipsDisplayOnNotification(false);

        // (可选)设置是否在通知栏显示积分赚取提示。默认为true，标识开启；设置为false则关闭。
        // 如果开发者采用了服务器回调积分的方式，那么本方法将不会生效
        // PointsManager.setEnableEarnPointsNotification(false);

        // (可选)设置是否开启积分赚取的Toast提示。默认为true，标识开启；设置为false这关闭。
        // 如果开发者采用了服务器回调积分的方式，那么本方法将不会生效
        // PointsManager.setEnableEarnPointsToastTips(false);

        // -------------------------------------------------------------------------------------------
        // 积分墙SDK 5.3.0 新增分享任务，下面为新增接口

        // (可选) 获取当前应用的签名md5字符串，可用于申请微信appid时使用
        // 注意：获取是确保应用采用的是发布版本的签名而不是debug签名

        //		Log.d("youmi", String.format("包名：%s\n签名md5值：%s", context.getPackageName(),
        //				OffersManager.getInstance(context).getSignatureMd5String()));

        // (重要) 如果开发者需要开启分享任务墙，需要调用下面代码以注册微信appid（这里的appid为贵应用在微信平台上注册获取得到的appid）
        // 1. 微信的appid，请开发者在微信官网上自行注册
        // 2. 如果注册失败(返回false)，请参考/doc/有米AndroidSDK常见问题.html
        String wx_appid = getApplicationMetaData(context, "WX_APP_ID");//"wxbe86d519b643cf08";
        boolean isRegisterSuccess = OffersManager.getInstance(context).registerToWx(wx_appid);
    }

    @Override
    public void release(Context context) {

        // （可选）注销积分监听
        // 如果在onCreate调用了PointsManager.getInstance(this).registerNotify(this)进行积分余额监听器注册，那这里必须得注销
        PointsManager.getInstance(context).unRegisterNotify(this);

        // （可选）注销积分订单赚取监听
        // 如果在onCreate调用了PointsManager.getInstance(this).registerPointsEarnNotify(this)
        // 进行积分订单赚取监听器注册，那这里必须得注销
//        PointsManager.getInstance(context).unRegisterPointsEarnNotify(this);

        // 回收积分广告占用的资源
        OffersManager.getInstance(context).onAppExit();
    }

    @Override
    public void showPopAd(Context context, LinearLayout layout, final AdListener listener) {

        int orientation = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                ? SpotManager.ORIENTATION_LANDSCAPE : SpotManager.ORIENTATION_PORTRAIT;

        SpotManager.getInstance(context).setSpotOrientation(
                orientation);

        SpotManager.getInstance(context).setAnimationType(SpotManager.ANIM_ADVANCE);

        SpotManager.getInstance(context).showSpotAds(context, new SpotDialogListener() {
            @Override
            public void onShowSuccess() {

                if (null != listener) {
                    listener.onPopSuccess();
                }
            }

            @Override
            public void onShowFailed() {
                if (null != listener) {
                    listener.onPopFailure();
                }
            }

            @Override
            public void onSpotClosed() {
                if (null != listener) {
                    listener.onPopClose();
                }
            }

            @Override
            public void onSpotClick(boolean b) {

                if (null != listener) {
                    listener.onPopClick(b);
                }
            }
        });
    }

    public boolean dismissPopAd(Context context) {
        return SpotManager.getInstance(context).disMiss();
    }

    public void onDestory(Context context) {
        SpotManager.getInstance(context).onDestroy();
    }

    public void showSplashPopAd(Context context, Class<?> clazz, LinearLayout layout, final AdListener listener) {
        if (null == layout) {
            SpotManager.getInstance(context).showSplashSpotAds(context, clazz);
            return;
        }

        SpotManager.getInstance(context).loadSplashSpotAds();
        SplashView splashView = new SplashView(context, null);

        // 设置是否显示倒计时，默认显示
        splashView.setShowReciprocal(true);
        // 设置是否显示关闭按钮，默认不显示
        splashView.hideCloseBtn(true);
        if (null != clazz) {
            //传入跳转的intent，若传入intent，初始化时目标activity应传入null
            Intent intent = new Intent(context, clazz);
            splashView.setIntent(intent);
        }

        //展示失败后是否直接跳转，默认直接跳转
        splashView.setIsJumpTargetWhenFail(true);

        //开屏也可以作为控件加入到界面中。
        layout.addView(splashView.getSplashView());

        SpotManager.getInstance(context).showSplashSpotAds(context, splashView,
                new SpotDialogListener() {

                    @Override
                    public void onShowSuccess() {
                        if (null != listener) {
                            listener.onPopSuccess();
                        }
                    }

                    @Override
                    public void onShowFailed() {
                        if (null != listener) {
                            listener.onPopFailure();
                        }
                    }

                    @Override
                    public void onSpotClosed() {
                        if (null != listener) {
                            listener.onPopClose();
                        }
                    }

                    @Override
                    public void onSpotClick(boolean isWebPath) {
                        if (null != listener) {
                            listener.onPopClick(isWebPath);
                        }
                    }
                });
    }

    @Override
    public void showBannerAd(Context context, LinearLayout layout, final AdListener listener) {
        showBannerAd(context, layout, AdSize.FIT_SCREEN, listener);
    }

    /**
     * @param context
     * @param layout
     * @param adSize   自定义广告大小
     *                 AdSize.FIT_SCREEN    // 自适应屏幕宽度
     *                 AdSize.SIZE_320x50   // 手机
     *                 AdSize.SIZE_300x250  // 手机，平板
     *                 AdSize.SIZE_468x60   // 平板
     *                 AdSize.SIZE_728x90   // 平板
     * @param listener
     */
    public void showBannerAd(Context context, LinearLayout layout, AdSize adSize, final AdListener listener) {


        // 实例化广告条
        AdView adView = new AdView(context, adSize);

        adView.setAdListener(new AdViewListener() {
            @Override
            public void onReceivedAd(AdView adView) {

                if (null != listener) {
                    listener.onBannerSuccess();
                }
            }

            @Override
            public void onSwitchedAd(AdView adView) {
                if (null != listener) {
                    listener.onBannerSwitch();
                }
            }

            @Override
            public void onFailedToReceivedAd(AdView adView) {

                if (null != listener) {
                    listener.onBannerFailure();
                }
            }


        });

        // 将广告条加入到布局中
        layout.addView(adView);
    }

    @Override
    public void showMiniAd(Context context, LinearLayout layout, int interval, AdListener listener) {

    }

    @Override
    public void showPointsWallAd(Context context, String id, PointsWallAdType type, boolean useDialog, final AdListener listener) {


        if (PointsWallAdType.SHARE == type) {
            if (useDialog) {

//                // 同时本方法还支持以下重载
//
//// 传入对话框关闭监听器
//                OffersManager.getInstance(Context context).showShareWallDialog(Activity activity, OffersWallDialogListener listener);
//
//// 传入对话框宽高像素
//                OffersManager.getInstance(Context context).showShareWallDialog(Activity activity, int width, int height);
//
//// 传入对话框宽高像素以及关闭监听器
//                OffersManager.getInstance(Context context).showShareWallDialog(Activity activity, int width, int height, OffersWallDialogListener listener);
//
//// 传入对话框宽高所占屏幕比例（0~1）
//                OffersManager.getInstance(Context context).showShareWallDialog(Activity activity, double scaleOfScreenWidth, double scaleOfScreenHeight);
//
//// 传入对话框宽高所占屏幕比例（0~1）以及关闭监听器
//                OffersManager.getInstance(Context context).showShareWallDialog(Activity activity, double scaleOfScreenWidth, double scaleOfScreenHeight, OffersWallDialogListener listener);

                // 展示对话框的分享任务墙界面(本方法支持多种重载格式，开发者可以参考文档或者使用代码提示快捷键来了解)
                OffersManager.getInstance(context).showShareWallDialog((Activity) context, new OffersWallDialogListener
                        () {

                    @Override
                    public void onDialogClose() {
                        if (null != listener) {
                            listener.onPointsWallClose();
                        }
                    }
                });
            } else {

                // 调用方式一：直接打开全屏分享任务积分墙
                // OffersManager.getInstance(context).showShareWall();

                // 调用方式二：直接打开全屏分享任务积分墙，并且监听分享任务积分墙退出的事件onDestory
                OffersManager.getInstance(context).showShareWall(new Interface_ActivityListener() {

                    /**
                     * 当分享任务积分墙销毁的时候，即分享任务积分墙的Activity调用了onDestory的时候回调
                     */
                    @Override
                    public void onActivityDestroy(Context context) {
                        if (null != listener) {
                            listener.onPointsWallClose();
                        }
                    }
                });
            }

        } else {

            if (useDialog) {
                // 展示对话框的积分墙界面(本方法支持多种重载格式，开发者可以参考文档或者使用代码提示快捷键来了解)
                OffersManager.getInstance(context)
                        .showOffersWallDialog((Activity) context, new OffersWallDialogListener() {

                            @Override
                            public void onDialogClose() {
                                if (null != listener) {
                                    listener.onPointsWallClose();
                                }
                            }
                        });

            } else {

                // 调用方式一：直接打开全屏积分墙
                // OffersManager.getInstance(context).showOffersWall();

                // 调用方式二：直接打开全屏积分墙，并且监听积分墙退出的事件onDestory
                OffersManager.getInstance(context).showOffersWall(new Interface_ActivityListener() {

                    /**
                     * 当积分墙销毁的时候，即积分墙的Activity调用了onDestory的时候回调
                     */
                    @Override
                    public void onActivityDestroy(Context context) {
//                Toast.makeText(context, "全屏积分墙退出了", Toast.LENGTH_SHORT).show();

                        if (null != listener) {
                            listener.onPointsWallClose();
                        }
                    }

                });
            }


        }


    }

    @Override
    public void getCustomAdInfo(Context context, AdListener listener) {

    }

    @Override
    public void getCustomAdInfoList(Context context, AdListener listener) {

    }

    @Override
    public void showMore(Context context, String id, AdListener listener) {

    }


    @Override
    public void clickAd(Context context, String id, AdListener listener) {

    }

    @Override
    public void downloadAd(Context context, String id, AdListener listener) {

    }

    @Override
    public void getPoints(Context context, String id) {

        float amount = PointsManager.getInstance(context).queryPoints();

        onPointBalanceChange(amount);
    }

    @Override
    public void awardPoints(Context context, String id, float amount) {
        PointsManager.getInstance(context).awardPoints(amount);
    }

    @Override
    public void spendPoints(Context context, String id, float amount) {
        PointsManager.getInstance(context).spendPoints(amount);
    }

    @Override
    public void getConfig(Context context, String key, final AdListener listener) {

        // 注意：这里获取的在线参数的key为 ：isOpen，为演示的key ， 开发者需要将key替换为开发者在自己有米后台上面设置的key
        AdManager.getInstance(context).asyncGetOnlineConfig(key, new OnlineConfigCallBack() {

            /**
             * 获取在线参数成功就会回调本方法（本回调方法执行在UI线程中）
             */
            @Override
            public void onGetOnlineConfigSuccessful(String key, String value) {
                // 获取在线参数成功

                // //
                // 开发者在这里可以判断一下获取到的value值，然后设置一个boolean值并将其保存在文件中，每次调用广告之前从文件中获取boolean
                // 值并判断一下是否可以展示广告
                // if (key.equals("isOpen")) {
                // if (value.equals("1")) {
                // // 如果满足开发者自己的定义：如示例中如果key=isOpen value=1 则定义为开启广告
                // // 则将flag（boolean值）设置为true，然后每次调用广告代码之前都判断一下flag，如果flag为true则执行展示广告的代码
                // flag = true;
                // // 写入文件 ...
                // }
                // }

                if (null != listener) {
                    listener.onGetConfig(key, value);
                }

            }

            /**
             * 获取在线参数失败就会回调本方法（本回调方法执行在UI线程中）
             */
            @Override
            public void onGetOnlineConfigFailed(String key) {
                if (null != listener) {
                    listener.onGetConfig(key, null);
                }
            }
        });
    }

    @Override
    public void setConfig(Context context, String key, String value, AdListener listener) {

    }

    @Override
    public void onPointBalanceChange(float v) {

        if (null != adPointsListener) {
            adPointsListener.onGetPoints(v);
        }
    }


}
